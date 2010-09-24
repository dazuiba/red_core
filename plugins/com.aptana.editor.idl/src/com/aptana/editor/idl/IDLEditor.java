package com.aptana.editor.idl;

import com.aptana.editor.common.AbstractThemeableEditor;

public class IDLEditor extends AbstractThemeableEditor
{
	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.AbstractThemeableEditor#initializeEditor()
	 */
	protected void initializeEditor()
	{
		super.initializeEditor();

		this.setSourceViewerConfiguration(new IDLSourceViewerConfiguration(this.getPreferenceStore(), this));
		this.setDocumentProvider(new IDLDocumentProvider());
	}
}
