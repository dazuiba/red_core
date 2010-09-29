/**
 * This file Copyright (c) 2005-2010 Aptana, Inc. This program is
 * dual-licensed under both the Aptana Public License and the GNU General
 * Public license. You may elect to use one or the other of these licenses.
 * 
 * This program is distributed in the hope that it will be useful, but
 * AS-IS and WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, TITLE, or
 * NONINFRINGEMENT. Redistribution, except as permitted by whichever of
 * the GPL or APL you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or modify this
 * program under the terms of the GNU General Public License,
 * Version 3, as published by the Free Software Foundation.  You should
 * have received a copy of the GNU General Public License, Version 3 along
 * with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Aptana provides a special exception to allow redistribution of this file
 * with certain other free and open source software ("FOSS") code and certain additional terms
 * pursuant to Section 7 of the GPL. You may view the exception and these
 * terms on the web at http://www.aptana.com/legal/gpl/.
 * 
 * 2. For the Aptana Public License (APL), this program and the
 * accompanying materials are made available under the terms of the APL
 * v1.0 which accompanies this distribution, and is available at
 * http://www.aptana.com/legal/apl/.
 * 
 * You may view the GPL, Aptana's exception and additional terms, and the
 * APL in the file titled license.html at the root of the corresponding
 * plugin containing this source file.
 * 
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.git.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;

import com.aptana.git.core.model.AbstractGitRepositoryListener;
import com.aptana.git.core.model.BranchChangedEvent;
import com.aptana.git.core.model.GitRepository;
import com.aptana.git.core.model.IGitRepositoriesListener;
import com.aptana.git.core.model.IGitRepositoryManager;
import com.aptana.git.core.model.IndexChangedEvent;
import com.aptana.git.core.model.PullEvent;
import com.aptana.git.core.model.RepositoryAddedEvent;
import com.aptana.git.core.model.RepositoryRemovedEvent;

/**
 * Listens to repository changes and forces the relevant resources in the workspace to refresh.
 * 
 * @author cwilliams
 */
class GitProjectRefresher extends AbstractGitRepositoryListener implements IGitRepositoriesListener
{

	public void branchChanged(BranchChangedEvent e)
	{
		refreshAffectedProjects(e.getRepository());
	}

	@Override
	public void pulled(PullEvent e)
	{
		refreshAffectedProjects(e.getRepository());
	}

	public void indexChanged(IndexChangedEvent e)
	{
		// We get a list of the files whose status just changed. We need to refresh those and any
		// parents/ancestors of those.
		refreshResources(e.getFilesWithChanges(), IResource.DEPTH_ZERO);
	}

	protected void refreshAffectedProjects(GitRepository repo)
	{
		final Set<IProject> affectedProjects = new HashSet<IProject>();
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
		{
			GitRepository other = getRepositoryManager().getAttached(project);
			if (other != null && other.equals(repo))
				affectedProjects.add(project);
		}

		refreshResources(affectedProjects, IResource.DEPTH_INFINITE);
	}

	protected IGitRepositoryManager getRepositoryManager()
	{
		return GitPlugin.getDefault().getGitRepositoryManager();
	}

	private void refreshResources(final Collection<? extends IResource> resources, final int depth)
	{
		if (resources == null || resources.isEmpty())
			return;

		WorkspaceJob job = new WorkspaceJob("Refresh projects") //$NON-NLS-1$
		{
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException
			{
				int work = 100 * resources.size();
				SubMonitor sub = SubMonitor.convert(monitor, work);
				for (IResource resource : resources)
				{
					if (sub.isCanceled())
						return Status.CANCEL_STATUS;
					resource.refreshLocal(depth, sub.newChild(100));
				}
				sub.done();
				return Status.OK_STATUS;
			}
		};
		job.setRule(ResourcesPlugin.getWorkspace().getRoot());
		job.setUser(false);
		job.setPriority(Job.LONG);
		job.schedule();
	}

	public void repositoryAdded(RepositoryAddedEvent e)
	{
		e.getRepository().addListener(this);
	}

	public void repositoryRemoved(RepositoryRemovedEvent e)
	{
		e.getRepository().removeListener(this);
	}

}
