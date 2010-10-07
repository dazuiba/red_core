package com.aptana.editor.dtd.parsing.ast;

public class DTDPCDataNode extends DTDNode
{
	/**
	 * DTDPCDataNode
	 */
	public DTDPCDataNode()
	{
		super(DTDNodeType.PCDATA);
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
