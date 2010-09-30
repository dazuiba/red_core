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
package com.aptana.editor.html.formatter.preferences;

import java.net.URL;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.aptana.editor.html.formatter.HTMLFormatterConstants;
import com.aptana.formatter.ui.FormatterModifyTabPage;
import com.aptana.formatter.ui.IFormatterControlManager;
import com.aptana.formatter.ui.IFormatterModifyDialog;
import com.aptana.ui.util.SWTFactory;

/**
 * A formatter comments page for HTML.
 * 
 * @author Shalom Gibly <sgibly@aptana.com>
 */
public class HTMLFormatterCommentsPage extends FormatterModifyTabPage
{

	/**
	 * @param dialog
	 */
	public HTMLFormatterCommentsPage(IFormatterModifyDialog dialog)
	{
		super(dialog);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.aptana.formatter.ui.FormatterModifyTabPage#createOptions(com.aptana.formatter.ui.IFormatterControlManager,
	 * org.eclipse.swt.widgets.Composite)
	 */
	protected void createOptions(IFormatterControlManager manager, Composite parent)
	{
		Group commentWrappingGroup = SWTFactory.createGroup(parent,
				Messages.HTMLFormatterCommentsPage_formattingGroupLabel, 2, 1, GridData.FILL_HORIZONTAL);
		manager.createCheckbox(commentWrappingGroup, HTMLFormatterConstants.WRAP_COMMENTS,
				Messages.HTMLFormatterCommentsPage_enableWrapping, 2);
		manager.createNumber(commentWrappingGroup, HTMLFormatterConstants.WRAP_COMMENTS_LENGTH,
				Messages.HTMLFormatterCommentsPage_maxLineWidth);

	}

	protected URL getPreviewContent()
	{
		return getClass().getResource("wrapping-preview.html"); //$NON-NLS-1$
	}
}