package com.aptana.editor.dtd.parsing.ast;

public class DTDAttributeNode extends DTDNode
{
	private String _name;
	private String _mode;
	
	/**
	 * DTDAttributeNode
	 */
	public DTDAttributeNode(String name, DTDNode type, String mode)
	{
		super(DTDNodeType.ATTRIBUTE);

		this._name = name;
		this.addChild(type);
		this._mode = mode;
	}

	/**
	 * getMode
	 */
	public String getMode()
	{
		return this._mode;
	}
	
	/**
	 * getName
	 * 
	 * @return
	 */
	public String getName()
	{
		return this._name;
	}
}
