package com.aptana.editor.dtd.parsing.ast;

public class DTDOrExpressionNode extends DTDNode
{
	/**
	 * DTDOrExpressionNode
	 */
	public DTDOrExpressionNode()
	{
		super(DTDNodeType.OR_EXPRESSION);
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
