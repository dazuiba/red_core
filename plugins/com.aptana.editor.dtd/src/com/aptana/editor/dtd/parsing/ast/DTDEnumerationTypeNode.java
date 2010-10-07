package com.aptana.editor.dtd.parsing.ast;

public class DTDEnumerationTypeNode extends DTDNode
{
	/**
	 * DTDEnumerationTypeNode
	 */
	public DTDEnumerationTypeNode()
	{
		super(DTDNodeType.ENUMERATION);
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
