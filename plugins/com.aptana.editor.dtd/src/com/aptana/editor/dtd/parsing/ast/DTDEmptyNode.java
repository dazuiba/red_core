package com.aptana.editor.dtd.parsing.ast;

public class DTDEmptyNode extends DTDNode
{
	/**
	 * DTDEmptyNode
	 */
	public DTDEmptyNode()
	{
		super(DTDNodeType.EMPTY);
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
