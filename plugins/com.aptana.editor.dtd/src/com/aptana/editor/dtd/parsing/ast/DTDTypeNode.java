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
