package com.aptana.editor.dtd.parsing.ast;

public class DTDIgnoreSectionNode extends DTDNode
{
	/**
	 * DTDIncludeSectionNode
	 */
	public DTDIgnoreSectionNode()
	{
		super(DTDNodeType.IGNORE_SECTION);
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
