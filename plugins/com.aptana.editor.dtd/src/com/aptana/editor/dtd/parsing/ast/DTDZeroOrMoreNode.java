package com.aptana.editor.dtd.parsing.ast;

public class DTDZeroOrMoreNode extends DTDNode
{
	/**
	 * DTDZeroOrMoreNode
	 */
	public DTDZeroOrMoreNode()
	{
		super(DTDNodeType.ZERO_OR_MORE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.dtd.parsing.ast.DTDNode#accept(com.aptana.editor.dtd.parsing.ast.DTDTreeWalker)
	 */
	public void accept(DTDTreeWalker walker)
	{
		walker.visit(this);
	}
}
