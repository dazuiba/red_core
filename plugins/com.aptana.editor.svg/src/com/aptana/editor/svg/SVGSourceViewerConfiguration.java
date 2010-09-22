package com.aptana.editor.svg;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;

import com.aptana.editor.common.AbstractThemeableEditor;
import com.aptana.editor.common.CommonSourceViewerConfiguration;
import com.aptana.editor.common.TextUtils;

public class SVGSourceViewerConfiguration extends CommonSourceViewerConfiguration
{

	public SVGSourceViewerConfiguration(IPreferenceStore preferences, AbstractThemeableEditor editor)
	{
		super(preferences, editor);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source
	 * .ISourceViewer)
	 */
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer)
	{
		return TextUtils.combine(new String[][] { { IDocument.DEFAULT_CONTENT_TYPE },
				SVGSourceConfiguration.CONTENT_TYPES });
	}

	@Override
	public String[][] getTopContentTypes()
	{
		return SVGSourceConfiguration.getDefault().getTopContentTypes();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source
	 * .ISourceViewer)
	 */
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer)
	{
		PresentationReconciler reconciler = (PresentationReconciler) super.getPresentationReconciler(sourceViewer);
		SVGSourceConfiguration.getDefault().setupPresentationReconciler(reconciler, sourceViewer);
		return reconciler;
	}

}
