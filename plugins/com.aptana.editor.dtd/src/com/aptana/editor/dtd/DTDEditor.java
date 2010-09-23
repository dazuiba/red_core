package com.aptana.editor.dtd;

import com.aptana.editor.common.AbstractThemeableEditor;

public class DTDEditor extends AbstractThemeableEditor
{
	protected void initializeEditor()
	{
		super.initializeEditor();
		
		this.setSourceViewerConfiguration(new DTDSourceViewerConfiguration(this.getPreferenceStore(), this));
		this.setDocumentProvider(new DTDDocumentProvider());
	}
}
