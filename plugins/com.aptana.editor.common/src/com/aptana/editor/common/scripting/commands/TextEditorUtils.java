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
package com.aptana.editor.common.scripting.commands;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * This holds the TextEditor related utilities.
 * 
 * @author schitale
 */
public abstract class TextEditorUtils
{

	/**
	 * Tries to get the accurate location of the caret relative to the document.
	 * <p>
	 * It tries to use the caret position in the StyledText and the selection to determine if the non-zero length
	 * selection is forward (LtoR) or backward (RtoL) and uses that info to compute the location.
	 * 
	 * @param textEditor
	 * @return -1 if editor is null or there is no selection. Otherwise uses caret offset from selection.
	 */
	public static int getCaretOffset(ITextEditor textEditor)
	{
		if (textEditor == null)
			return -1;
		// Assume forward (LtoR) selection
		boolean forwardSelection = true;
		Object adapter = textEditor.getAdapter(Control.class);
		if (adapter instanceof StyledText)
		{
			// Accurate
			StyledText styledText = (StyledText) adapter;
			int caretOffset = styledText.getCaretOffset();
			Point selection = styledText.getSelection();
			forwardSelection = (caretOffset == selection.y);
		}
		ISelection selection = textEditor.getSelectionProvider().getSelection();
		if (selection instanceof ITextSelection)
		{
			ITextSelection textSelection = (ITextSelection) selection;
			// Inaccurate. This can happen when the selection is
			// reverse i.e. from higher offset to lower offset
			if (forwardSelection)
			{
				return textSelection.getOffset() + textSelection.getLength();
			}
			return textSelection.getOffset();
		}
		return -1;
	}
}
