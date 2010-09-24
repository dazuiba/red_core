package com.aptana.editor.idl;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.EndOfLineRule;
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

public class IDLSourceConfiguration implements IPartitioningConfiguration, ISourceViewerConfiguration
{
	public static final String PREFIX = "__idl__";
	public static final String DEFAULT = "__idl" + IDocument.DEFAULT_CONTENT_TYPE;
	public final static String IDL_SINGLELINE_COMMENT = PREFIX + "singleline_comment";
	public static final String IDL_MULTILINE_COMMENT = PREFIX + "multiline_comment";
	public final static String IDL_DOC_COMMENT = PREFIX + "doc_comment";

	// TODO: add other content types
	public static final String[] CONTENT_TYPES = new String[] { DEFAULT, IDL_MULTILINE_COMMENT, IDL_SINGLELINE_COMMENT, IDL_DOC_COMMENT };
	private static final String[][] TOP_CONTENT_TYPES = new String[][] { { IIDLConstants.CONTENT_TYPE_IDL } };

	private IPredicateRule[] partitioningRules = new IPredicateRule[] {
		new EndOfLineRule("//", new Token(IDL_SINGLELINE_COMMENT)),
		new MultiLineRule("/**", "*/", new Token(IDL_DOC_COMMENT), '\0', true),
		new MultiLineRule("/*", "*/", new Token(IDL_MULTILINE_COMMENT), '\0', true)
	};
	private IDLScanner idlScanner;

	private static IDLSourceConfiguration instance;

	/**
	 * getDefault
	 * 
	 * @return
	 */
	public static IDLSourceConfiguration getDefault()
	{
		if (instance == null)
		{
			IContentTypeTranslator c = CommonEditorPlugin.getDefault().getContentTypeTranslator();

			c.addTranslation(new QualifiedContentType(IIDLConstants.CONTENT_TYPE_IDL), new QualifiedContentType("source.idl"));
			c.addTranslation(new QualifiedContentType(IDL_SINGLELINE_COMMENT), new QualifiedContentType("comment.line.double-slash.idl"));
			c.addTranslation(new QualifiedContentType(IDL_DOC_COMMENT), new QualifiedContentType("comment.block.documentation.idl"));
			c.addTranslation(new QualifiedContentType(IDL_MULTILINE_COMMENT), new QualifiedContentType("comment.block.idl"));

			instance = new IDLSourceConfiguration();
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
			return IIDLConstants.CONTENT_TYPE_IDL;
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
		if (idlScanner == null)
		{
			idlScanner = new IDLScanner();
		}

		return idlScanner;
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
		
		NonRuleBasedDamagerRepairer docCommentDR = new NonRuleBasedDamagerRepairer(this.getToken("comment.block.documentation.idl"));
		reconciler.setDamager(docCommentDR, IDL_DOC_COMMENT);
		reconciler.setRepairer(docCommentDR, IDL_DOC_COMMENT);

		NonRuleBasedDamagerRepairer multilineCommentDR = new NonRuleBasedDamagerRepairer(this.getToken("comment.block.idl"));
		reconciler.setDamager(multilineCommentDR, IDL_MULTILINE_COMMENT);
		reconciler.setRepairer(multilineCommentDR, IDL_MULTILINE_COMMENT);
		
		NonRuleBasedDamagerRepairer singlelineCommentDR = new NonRuleBasedDamagerRepairer(this.getToken("comment.line.double-slash.idl"));
		reconciler.setDamager(singlelineCommentDR, IDL_SINGLELINE_COMMENT);
		reconciler.setRepairer(singlelineCommentDR, IDL_SINGLELINE_COMMENT);
	}
}
