package com.aptana.editor.dtd.parsing.ast;

public class DTDOneOrMoreNode extends DTDNode
{
	/**
	 * DTDOneOrMoreNode
	 */
	public DTDOneOrMoreNode()
	{
		super(DTDNodeType.ONE_OR_MORE);
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
