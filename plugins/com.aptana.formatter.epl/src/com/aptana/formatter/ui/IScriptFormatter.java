/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package com.aptana.formatter.ui;

import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

/**
 * Script source code formatter interface.
 */
public interface IScriptFormatter
{

	/**
	 * Detects the indentation level at the specified offset
	 * 
	 * @param document
	 * @param offset
	 * @return
	 */
	int detectIndentationLevel(IDocument document, int offset);

	/**
	 * Format <code>source</code>, and returns a text edit that correspond to the difference between the given string
	 * and the formatted string.
	 * <p>
	 * It returns null if the given string cannot be formatted.
	 * </p>
	 * 
	 * @param source
	 *            full source module content
	 * @param offset
	 *            the offset of the region to format
	 * @param length
	 *            the length of the region to format
	 * @param indentationLevel
	 *            the additional indent level
	 */
	TextEdit format(String source, int offset, int length, int indentationLevel) throws FormatterException;

	/**
	 * Set a flag on this formatter to indicate that it will be running as a slave formatter by the multi-pass
	 * formatter.
	 * 
	 * @param isSlave
	 * @see #isSlave()
	 */
	void setIsSlave(boolean isSlave);

	/**
	 * Returns true if this formatter is running as a 'slave' formatter.
	 * 
	 * @return boolean
	 * @see #setIsSlave(boolean)
	 */
	boolean isSlave();

	/**
	 * Returns the indentation size, as defined in the formatter preferences.
	 * 
	 * @return The indentation size.
	 */
	int getIndentSize();

	/**
	 * Returns the tab size, as defined in the formatter preferences.
	 * 
	 * @return The tab size.
	 */
	int getTabSize();

	/**
	 * Returns the indentation type, as defined in the formatter preferences.
	 * 
	 * @return The indentation type - One of {@link CodeFormatterConstants#TAB}, {@link CodeFormatterConstants#SPACE}
	 *         or {@link CodeFormatterConstants#MIXED}
	 */
	String getIndentType();
}