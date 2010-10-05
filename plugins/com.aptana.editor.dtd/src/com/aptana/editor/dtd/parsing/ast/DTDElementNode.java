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

	/**
	 * getName
	 */
	public String getName()
	{
		return this._name;
	}
}
