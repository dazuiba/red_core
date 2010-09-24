package com.aptana.editor.dtd;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.MultiLineRule;
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
import com.aptana.editor.common.text.rules.ThemeingDamagerRepairer;

public class DTDSourceConfiguration implements IPartitioningConfiguration, ISourceViewerConfiguration
{
	public static final String PREFIX = "__dtd__";
	public static final String DEFAULT = "__dtd" + IDocument.DEFAULT_CONTENT_TYPE;
	public static final String DTD_COMMENT = PREFIX + "comment";
	public final static String STRING_DOUBLE = PREFIX + "string_double"; //$NON-NLS-1$
	public final static String STRING_SINGLE = PREFIX + "string_single"; //$NON-NLS-1$

	// TODO: add other content types
	public static final String[] CONTENT_TYPES = new String[] { DEFAULT, DTD_COMMENT, STRING_DOUBLE, STRING_SINGLE };
	private static final String[][] TOP_CONTENT_TYPES = new String[][] { { IDTDConstants.CONTENT_TYPE_DTD } };

	private IPredicateRule[] partitioningRules = new IPredicateRule[] {
		new MultiLineRule("<!--", "-->", new Token(DTD_COMMENT), '\0', true),
		new MultiLineRule("\"", "\"", new Token(DTD_COMMENT), '\0', true),
		new MultiLineRule("\'", "\'", new Token(DTD_COMMENT), '\0', true)
	};
	private DTDScanner dtdScanner;

	private static DTDSourceConfiguration instance;

	/**
	 * getDefault
	 * 
	 * @return
	 */
	public static DTDSourceConfiguration getDefault()
	{
		if (instance == null)
		{
			IContentTypeTranslator c = CommonEditorPlugin.getDefault().getContentTypeTranslator();

			c.addTranslation(new QualifiedContentType(IDTDConstants.CONTENT_TYPE_DTD), new QualifiedContentType("text.dtd"));

			instance = new DTDSourceConfiguration();
		}

		return instance;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.IPartitioningConfiguration#createSubPartitionScanner()
	 */
	@Override
	public ISubPartitionScanner createSubPartitionScanner()
	{
		return new SubPartitionScanner(partitioningRules, CONTENT_TYPES, new Token(DEFAULT));
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.IPartitioningConfiguration#getContentTypes()
	 */
	@Override
	public String[] getContentTypes()
	{
		return CONTENT_TYPES;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.IPartitioningConfiguration#getDocumentContentType(java.lang.String)
	 */
	@Override
	public String getDocumentContentType(String contentType)
	{
		if (contentType.startsWith(PREFIX))
		{
			return IDTDConstants.CONTENT_TYPE_DTD;
		}

		return null;
	}

	/**
	 * getDTDScanner
	 * 
	 * @return
	 */
	protected ITokenScanner getDTDScanner()
	{
		if (dtdScanner == null)
		{
			dtdScanner = new DTDScanner();
		}

		return dtdScanner;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.IPartitioningConfiguration#getPartitioningRules()
	 */
	@Override
	public IPredicateRule[] getPartitioningRules()
	{
		return partitioningRules;
	}

	/**
	 * getToken
	 * 
	 * @param tokenName
	 * @return
	 */
	protected IToken getToken(String tokenName)
	{
		return new Token(tokenName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.ITopContentTypesProvider#getTopContentTypes()
	 */
	@Override
	public String[][] getTopContentTypes()
	{
		return TOP_CONTENT_TYPES;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.aptana.editor.common.ISourceViewerConfiguration#setupPresentationReconciler(org.eclipse.jface.text.presentation
	 * .PresentationReconciler, org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public void setupPresentationReconciler(PresentationReconciler reconciler, ISourceViewer sourceViewer)
	{
		DefaultDamagerRepairer dr = new ThemeingDamagerRepairer(getDTDScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		reconciler.setDamager(dr, DEFAULT);
		reconciler.setRepairer(dr, DEFAULT);

		NonRuleBasedDamagerRepairer commentDR = new NonRuleBasedDamagerRepairer(this.getToken("comment.block.dtd"));
		reconciler.setDamager(commentDR, DTD_COMMENT);
		reconciler.setRepairer(commentDR, DTD_COMMENT);
		
		NonRuleBasedDamagerRepairer singleQuotedStringDR = new NonRuleBasedDamagerRepairer(this.getToken("string.quoted.single.dtd"));
		reconciler.setDamager(singleQuotedStringDR, STRING_SINGLE);
		reconciler.setRepairer(singleQuotedStringDR, STRING_SINGLE);
		
		NonRuleBasedDamagerRepairer doubleQuotedStringDR = new NonRuleBasedDamagerRepairer(this.getToken("string.quoted.double.dtd"));
		reconciler.setDamager(doubleQuotedStringDR, STRING_DOUBLE);
		reconciler.setRepairer(doubleQuotedStringDR, STRING_DOUBLE);
	}
}
