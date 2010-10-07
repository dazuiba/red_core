package com.aptana.editor.dtd.parsing.ast;

public class DTDIncludeSectionNode extends DTDNode
{
	/**
	 * DTDIncludeSectionNode
	 */
	public DTDIncludeSectionNode()
	{
		super(DTDNodeType.INCLUDE_SECTION);
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
