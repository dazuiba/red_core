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
}
