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
package com.aptana.editor.css;

import java.io.InputStream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.test.performance.PerformanceTestCase;

import com.aptana.core.util.IOUtil;
import com.aptana.editor.css.parsing.lexer.CSSTokenType;

public class CSSCodeScannerPerformanceTest extends PerformanceTestCase
{

	private CSSCodeScanner fScanner;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		fScanner = new CSSCodeScanner()
		{
			@Override
			protected IToken createToken(CSSTokenType string)
			{
				return new Token(string.getScope());
			}
		};
	}

	@Override
	protected void tearDown() throws Exception
	{
		fScanner = null;
		super.tearDown();
	}

	public void testScanningWordpressAdminCSS() throws Exception
	{
		InputStream stream = FileLocator.openStream(Platform.getBundle("com.aptana.editor.css.tests"),
				Path.fromPortableString("performance/wp-admin.css"), false);
		String src = IOUtil.read(stream);
		IDocument document = new Document(src);

		for (int i = 0; i < 80; i++)
		{
			startMeasuring();
			fScanner.setRange(document, 0, src.length());
			while (fScanner.nextToken() != Token.EOF)
			{
				fScanner.getTokenOffset();
				fScanner.getTokenLength();
			}
			stopMeasuring();
		}
		commitMeasurements();
		assertPerformance();
	}
}
