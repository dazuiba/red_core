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

package com.aptana.editor.common.internal.scripting;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import com.aptana.editor.common.scripting.QualifiedContentType;
import com.aptana.scripting.model.BundleManager;

/**
 * This class stores the association between an IDocument in an editor and the filename of teh file it represents. This
 * is used to do dynamic top-level scope associations, since bundles can define filename patterns to override top level
 * scopes returned for scripting purposes.
 * 
 * @author Max Stepanov
 */
/* package */class ExtendedDocumentInfo
{

	private Map<String, String> contentTypesAssociation = new HashMap<String, String>();
	private String filename;
	private String defaultContentType;

	// Cached copy of the top level scope
	private String fTopLevelScope;
	// Boolean flag to tell if we calculated top level scope already. It may have been null, so we can't just check for
	// null on memoized field.
	private boolean calculatedTopLevelScope = false;

	/**
	 * @param filename
	 */
	protected ExtendedDocumentInfo(String defaultContentType, String filename)
	{
		this.defaultContentType = defaultContentType;
		this.filename = filename;
	}

	public QualifiedContentType getContentType(IDocument document, int offset) throws BadLocationException
	{
		QualifiedContentType result = new QualifiedContentType(defaultContentType);
		// get partition at offset
		String contentType = document.getContentType(offset);
		// grab the top level document type that this partition is a subtype of
		String subdocContentType = contentTypesAssociation.get(contentType);
		if (subdocContentType != null && !subdocContentType.equals(result.getLastPart()))
		{
			// if our content type/scope doesn't have this language level scope at the end, add it to the end
			result = result.subtype(subdocContentType);
		}
		// add partition to end
		return result.subtype(contentType);
	}

	/**
	 * Associate the partition with it's implied top level document Content Type/scope
	 * 
	 * @param contentType
	 * @param documentContentType
	 */
	public void associateContentType(String contentType, String documentContentType)
	{
		contentTypesAssociation.put(contentType, documentContentType);
	}

	/**
	 * Takes a fully converted/translated scope and then forcibly replaces the wrapping scope!
	 * 
	 * @param translation
	 * @return
	 */
	public QualifiedContentType modify(QualifiedContentType translation)
	{
		if (filename == null)
		{
			return translation;
		}

		// memoize since it's expensive (we repeatedly ask for scopes with our new coloring!)
		if (!calculatedTopLevelScope)
		{
			fTopLevelScope = BundleManager.getInstance().getTopLevelScope(filename);
			calculatedTopLevelScope = true;
		}

		if (fTopLevelScope == null)
		{
			return translation;
		}

		// TODO If scope ends with generic DEFAULT PARTITION, cut that off

		String[] parts = translation.getParts();
		String[] newParts = new String[parts.length];
		newParts[0] = fTopLevelScope;
		System.arraycopy(parts, 1, newParts, 1, parts.length - 1);
		return new QualifiedContentType(newParts);
	}

}
