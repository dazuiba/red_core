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

package com.aptana.editor.svg;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;

import com.aptana.editor.common.CommonEditorPlugin;
import com.aptana.editor.common.IPartitioningConfiguration;
import com.aptana.editor.common.ISourceViewerConfiguration;
import com.aptana.editor.common.scripting.IContentTypeTranslator;
import com.aptana.editor.common.scripting.QualifiedContentType;
import com.aptana.editor.common.text.rules.ISubPartitionScanner;
import com.aptana.editor.common.text.rules.NonRuleBasedDamagerRepairer;
import com.aptana.editor.common.text.rules.SubPartitionScanner;
import com.aptana.editor.common.text.rules.TagRule;
import com.aptana.editor.common.text.rules.ThemeingDamagerRepairer;
import com.aptana.editor.xml.IXMLConstants;
import com.aptana.editor.xml.XMLScanner;
import com.aptana.editor.xml.XMLSourceConfiguration;
import com.aptana.editor.xml.XMLTagScanner;

/**
 * @author Max Stepanov
 */
public class SVGSourceConfiguration implements IPartitioningConfiguration, ISourceViewerConfiguration
{

	public final static String PREFIX = "__svg_"; //$NON-NLS-1$
	public final static String DEFAULT = "__svg" + IDocument.DEFAULT_CONTENT_TYPE; //$NON-NLS-1$
	public final static String XML_COMMENT = PREFIX + "comment"; //$NON-NLS-1$
	public final static String CDATA = PREFIX + "cdata"; //$NON-NLS-1$
	public final static String PRE_PROCESSOR = PREFIX + "pre_processor"; //$NON-NLS-1$
	public final static String XML_TAG = PREFIX + "tag"; //$NON-NLS-1$

	public static final String[] CONTENT_TYPES = new String[] { DEFAULT, XML_COMMENT, CDATA, PRE_PROCESSOR, XML_TAG };

	private static final String[][] TOP_CONTENT_TYPES = new String[][] { { ISVGConstants.CONTENT_TYPE_SVG } };

	private IPredicateRule[] partitioningRules = new IPredicateRule[] {
			new MultiLineRule("<?", "?>", new Token(PRE_PROCESSOR)), //$NON-NLS-1$ //$NON-NLS-2$
			new MultiLineRule("<!--", "-->", new Token(XML_COMMENT), (char) 0, true), //$NON-NLS-1$ //$NON-NLS-2$
			new MultiLineRule("<![CDATA[", "]]>", new Token(CDATA)), //$NON-NLS-1$ //$NON-NLS-2$
			new TagRule("/", new Token(XML_TAG)), //$NON-NLS-1$
			new TagRule(new Token(XML_TAG)) };

	private XMLScanner xmlScanner;
	private RuleBasedScanner cdataScanner;
	private RuleBasedScanner preProcessorScanner;
	private XMLTagScanner xmlTagScanner;

	private static SVGSourceConfiguration instance;

	public static SVGSourceConfiguration getDefault()
	{
		if (instance == null)
		{
			IContentTypeTranslator c = CommonEditorPlugin.getDefault().getContentTypeTranslator();
			c.addTranslation(new QualifiedContentType(ISVGConstants.CONTENT_TYPE_SVG), new QualifiedContentType(
					"text.xml")); //$NON-NLS-1$
			instance = new SVGSourceConfiguration();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.IPartitioningConfiguration#getContentTypes()
	 */
	public String[] getContentTypes()
	{
		return CONTENT_TYPES;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.ITopContentTypesProvider#getTopContentTypes()
	 */
	public String[][] getTopContentTypes()
	{
		return TOP_CONTENT_TYPES;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.IPartitioningConfiguration#getPartitioningRules()
	 */
	public IPredicateRule[] getPartitioningRules()
	{
		return partitioningRules;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.IPartitioningConfiguration#createSubPartitionScanner()
	 */
	public ISubPartitionScanner createSubPartitionScanner()
	{
		return new SubPartitionScanner(partitioningRules, CONTENT_TYPES, new Token(DEFAULT));
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.IPartitioningConfiguration#getDocumentDefaultContentType()
	 */
	public String getDocumentContentType(String contentType)
	{
		if (contentType.startsWith(PREFIX))
		{
			return IXMLConstants.CONTENT_TYPE_XML;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.ISourceViewerConfiguration#setupPresentationReconciler(org.eclipse.jface.text
	 * .presentation.PresentationReconciler, org.eclipse.jface.text.source.ISourceViewer)
	 */
	public void setupPresentationReconciler(PresentationReconciler reconciler, ISourceViewer sourceViewer)
	{
		DefaultDamagerRepairer dr = new ThemeingDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		reconciler.setDamager(dr, DEFAULT);
		reconciler.setRepairer(dr, DEFAULT);

		dr = new ThemeingDamagerRepairer(getPreProcessorScanner());
		reconciler.setDamager(dr, PRE_PROCESSOR);
		reconciler.setRepairer(dr, PRE_PROCESSOR);

		dr = new ThemeingDamagerRepairer(getCDATAScanner());
		reconciler.setDamager(dr, CDATA);
		reconciler.setRepairer(dr, CDATA);

		dr = new ThemeingDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, XML_TAG);
		reconciler.setRepairer(dr, XML_TAG);

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(getToken("comment.block.xml")); //$NON-NLS-1$
		reconciler.setDamager(ndr, XMLSourceConfiguration.XML_COMMENT);
		reconciler.setRepairer(ndr, XMLSourceConfiguration.XML_COMMENT);
	}

	private ITokenScanner getPreProcessorScanner()
	{
		if (preProcessorScanner == null)
		{
			preProcessorScanner = new XMLTagScanner();
			preProcessorScanner.setDefaultReturnToken(getToken("meta.tag.preprocessor.xml")); //$NON-NLS-1$
		}
		return preProcessorScanner;
	}

	private ITokenScanner getCDATAScanner()
	{
		if (cdataScanner == null)
		{
			cdataScanner = new RuleBasedScanner();
			cdataScanner.setDefaultReturnToken(getToken("string.unquoted.cdata.xml")); //$NON-NLS-1$
		}
		return cdataScanner;
	}

	protected ITokenScanner getXMLScanner()
	{
		if (xmlScanner == null)
		{
			xmlScanner = new XMLScanner();
		}
		return xmlScanner;
	}

	protected ITokenScanner getXMLTagScanner()
	{
		if (xmlTagScanner == null)
		{
			xmlTagScanner = new XMLTagScanner();
		}
		return xmlTagScanner;
	}

	protected IToken getToken(String tokenName)
	{
		return new Token(tokenName);
	}
}
