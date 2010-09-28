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
package com.aptana.editor.svg;

import org.eclipse.jface.preference.IPreferenceStore;

import com.aptana.editor.xml.XMLEditor;

public class SVGSourceEditor extends XMLEditor {

<<<<<<< HEAD:plugins/com.aptana.editor.svg/src/com/aptana/editor/svg/SVGSourceEditor.java
    @Override
    protected void initializeEditor() {
        super.initializeEditor();

        setSourceViewerConfiguration(new SVGSourceViewerConfiguration(getPreferenceStore(), this));
        setDocumentProvider(new SVGDocumentProvider());
    }

	@Override
	protected IPreferenceStore getOutlinePreferenceStore()
=======
	/**
	 * CharacterMapRule
	 */
	public CharacterMapRule()
	{
		characterTokenMap = new HashMap<Character, IToken>();
	}

	/**
	 * add
	 * 
	 * @param c
	 * @param token
	 */
	public void add(char c, IToken token)
	{
		characterTokenMap.put(c, token);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner,
	 * boolean)
	 */
	public IToken evaluate(ICharacterScanner scanner, boolean resume)
	{
		successToken = characterTokenMap.get((char) scanner.read());

		if (successToken == null)
		{
			scanner.unread();
			successToken = Token.UNDEFINED;
		}

		return successToken;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
	 */
	public IToken getSuccessToken()
	{
		return successToken;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner)
>>>>>>> development:plugins/com.aptana.editor.js/src/com/aptana/editor/js/text/rules/CharacterMapRule.java
	{
		return SVGEditorPlugin.getDefault().getPreferenceStore();
	}
}
