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
package com.aptana.editor.common.internal.commands;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This handler activates the Next editor without showing the Editor list popup.
 * 
 * @author schitale
 */
public class NextEditorHandler extends AbstractHandler
{

	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null)
		{
			return null;
		}

		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		if (activePage == null)
		{
			return null;
		}

		switchEditor(activePage, next());

		return null;
	}

	protected boolean next()
	{
		return true;
	}

	private static void switchEditor(IWorkbenchPage activePage, boolean next)
	{
		IEditorPart activeEditor = activePage.getActiveEditor();
		if (activeEditor != null)
		{
			IEditorReference[] editorReferences = activePage.getEditorReferences();
			if (editorReferences != null && editorReferences.length >= 2)
			{
				List<IEditorPart> editorsList = new LinkedList<IEditorPart>();
				for (IEditorReference editorReference : editorReferences)
				{
					IWorkbenchPart editorPart = editorReference.getPart(true);
					if (editorPart instanceof IEditorPart)
					{
						editorsList.add((IEditorPart) editorPart);
					}
				}
				int activeEditorIndex = editorsList.indexOf(activeEditor);
				int toEditorIndex = (activeEditorIndex == -1 ? 0 : (activeEditorIndex + (next ? 1 : -1)));
				if (toEditorIndex < 0)
				{
					toEditorIndex = editorsList.size() - 1;
				}
				else if (toEditorIndex >= editorsList.size())
				{
					toEditorIndex = 0;
				}
				activePage.activate(editorsList.get(toEditorIndex));
			}
		}
	}
}
