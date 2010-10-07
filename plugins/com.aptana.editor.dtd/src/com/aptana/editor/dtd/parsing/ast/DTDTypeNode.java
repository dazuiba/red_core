package com.aptana.editor.dtd.parsing.ast;

public class DTDTypeNode extends DTDNode
{
	private String _type;

	/**
	 * DTDTypeNode
	 * 
	 * @param type
	 */
	public DTDTypeNode(String type)
	{
		super(DTDNodeType.TYPE);

		this._type = type;
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
	 * getType
	 * 
	 * @return
	 */
	public String getType()
	{
		return this._type;
	}
}
