package com.aptana.editor.dtd.parsing.ast;

public class DTDProcessingInstructionNode extends DTDNode
{
	private String _text;

	/**
	 * DTDProcessingInstructionNode
	 */
	public DTDProcessingInstructionNode(String text)
	{
		super(DTDNodeType.PROCESSING_INSTRUCTION);

		this._text = text;
	}

	/**
	 * getText
	 */
	public String getText()
	{
		return this._text;
	}
}
