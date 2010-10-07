package com.aptana.editor.dtd.parsing.ast;

public class DTDNDataDeclNode extends DTDNode
{
	private String _name;

	/**
	 * DTDNDataDeclNode
	 */
	public DTDNDataDeclNode(String name)
	{
		super(DTDNodeType.NDATA_DECLARATION);

		this._name = name;
	}

	/**
	 * getName
	 */
	public String getName()
	{
		return this._name;
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
