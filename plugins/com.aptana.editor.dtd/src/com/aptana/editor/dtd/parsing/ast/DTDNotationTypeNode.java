package com.aptana.editor.dtd.parsing.ast;

public class DTDNotationTypeNode extends DTDNode
{
	/**
	 * DTDNotationTypeNode
	 */
	public DTDNotationTypeNode()
	{
		super(DTDNodeType.NOTATION);
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