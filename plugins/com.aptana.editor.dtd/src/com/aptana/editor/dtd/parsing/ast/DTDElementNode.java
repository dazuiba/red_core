package com.aptana.editor.dtd.parsing.ast;

public class DTDElementNode extends DTDNode
{
	private String _name;

	/**
	 * DTDElementNode
	 */
	public DTDElementNode(String name)
	{
		super(DTDNodeType.ELEMENT);

		this._name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.dtd.parsing.ast.DTDNode#accept(com.aptana.editor.dtd.parsing.ast.DTDTreeWalker)
	 */
	public void accept(DTDTreeWalker walker)
	{
		walker.visit(this);
	}

	/**
	 * getName
	 */
	public String getName()
	{
		return this._name;
	}
}
