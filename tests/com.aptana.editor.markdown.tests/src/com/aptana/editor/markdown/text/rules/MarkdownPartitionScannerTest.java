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
package com.aptana.editor.markdown.text.rules;

import junit.framework.TestCase;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import com.aptana.editor.common.ExtendedFastPartitioner;
import com.aptana.editor.common.NullPartitionerSwitchStrategy;
import com.aptana.editor.common.text.rules.CompositePartitionScanner;
import com.aptana.editor.common.text.rules.NullSubPartitionScanner;
import com.aptana.editor.markdown.MarkdownSourceConfiguration;

public class MarkdownPartitionScannerTest extends TestCase
{

	// TODO Refactor out common code with other language's partition scanner testcases!
	private ExtendedFastPartitioner partitioner;

	private void assertContentType(String contentType, String code, int offset)
	{
		assertEquals("Content type doesn't match expectations for: " + code.charAt(offset), contentType,
				getContentType(code, offset));
	}

	@Override
	protected void tearDown() throws Exception
	{
		partitioner = null;
		super.tearDown();
	}

	private String getContentType(String content, int offset)
	{
		if (partitioner == null)
		{
			IDocument document = new Document(content);
			CompositePartitionScanner partitionScanner = new CompositePartitionScanner(MarkdownSourceConfiguration
					.getDefault().createSubPartitionScanner(), new NullSubPartitionScanner(),
					new NullPartitionerSwitchStrategy());
			partitioner = new ExtendedFastPartitioner(partitionScanner, MarkdownSourceConfiguration.getDefault()
					.getContentTypes());
			partitionScanner.setPartitioner(partitioner);
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return partitioner.getContentType(offset);
	}

	public void testLevel1ATXHeader()
	{
		String source = "#  HTML5 ";

		assertContentType(MarkdownSourceConfiguration.HEADING, source, 0);
		assertContentType(MarkdownSourceConfiguration.HEADING, source, source.length() - 1);
	}

	public void testLevel2ATXHeader()
	{
		String source = "## License:";

		assertContentType(MarkdownSourceConfiguration.HEADING, source, 0);
		assertContentType(MarkdownSourceConfiguration.HEADING, source, source.length() - 1);
	}

	public void testLevel3ATXHeader()
	{
		String source = "### v.0.9.1 : August 13th, 2010";

		assertContentType(MarkdownSourceConfiguration.HEADING, source, 0);
		assertContentType(MarkdownSourceConfiguration.HEADING, source, source.length() - 1);
	}

	public void testLevel5ATXHeader()
	{
		String source = "##### Thanks:";

		assertContentType(MarkdownSourceConfiguration.HEADING, source, 0);
		assertContentType(MarkdownSourceConfiguration.HEADING, source, source.length() - 1);
	}

	public void testAsteriskList()
	{
		String source = "* Modernizr: MIT/BSD license";

		assertContentType(MarkdownSourceConfiguration.UNNUMBERED_LIST, source, 0);
		assertContentType(MarkdownSourceConfiguration.UNNUMBERED_LIST, source, source.length() - 1);
	}

	public void testMultiplePartitions()
	{
		String source = "#  HTML5 Boilerplate [http://html5boilerplate.com](http://html5boilerplate.com)\n" + "\n\n"
				+ "## License:\n" + "\n" + "Major components:\n" + "\n" + "* Modernizr: MIT/BSD license";
		// HTML 5 heading
		assertContentType(MarkdownSourceConfiguration.HEADING, source, 0);
		assertContentType(MarkdownSourceConfiguration.HEADING, source, 78);
		// newline
		assertContentType(MarkdownSourceConfiguration.DEFAULT, source, 80);
		// License heading
		assertContentType(MarkdownSourceConfiguration.HEADING, source, 82);
		assertContentType(MarkdownSourceConfiguration.HEADING, source, 92);
		// newline
		assertContentType(MarkdownSourceConfiguration.DEFAULT, source, 94);
		// Major components
		assertContentType(MarkdownSourceConfiguration.DEFAULT, source, 95);
		assertContentType(MarkdownSourceConfiguration.DEFAULT, source, 111);
		// Modernizr list item
		assertContentType(MarkdownSourceConfiguration.UNNUMBERED_LIST, source, 114);
		assertContentType(MarkdownSourceConfiguration.UNNUMBERED_LIST, source, 141);
	}

	public void testSeparator1()
	{
		String source = "* * *";

		assertContentType(MarkdownSourceConfiguration.SEPARATOR, source, 0);
		assertContentType(MarkdownSourceConfiguration.SEPARATOR, source, source.length() - 1);
	}

	public void testSeparator2()
	{
		String source = "***";

		assertContentType(MarkdownSourceConfiguration.SEPARATOR, source, 0);
		assertContentType(MarkdownSourceConfiguration.SEPARATOR, source, source.length() - 1);
	}

	public void testSeparator3()
	{
		String source = "*****";

		assertContentType(MarkdownSourceConfiguration.SEPARATOR, source, 0);
		assertContentType(MarkdownSourceConfiguration.SEPARATOR, source, source.length() - 1);
	}

	public void testSeparator4()
	{
		String source = "- - -";

		assertContentType(MarkdownSourceConfiguration.SEPARATOR, source, 0);
		assertContentType(MarkdownSourceConfiguration.SEPARATOR, source, source.length() - 1);
	}

	public void testSeparator5()
	{
		String source = "---------------------------------------";

		assertContentType(MarkdownSourceConfiguration.SEPARATOR, source, 0);
		assertContentType(MarkdownSourceConfiguration.SEPARATOR, source, source.length() - 1);
	}

	public void testQuote()
	{
		String source = "> This is the first level of quoting.";

		assertContentType(MarkdownSourceConfiguration.QUOTE, source, 0);
		assertContentType(MarkdownSourceConfiguration.QUOTE, source, source.length() - 1);
	}

	public void testQuoteFollowedByNoSpace()
	{
		String source = ">This is the first level of quoting.";

		assertContentType(MarkdownSourceConfiguration.QUOTE, source, 0);
		assertContentType(MarkdownSourceConfiguration.QUOTE, source, source.length() - 1);
	}

	public void testQuotePrecededByOneSpace()
	{
		String source = " > This is the first level of quoting.";

		assertContentType(MarkdownSourceConfiguration.QUOTE, source, 1);
		assertContentType(MarkdownSourceConfiguration.QUOTE, source, source.length() - 1);
	}

	public void testQuotePrecededByTwoSpaces()
	{
		String source = "  > This is the first level of quoting.";

		assertContentType(MarkdownSourceConfiguration.QUOTE, source, 2);
		assertContentType(MarkdownSourceConfiguration.QUOTE, source, source.length() - 1);
	}

	public void testQuotePrecededByThreeSpaces()
	{
		String source = "   > This is the first level of quoting.";

		assertContentType(MarkdownSourceConfiguration.QUOTE, source, 3);
		assertContentType(MarkdownSourceConfiguration.QUOTE, source, source.length() - 1);
	}

	public void testQuoteHardWrapped()
	{
		String source = "> This is a blockquote with two paragraphs. Lorem ipsum dolor sit amet,\n"
				+ "consectetuer adipiscing elit. Aliquam hendrerit mi posuere lectus.\n"
				+ "Vestibulum enim wisi, viverra nec, fringilla in, laoreet vitae, risus.";

		assertContentType(MarkdownSourceConfiguration.QUOTE, source, 1);
		assertContentType(MarkdownSourceConfiguration.QUOTE, source, 74);
		assertContentType(MarkdownSourceConfiguration.QUOTE, source, 138);
		assertContentType(MarkdownSourceConfiguration.QUOTE, source, 160);
		assertContentType(MarkdownSourceConfiguration.QUOTE, source, source.length() - 1);
	}

	public void testCodeBlockWithTab()
	{
		String source = "\tBlock of code";

		assertContentType(MarkdownSourceConfiguration.BLOCK, source, 0);
		assertContentType(MarkdownSourceConfiguration.BLOCK, source, 1);
		assertContentType(MarkdownSourceConfiguration.BLOCK, source, source.length() - 1);
	}

	public void testCodeBlockWith4Spaces()
	{
		String source = "    Block of code";

		assertContentType(MarkdownSourceConfiguration.BLOCK, source, 0);
		assertContentType(MarkdownSourceConfiguration.BLOCK, source, 1);
		assertContentType(MarkdownSourceConfiguration.BLOCK, source, 3);
		assertContentType(MarkdownSourceConfiguration.BLOCK, source, source.length() - 1);
	}
	
	public void testNumberedList()
	{
		String source = "1. First item";

		assertContentType(MarkdownSourceConfiguration.NUMBERED_LIST, source, 0);
		assertContentType(MarkdownSourceConfiguration.NUMBERED_LIST, source, 1);
		assertContentType(MarkdownSourceConfiguration.NUMBERED_LIST, source, 3);
		assertContentType(MarkdownSourceConfiguration.NUMBERED_LIST, source, source.length() - 1);
	}
}
