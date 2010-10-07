package com.aptana.editor.dtd.parsing.ast;

public class DTDOptionalNode extends DTDNode
{
	/**
	 * DTDOptionalNode
	 */
	public DTDOptionalNode()
	{
		super(DTDNodeType.OPTIONAL);
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
