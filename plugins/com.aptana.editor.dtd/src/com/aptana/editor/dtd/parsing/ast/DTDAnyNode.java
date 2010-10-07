package com.aptana.editor.dtd.parsing.ast;

public class DTDAnyNode extends DTDNode
{
	/**
	 * DTDAnyNode
	 */
	public DTDAnyNode()
	{
		super(DTDNodeType.ANY);
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
