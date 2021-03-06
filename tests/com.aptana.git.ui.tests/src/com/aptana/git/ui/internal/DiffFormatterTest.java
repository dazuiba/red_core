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
package com.aptana.git.ui.internal;

import com.aptana.git.ui.DiffFormatter;

import junit.framework.TestCase;

public class DiffFormatterTest extends TestCase
{
	
	public void testThing()
	{
		String rawDiff = "diff --git a/bundle.rb b/bundle.rb\nindex ce16112..a67a29f 100644\n--- a/bundle.rb\n+++ b/bundle.rb\n@@ -1,4 +1,4 @@\n-require 'ruble'\n+require 'ruble' # BLAH!\n \n # its ruby, so this just addscommands/snippets in bundle (or replaces those with same name)\n # many ruby files could add to a single bundle";
		String expected = "<html>\n  <head>\n    <style>\nbody {\n\tmargin: 0;\n\tmargin-top: 5px;\n\tfont-family: 'Lucida Grande', Arial;\n\tfont-size: 12px;\n}\n\n.diff .file .fileHeader {\n\tmargin-top: -5px;\n\tpadding-bottom: 1px;\n\tfont-weight: bold;\n\tclear: left;\n}\n\n.diff .file .diffContent {\n\twhite-space: pre;\n\tfont-family: Monaco;\n}\n\n.diff .file .diffcontent .lineno {\n\tfloat: left;\n\tpadding-left: 2px;\n\tpadding-right: 2px;\n\tbackground-color: #ECECEC;\n\tcolor: #A9A9A9;\n\tborder: 1px solid #DDDDDD;\n\ttext-align: right;\n}\n\n.diff .file .diffContent .lines {\n\toverflow: visible;\n}\n\n.diff .file .diffContent .lines .hunkheader {\n  background-color: #f7f7f7;\n  color: #bbb;\n}\n\n.diff .file .diffcontent .lines .delline {\n  background-color: #FEE;\n  color: #B00;\n}\n\n.diff .file .diffcontent .lines .addline {\n  background-color: #DFD;\n  color: #080;\n}\n\n.diff .file .diffcontent .lines .whitespace {\n\tbackground-color: rgba(255,0,0,0.5);\n}\n\n#CurrentHunk {\n\tborder-left: 5px solid black;\n}\n    </style>\n  </head>\n  <body>\n    <div class=\"diff\">\n      <div class=\"file\"><div class=\"fileHeader\"></div><div class=\"diffContent\"><div class=\"lineno\">..\n1\n\n2\n3\n4\n</div><div class=\"lineno\">..\n\n1\n2\n3\n4\n</div><div class=\"lines\"><div class=\"hunkheader\">@@ -1,4 +1,4 @@</div><div class=\"delline\">-require 'ruble'</div><div class=\"addline\">+require 'ruble' # BLAH!</div><div class=\"noopline\"> </div><div class=\"noopline\"> # its ruby, so this just addscommands/snippets in bundle (or replaces those with same name)</div><div class=\"noopline\"> # many ruby files could add to a single bundle</div></div></div></div>\n    </div>\n  </body>\n</html>";
		assertEquals(expected, DiffFormatter.toHTML("", rawDiff));
	}
	
	public void testBackslashInTitle()
	{
		String rawDiff = "diff --git a/bundle.rb b/bundle.rb\nindex ce16112..a67a29f 100644\n--- a/bundle.rb\n+++ b/bundle.rb\n@@ -1,4 +1,4 @@\n-require 'ruble'\n+require 'ruble' # BLAH!\n \n # its ruby, so this just addscommands/snippets in bundle (or replaces those with same name)\n # many ruby files could add to a single bundle";
		String expected = "<html>\n  <head>\n    <style>\nbody {\n\tmargin: 0;\n\tmargin-top: 5px;\n\tfont-family: 'Lucida Grande', Arial;\n\tfont-size: 12px;\n}\n\n.diff .file .fileHeader {\n\tmargin-top: -5px;\n\tpadding-bottom: 1px;\n\tfont-weight: bold;\n\tclear: left;\n}\n\n.diff .file .diffContent {\n\twhite-space: pre;\n\tfont-family: Monaco;\n}\n\n.diff .file .diffcontent .lineno {\n\tfloat: left;\n\tpadding-left: 2px;\n\tpadding-right: 2px;\n\tbackground-color: #ECECEC;\n\tcolor: #A9A9A9;\n\tborder: 1px solid #DDDDDD;\n\ttext-align: right;\n}\n\n.diff .file .diffContent .lines {\n\toverflow: visible;\n}\n\n.diff .file .diffContent .lines .hunkheader {\n  background-color: #f7f7f7;\n  color: #bbb;\n}\n\n.diff .file .diffcontent .lines .delline {\n  background-color: #FEE;\n  color: #B00;\n}\n\n.diff .file .diffcontent .lines .addline {\n  background-color: #DFD;\n  color: #080;\n}\n\n.diff .file .diffcontent .lines .whitespace {\n\tbackground-color: rgba(255,0,0,0.5);\n}\n\n#CurrentHunk {\n\tborder-left: 5px solid black;\n}\n    </style>\n  </head>\n  <body>\n    <div class=\"diff\">\n      <div class=\"file\"><div class=\"fileHeader\">\\chris\\bundle.rb</div><div class=\"diffContent\"><div class=\"lineno\">..\n1\n\n2\n3\n4\n</div><div class=\"lineno\">..\n\n1\n2\n3\n4\n</div><div class=\"lines\"><div class=\"hunkheader\">@@ -1,4 +1,4 @@</div><div class=\"delline\">-require 'ruble'</div><div class=\"addline\">+require 'ruble' # BLAH!</div><div class=\"noopline\"> </div><div class=\"noopline\"> # its ruby, so this just addscommands/snippets in bundle (or replaces those with same name)</div><div class=\"noopline\"> # many ruby files could add to a single bundle</div></div></div></div>\n    </div>\n  </body>\n</html>";
		assertEquals(expected, DiffFormatter.toHTML("\\chris\\bundle.rb", rawDiff));
	}

}
