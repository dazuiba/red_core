package com.aptana.editor.dtd;

import com.aptana.editor.common.AbstractThemeableEditor;
import com.aptana.editor.common.parsing.FileService;
import com.aptana.editor.dtd.parsing.DTDParserConstants;

public class DTDEditor extends AbstractThemeableEditor
{
	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.AbstractThemeableEditor#initializeEditor()
	 */
	protected void initializeEditor()
	{
		super.initializeEditor();
		
		this.setSourceViewerConfiguration(new DTDSourceViewerConfiguration(this.getPreferenceStore(), this));
		this.setDocumentProvider(new DTDDocumentProvider());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.AbstractThemeableEditor#createFileService()
	 */
	protected FileService createFileService()
	{
		return new FileService(DTDParserConstants.LANGUAGE);
	}
}
