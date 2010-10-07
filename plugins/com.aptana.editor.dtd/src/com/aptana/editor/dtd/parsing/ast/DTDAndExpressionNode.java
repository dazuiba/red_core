package com.aptana.editor.dtd.parsing.ast;

public class DTDAndExpressionNode extends DTDNode
{
	/**
	 * DTDAndExpressionNode
	 */
	public DTDAndExpressionNode()
	{
		super(DTDNodeType.AND_EXPRESSION);
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
