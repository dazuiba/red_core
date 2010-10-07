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

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.dtd.parsing.ast.DTDNode#accept(com.aptana.editor.dtd.parsing.ast.DTDTreeWalker)
	 */
	public void accept(DTDTreeWalker walker)
	{
		walker.visit(this);
	}

	/**
	 * getText
	 */
	public String getText()
	{
		return this._text;
	}
}
