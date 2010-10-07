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
package com.aptana.editor.html;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;

import com.aptana.editor.common.tests.AbstractTokenScannerTestCase;

public class HTMLTagScannerTest extends AbstractTokenScannerTestCase
{

	@Override
	protected ITokenScanner createTokenScanner()
	{
		return new HTMLTagScanner()
		{
			protected IToken createToken(String string)
			{
				return HTMLTagScannerTest.this.getToken(string);
			};
		};
	}

	public void testBasicTokenizing()
	{
		String src = "<html id=\"chris\" class=\"cool\" height=\"100\">";
		IDocument document = new Document(src);
		scanner.setRange(document, 0, src.length());

		assertToken(getToken("punctuation.definition.tag.begin.html"), 0, 1);
		assertToken(getToken("entity.name.tag.structure.any.html"), 1, 4);
		assertToken(Token.WHITESPACE, 5, 1);
		assertToken(getToken("entity.other.attribute-name.id.html"), 6, 2);
		assertToken(getToken("punctuation.separator.key-value.html"), 8, 1);
		assertToken(getToken("string.quoted.double.html"), 9, 7);
		assertToken(Token.WHITESPACE, 16, 1);
		assertToken(getToken("entity.other.attribute-name.class.html"), 17, 5);
		assertToken(getToken("punctuation.separator.key-value.html"), 22, 1);
		assertToken(getToken("string.quoted.double.html"), 23, 6);
		assertToken(Token.WHITESPACE, 29, 1);
		assertToken(getToken("entity.other.attribute-name.html"), 30, 6);
		assertToken(getToken("punctuation.separator.key-value.html"), 36, 1);
		assertToken(getToken("string.quoted.double.html"), 37, 5);
		assertToken(getToken("punctuation.definition.tag.end.html"), 42, 1);
	}

	public void testMultiLineSingleQuoteString()
	{
		String src = "<html attribute='\nchris'>";
		IDocument document = new Document(src);
		scanner.setRange(document, 0, src.length());

		assertToken(getToken("punctuation.definition.tag.begin.html"), 0, 1);
		assertToken(getToken("entity.name.tag.structure.any.html"), 1, 4);
		assertToken(Token.WHITESPACE, 5, 1);
		assertToken(getToken("entity.other.attribute-name.html"), 6, 9);
		assertToken(getToken("punctuation.separator.key-value.html"), 15, 1);
		assertToken(getToken("string.quoted.single.html"), 16, 8);
		assertToken(getToken("punctuation.definition.tag.end.html"), 24, 1);
	}

	public void testMultiLineDoubleQuoteString()
	{
		String src = "<html attribute=\"\nchris\">";
		IDocument document = new Document(src);
		scanner.setRange(document, 0, src.length());

		assertToken(getToken("punctuation.definition.tag.begin.html"), 0, 1);
		assertToken(getToken("entity.name.tag.structure.any.html"), 1, 4);
		assertToken(Token.WHITESPACE, 5, 1);
		assertToken(getToken("entity.other.attribute-name.html"), 6, 9);
		assertToken(getToken("punctuation.separator.key-value.html"), 15, 1);
		assertToken(getToken("string.quoted.double.html"), 16, 8);
		assertToken(getToken("punctuation.definition.tag.end.html"), 24, 1);
	}
}
