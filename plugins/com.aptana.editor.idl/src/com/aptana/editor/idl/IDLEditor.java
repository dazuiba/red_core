package com.aptana.editor.idl;

import com.aptana.editor.common.AbstractThemeableEditor;
import com.aptana.editor.common.parsing.FileService;
import com.aptana.editor.idl.parsing.IDLParserConstants;

public class IDLEditor extends AbstractThemeableEditor
{
	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.AbstractThemeableEditor#createFileService()
	 */
	protected FileService createFileService()
	{
		return new FileService(IDLParserConstants.LANGUAGE);
	}

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
