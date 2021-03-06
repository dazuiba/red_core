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
package com.aptana.git.ui.actions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.browser.BrowserViewer;
import org.eclipse.ui.internal.browser.WebBrowserView;
import org.eclipse.ui.part.WorkbenchPart;

import com.aptana.git.core.model.GitRepository;
import com.aptana.git.ui.GitUIPlugin;
import com.aptana.git.ui.actions.GitAction;

@SuppressWarnings({ "restriction", "rawtypes" })
public class GithubNetworkAction extends GitAction
{
	@Override
	public void run()
	{
		final String networkURL = getGithubURL();
		try
		{
			// Use View
			final String browserViewerFieldName = "viewer"; //$NON-NLS-1$
			final Class klass = WebBrowserView.class;
			final IViewPart obj = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
					WebBrowserView.WEB_BROWSER_VIEW_ID, "-0", IWorkbenchPage.VIEW_VISIBLE); //$NON-NLS-1$

			try
			{
				Method m = WorkbenchPart.class.getDeclaredMethod("setPartName", String.class); //$NON-NLS-1$
				m.setAccessible(true);
				m.invoke(obj, "Github Network Viewer");
			}
			catch (Exception e)
			{
				GitUIPlugin.logError(e.getMessage(), e);
			}

			// Use Editor
			// final String browserViewerFieldName = "webBrowser";
			// final Class klass = WebBrowserEditor.class;
			// final Object obj = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(
			// new WebBrowserEditorInput(new URL(networkURL)), WebBrowserEditor.WEB_BROWSER_EDITOR_ID);

			try
			{
				Field f = klass.getDeclaredField(browserViewerFieldName);
				f.setAccessible(true);
				final BrowserViewer viewer = (BrowserViewer) f.get(obj);
				viewer.getBrowser().addProgressListener(new ProgressListener()
				{

					public void completed(ProgressEvent event)
					{
						String js = "var elements = new Array();\n" //$NON-NLS-1$
								+ "var ids = new Array('header', 'repo_menu', 'repo_sub_menu', 'repos', 'footer', 'triangle');\n" //$NON-NLS-1$
								+ "elements.push(document.getElementById('network').children.item(0));\n" //$NON-NLS-1$
								+ "elements.push(document.getElementById('network').children.item(1));\n" //$NON-NLS-1$
								+ "elements.push(document.getElementById('network').children.item(2));\n" //$NON-NLS-1$
								+ "for(i=0; i<ids.length; i++) {\n" //$NON-NLS-1$
								+ "  elements.push(document.getElementById(ids[i]));\n" + "}\n" //$NON-NLS-1$ //$NON-NLS-2$
								+ "for(i=0; i<elements.length; i++) {\n" + "  elements[i].style.display = 'none';\n" //$NON-NLS-1$ //$NON-NLS-2$
								+ "}\n"; //$NON-NLS-1$

						viewer.getBrowser().execute(js);
					}

					public void changed(ProgressEvent event)
					{
					}
				});
				viewer.setURL(networkURL);
			}
			catch (Exception e)
			{
				GitUIPlugin.logError(e.getMessage(), e);
			}
		}
		catch (CoreException e)
		{
			GitUIPlugin.logError(e);
		}
	}

	private String getGithubURL()
	{
		final GitRepository repo = getSelectedRepository();
		// Check the remote urls for github and use that to determine URL we need!
		Set<String> remoteURLs = repo.remoteURLs();
		for (String remoteURL : remoteURLs)
		{
			if (!remoteURL.contains("github.com")) //$NON-NLS-1$
				continue;
			String remaining = remoteURL.substring(remoteURL.indexOf("github.com") + 10); //$NON-NLS-1$
			if (remaining.startsWith("/") || remaining.startsWith(":")) //$NON-NLS-1$ //$NON-NLS-2$
				remaining = remaining.substring(1);
			if (remaining.endsWith(".git")) //$NON-NLS-1$
				remaining = remaining.substring(0, remaining.length() - 4);
			int split = remaining.indexOf("/"); //$NON-NLS-1$
			String userName = remaining.substring(0, split);
			String repoName = remaining.substring(split + 1);
			return MessageFormat.format("http://github.com/{0}/{1}/network", userName, repoName); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public boolean isEnabled()
	{
		return getGithubURL() != null;
	}
}
