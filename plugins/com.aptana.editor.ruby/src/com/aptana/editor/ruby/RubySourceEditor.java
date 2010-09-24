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
package com.aptana.editor.ruby;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;

import com.aptana.editor.common.AbstractThemeableEditor;
import com.aptana.editor.common.CommonEditorPlugin;
import com.aptana.editor.common.outline.CommonOutlineItem;
import com.aptana.editor.common.outline.CommonOutlinePage;
import com.aptana.editor.common.parsing.FileService;
import com.aptana.editor.ruby.core.IImportContainer;
import com.aptana.editor.ruby.outline.RubyOutlineContentProvider;
import com.aptana.editor.ruby.outline.RubyOutlineLabelProvider;
import com.aptana.editor.ruby.parsing.IRubyParserConstants;
import com.aptana.parsing.ast.IParseNode;
import com.aptana.parsing.lexer.IRange;

@SuppressWarnings("restriction")
public class RubySourceEditor extends AbstractThemeableEditor
{

	private static final char[] PAIR_MATCHING_CHARS = new char[] { '(', ')', '{', '}', '[', ']', '`', '`', '\'', '\'',
			'"', '"', '|', '|', '\u201C', '\u201D', '\u2018', '\u2019' }; // curly double quotes, curly single quotes

	@Override
	protected void initializeEditor()
	{
		super.initializeEditor();

		setPreferenceStore(new ChainedPreferenceStore(new IPreferenceStore[] {
				RubyEditorPlugin.getDefault().getPreferenceStore(), CommonEditorPlugin.getDefault().getPreferenceStore(),
				EditorsPlugin.getDefault().getPreferenceStore() }));

		setSourceViewerConfiguration(new RubySourceViewerConfiguration(getPreferenceStore(), this));
		setDocumentProvider(new RubyDocumentProvider());
	}

	@Override
	protected FileService createFileService()
	{
		return new FileService(IRubyParserConstants.LANGUAGE);
	}
	
	protected char[] getPairMatchingCharacters()
	{
		return PAIR_MATCHING_CHARS;
	}

	@Override
	protected CommonOutlinePage createOutlinePage()
	{
		CommonOutlinePage outline = super.createOutlinePage();
		outline.setContentProvider(new RubyOutlineContentProvider());
		outline.setLabelProvider(new RubyOutlineLabelProvider());

		return outline;
	}

	@Override
	protected void setSelectedElement(IRange element)
	{
		if (element instanceof CommonOutlineItem)
		{
			IParseNode node = ((CommonOutlineItem) element).getReferenceNode();
			if (node instanceof IImportContainer) {
				// just sets the highlight range and moves the cursor
				setHighlightRange(element.getStartingOffset(), element.getLength(), true);
				return;
			}
		}
		super.setSelectedElement(element);
	}
}
