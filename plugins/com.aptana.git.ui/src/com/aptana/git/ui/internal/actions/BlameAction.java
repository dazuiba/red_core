package com.aptana.git.ui.internal.actions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.internal.text.revisions.Colors;
import org.eclipse.jface.text.revisions.Revision;
import org.eclipse.jface.text.revisions.RevisionInformation;
import org.eclipse.jface.text.source.LineRange;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.team.ui.TeamUI;
import org.eclipse.team.ui.history.IHistoryPage;
import org.eclipse.team.ui.history.IHistoryView;
import org.eclipse.team.ui.history.RevisionAnnotationController;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import com.aptana.git.core.GitPlugin;
import com.aptana.git.core.model.GitCommit;
import com.aptana.git.core.model.GitExecutable;
import com.aptana.git.core.model.GitRepository;
import com.aptana.git.ui.GitUIPlugin;
import com.aptana.git.ui.actions.GitAction;
import com.aptana.git.ui.internal.QuickDiffReferenceProvider;
import com.aptana.git.ui.internal.history.GitHistoryPage;

@SuppressWarnings("restriction")
public class BlameAction extends GitAction implements IEditorActionDelegate
{

	private IEditorPart fEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor)
	{
		this.fEditor = targetEditor;
	}

	@Override
	public void run()
	{
		IResource[] resources = getSelectedResources();
		for (IResource resource : resources)
		{
			if (resource.getType() != IResource.FILE)
			{
				continue;
			}
			final IFile file = (IFile) resource;
			final GitRepository repo = GitPlugin.getDefault().getGitRepositoryManager().getAttached(file.getProject());
			RevisionInformation info = createRevisionInformation(repo, repo.relativePath(file));

			if (fEditor == null && getTargetPart() instanceof AbstractDecoratedTextEditor)
			{
				fEditor = (AbstractDecoratedTextEditor) getTargetPart();
			}

			if (fEditor != null)
			{
				((AbstractDecoratedTextEditor) fEditor).showRevisionInformation(info, QuickDiffReferenceProvider.ID);
				IWorkbenchPage page = getActivePage();
				if (page != null)
				{
					attachHistorySyncher(file, repo, page);
				}
			}
			else
			{
				IWorkbenchPage page = getActivePage();
				if (page != null)
				{
					try
					{
						AbstractDecoratedTextEditor editor = RevisionAnnotationController.openEditor(page, file);
						editor.showRevisionInformation(info, QuickDiffReferenceProvider.ID);
						attachHistorySyncher(file, repo, page);
					}
					catch (PartInitException e)
					{
						GitUIPlugin.logError(e);
					}
				}
			}
		}
	}

	protected IHistoryView attachHistorySyncher(final IFile file, final GitRepository repo, IWorkbenchPage page)
	{
		IHistoryView historyView = TeamUI.getHistoryView();
		if (historyView != null)
		{
			IHistoryPage historyPage = historyView.getHistoryPage();
			if (historyPage instanceof GitHistoryPage)
			{
				new RevisionAnnotationController(page, file, ((GitHistoryPage) historyPage).getSelectionProvider())
				{

					@Override
					protected Object getHistoryEntry(Revision selected)
					{
						String sha = selected.getId();
						return new GitCommit(repo, sha);
					}
				};
			}
		}
		return historyView;
	}

	private IWorkbenchPage getActivePage()
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
		{
			return null;
		}
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null)
		{
			return null;
		}
		return window.getActivePage();
	}

	private RevisionInformation createRevisionInformation(GitRepository repo, IPath relativePath)
	{
		// Run git blame on the file, parse out the output and turn it into revisions!
		Map<Integer, String> result = GitExecutable.instance().runInBackground(repo.workingDirectory(), "blame", "-p", //$NON-NLS-1$ //$NON-NLS-2$
				relativePath.toOSString());
		if (result == null || result.keySet().iterator().next() != 0)
		{
			return new RevisionInformation();
		}

		String output = result.values().iterator().next();
		Map<String, GitRevision> revisions = new HashMap<String, GitRevision>();

		String[] lines = output.split("\r\n|\r|\n"); //$NON-NLS-1$
		GitRevision revision = null;
		String sha = null;
		String author = null;
		String committer = null;
		int finalLine = 1;
		int numberOfLines = 1;
		Calendar timestamp = null;
		String summary = null;
		for (String line : lines)
		{
			if (sha == null)
			{
				// first line, grab the sha, line numbers
				String[] parts = line.split("\\s"); //$NON-NLS-1$
				sha = new String(parts[0]);
				finalLine = Integer.parseInt(parts[2]);
				if (parts.length > 3)
				{
					numberOfLines = Integer.parseInt(parts[3]);
				}
			}
			else if (line.startsWith("author ")) //$NON-NLS-1$
			{
				author = new String(line.substring(7));
			}
			else if (line.startsWith("summary ")) //$NON-NLS-1$
			{
				summary = new String(line.substring(8));
			}
			else if (line.startsWith("committer-time ")) //$NON-NLS-1$
			{
				String time = line.substring(15);
				long timeValue = Long.parseLong(time) * 1000;
				timestamp = Calendar.getInstance();
				timestamp.setTimeInMillis(timeValue);
			}
			else if (line.startsWith("committer ")) //$NON-NLS-1$
			{
				committer = new String(line.substring(10));
			}
			else if (line.charAt(0) == '\t')
			{
				// it's the actual source, we're done with the revision!
				revision = revisions.get(sha);
				if (revision == null)
				{
					revision = new GitRevision(sha, author, committer, summary, timestamp.getTime());
				}
				// Need to shift line numbers down one to match properly
				revision.addRange(new LineRange(finalLine - 1, numberOfLines));
				revisions.put(sha, revision);
				sha = null;
				revision = null;
				author = null;
				committer = null;
				timestamp = null;
				summary = null;
				numberOfLines = 1;
				finalLine = 1;
			}
		}

		RevisionInformation info = new RevisionInformation();
		if (!revisions.isEmpty())
		{
			List<String> uniqueAuthors = new ArrayList<String>();
			for (GitRevision rev : revisions.values())
			{
				if (uniqueAuthors.contains(rev.getAuthor()))
				{
					continue;
				}
				uniqueAuthors.add(rev.getAuthor());
			}
			// Assign unique colors!
			RGB[] colors;
			if (uniqueAuthors.size() < 2)
			{
				colors = new RGB[] { new RGB(255, 0, 0) };
			}
			else
			{
				// FIXME Shouldn't be accessing this class, so we'll need to create our own version eventually
				colors = Colors.rainbow(uniqueAuthors.size());
			}
			for (GitRevision rev : revisions.values())
			{
				rev.setColor(colors[uniqueAuthors.indexOf(rev.getAuthor())]);
				info.addRevision(rev);
			}
		}
		return info;
	}

}
