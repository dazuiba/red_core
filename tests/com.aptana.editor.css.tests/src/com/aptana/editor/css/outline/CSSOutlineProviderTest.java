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
package com.aptana.editor.css.outline;

import com.aptana.editor.css.Activator;
import com.aptana.editor.css.parsing.CSSParser;
import com.aptana.editor.css.parsing.CSSScanner;
import com.aptana.editor.css.parsing.ast.CSSRuleNode;
import com.aptana.parsing.ast.IParseNode;

import junit.framework.TestCase;

public class CSSOutlineProviderTest extends TestCase
{

	private CSSOutlineLabelProvider fLabelProvider;
	private CSSOutlineContentProvider fContentProvider;

	private CSSParser fParser;
	private CSSScanner fScanner;

	@Override
	protected void setUp() throws Exception
	{
		fLabelProvider = new CSSOutlineLabelProvider();
		fContentProvider = new CSSOutlineContentProvider();
		fParser = new CSSParser();
		fScanner = new CSSScanner();
	}

	@Override
	protected void tearDown() throws Exception
	{
		fLabelProvider = null;
		fContentProvider = null;
		fParser = null;
		fScanner = null;
	}

	public void testMultipleSelectors() throws Exception
	{
		String source = "textarea.JScript, textarea.HTML {height:10em;}";
		fScanner.setSource(source);
		IParseNode result = (IParseNode) fParser.parse(fScanner);

		Object[] selectors = fContentProvider.getElements(result);
		assertEquals(2, selectors.length);
		CSSRuleNode rule = (CSSRuleNode) result.getChild(0);
		assertEquals(rule.getSelectors()[0], selectors[0]);
		assertEquals(Activator.getImage("icons/selector.png"), fLabelProvider.getImage(selectors[0]));
		assertEquals(rule.getSelectors()[1], selectors[1]);

		Object[] declarations = fContentProvider.getChildren(selectors[0]);
		assertEquals(1, declarations.length);
		assertEquals(rule.getDeclarations()[0], declarations[0]);
		assertEquals(Activator.getImage("icons/declaration.png"), fLabelProvider.getImage(declarations[0]));

		declarations = fContentProvider.getChildren(selectors[1]);
		assertEquals(1, declarations.length);
		assertEquals(rule.getDeclarations()[0], declarations[0]);
	}

	public void testElementAt() throws Exception
	{
		String source = "textarea.JScript, textarea.HTML {height:10em;}";
		fScanner.setSource(source);
		IParseNode result = (IParseNode) fParser.parse(fScanner);

		CSSRuleNode rule = (CSSRuleNode) result.getChild(0);
		assertEquals(rule.getSelectors()[0], CSSOutlineContentProvider.getElementAt(result, 0));
		assertEquals(rule.getSelectors()[0], CSSOutlineContentProvider.getElementAt(result, 10));
		assertEquals(rule.getSelectors()[1], CSSOutlineContentProvider.getElementAt(result, 20));
		assertEquals(rule.getDeclarations()[0], CSSOutlineContentProvider.getElementAt(result, 40));
		assertEquals(rule.getSelectors()[1], CSSOutlineContentProvider.getElementAt(result, source.length() - 1));
	}
}
