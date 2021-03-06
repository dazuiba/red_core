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
package com.aptana.git.ui;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

import com.aptana.core.util.IOUtil;
import com.aptana.core.util.StringUtil;

/**
 * Used to share common code for formatting Diffs for display.
 * 
 * @author cwilliams
 */
public abstract class DiffFormatter
{

	private static final Pattern gitDiffHeaderRegexp = Pattern.compile("@@ \\-([0-9]+),?\\d* \\+(\\d+),?\\d* @@"); //$NON-NLS-1$

	/**
	 * Generates a colored HTML view of the diff
	 * 
	 * @param diff
	 * @return
	 */
	public static String toHTML(String title, String diff)
	{
		if (diff == null)
		{
			return ""; //$NON-NLS-1$
		}
		return injectIntoTemplate(convertDiff(title, diff));
	}

	protected static String convertDiff(String title, String diff)
	{
		if (title == null)
			title = ""; //$NON-NLS-1$
		StringBuilder html = new StringBuilder();
		html.append("<div class=\"file\">"); //$NON-NLS-1$
		html.append("<div class=\"fileHeader\">").append(title).append("</div>"); //$NON-NLS-1$ //$NON-NLS-2$
		html.append("<div class=\"diffContent\">"); //$NON-NLS-1$
		if (!diff.startsWith("diff")) //$NON-NLS-1$
		{
			// New file, no "diff", all lines are added. TODO Maybe split all lines and pretend like they have a + in front?
			if (diff.length() == 0)
			{
				diff = Messages.DiffFormatter_NoContent;
			}
			html.append("<pre>" + StringUtil.sanitizeHTML(diff) + "</pre>"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else
		{
			String[] lines = diff.split("\r\n|\r|\n"); //$NON-NLS-1$
			StringBuilder diffContent = new StringBuilder();
			StringBuilder line1 = new StringBuilder();
			StringBuilder line2 = new StringBuilder();
			String lineNumberNewline = "\n"; //$NON-NLS-1$
			if (Platform.getOS().equals(Platform.OS_WIN32))
			{
				// BUGFIX Have to force breakread/newlines in HTML for IE...
				lineNumberNewline = "<br />\n"; //$NON-NLS-1$
			}
			int hunkStartLine1 = 0;
			int hunkStartLine2 = 0;
			for (int i = 4; i < lines.length; i++)
			{
				String line = lines[i];
				if (line == null || line.length() == 0)
					continue;
				char c = line.charAt(0);
				switch (c)
				{
					case '@':
						Matcher m = gitDiffHeaderRegexp.matcher(line);
						if (m.find())
						{
							hunkStartLine1 = Integer.parseInt(m.group(1)) - 1;
							hunkStartLine2 = Integer.parseInt(m.group(2)) - 1;
						}
						line1.append("..").append(lineNumberNewline); //$NON-NLS-1$
						line2.append("..").append(lineNumberNewline); //$NON-NLS-1$
						diffContent.append("<div class=\"hunkheader\">").append(StringUtil.sanitizeHTML(line)).append( //$NON-NLS-1$
								"</div>"); //$NON-NLS-1$
						break;

					case '+':
						if (line.equals("+++ /dev/null")) //$NON-NLS-1$
							continue;
						// Highlight trailing whitespace
						line = StringUtil.sanitizeHTML(line);
						line = line.replaceFirst("\\s+$", "<span class=\"whitespace\">$0</span>"); //$NON-NLS-1$ //$NON-NLS-2$
						line1.append(lineNumberNewline);
						line2.append(++hunkStartLine2).append(lineNumberNewline);
						diffContent.append("<div class=\"addline\">").append(line).append("</div>"); //$NON-NLS-1$ //$NON-NLS-2$
						break;

					case ' ':
						line1.append(++hunkStartLine1).append(lineNumberNewline);
						line2.append(++hunkStartLine2).append(lineNumberNewline);
						diffContent.append("<div class=\"noopline\">").append(StringUtil.sanitizeHTML(line)).append( //$NON-NLS-1$
								"</div>"); //$NON-NLS-1$
						break;

					case '-':
						line1.append(++hunkStartLine1).append(lineNumberNewline);
						line2.append(lineNumberNewline);
						diffContent.append("<div class=\"delline\">").append(StringUtil.sanitizeHTML(line)) //$NON-NLS-1$
								.append("</div>"); //$NON-NLS-1$
						break;

					default:
						break;
				}

			}
			html.append("<div class=\"lineno\">").append(line1).append("</div>"); //$NON-NLS-1$ //$NON-NLS-2$
			html.append("<div class=\"lineno\">").append(line2).append("</div>"); //$NON-NLS-1$ //$NON-NLS-2$
			html.append("<div class=\"lines\">").append(diffContent).append("</div>"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		html.append("</div>").append("</div>"); //$NON-NLS-1$ //$NON-NLS-2$
		return html.toString();
	}

	protected static String injectIntoTemplate(String html)
	{
		InputStream stream = null;
		try
		{
			stream = FileLocator.openStream(GitUIPlugin.getDefault().getBundle(), new Path("templates") //$NON-NLS-1$
					.append("diff.html"), false); //$NON-NLS-1$
			String template = IOUtil.read(stream);
			Map<String, String> variables = new HashMap<String, String>();
			variables.put("\\{diff\\}", Matcher.quoteReplacement(html)); //$NON-NLS-1$
			return StringUtil.replaceAll(template, variables);
		}
		catch (Exception e)
		{
			GitUIPlugin.logError(e.getMessage(), e);
			return html.toString();
		}
	}

	/**
	 * Pass in a Map of filename/path to diff.
	 * 
	 * @param diffs
	 * @return
	 */
	public static String toHTML(Map<String, String> diffs)
	{
		if (diffs == null)
		{
			return ""; //$NON-NLS-1$
		}
		StringBuilder combined = new StringBuilder();
		for (Map.Entry<String, String> diffMap : diffs.entrySet())
		{
			combined.append(convertDiff(diffMap.getKey(), diffMap.getValue())).append("\n"); //$NON-NLS-1$
		}
		return injectIntoTemplate(combined.toString());
	}

}
