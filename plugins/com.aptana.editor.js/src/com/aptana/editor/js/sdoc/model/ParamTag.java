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
package com.aptana.editor.js.sdoc.model;

import java.util.List;

import com.aptana.core.util.StringUtil;
import com.aptana.parsing.io.SourcePrinter;

public class ParamTag extends TagWithTypes
{
	private Parameter _parameter;

	/**
	 * ParamTag
	 */
	public ParamTag(Parameter parameter, List<Type> types, String text)
	{
		super(TagType.PARAM, types, text);

		this._parameter = parameter;
	}

	/**
	 * getName
	 * 
	 * @return
	 */
	public String getName()
	{
		String result = ""; //$NON-NLS-1$
		
		if (this._parameter != null)
		{
			result = this._parameter.getName();
		}
		
		return result;
	}

	/**
	 * getUsage
	 * 
	 * @return
	 */
	public Usage getUsage()
	{
		Usage result = Usage.UNKNOWN;
		
		if (this._parameter != null)
		{
			result = this._parameter.getUsage();
		}
		
		return result;
	}

	/**
	 * toSource
	 * 
	 * @param writer
	 */
	public void toSource(SourcePrinter writer)
	{
		writer.print(this.getType().toString());

		boolean first = true;
		writer.print(" {"); //$NON-NLS-1$
		for (Type type : this.getTypes())
		{
			if (first == false)
			{
				writer.print(","); //$NON-NLS-1$
			}
			else
			{
				first = false;
			}

			type.toSource(writer);
		}
		writer.print("} "); //$NON-NLS-1$

		if (this._parameter != null)
		{
			this._parameter.toSource(writer);
		}

		String text = this.getText();

		if (text != null && StringUtil.isEmpty(text) == false)
		{
			writer.println();
			writer.printIndent().print("    ").print(text); //$NON-NLS-1$
		}
	}
}
