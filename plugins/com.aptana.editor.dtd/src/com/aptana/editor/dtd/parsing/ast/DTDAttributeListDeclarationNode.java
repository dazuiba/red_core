package com.aptana.editor.dtd.parsing.ast;

public class DTDAttributeListDeclarationNode extends DTDNode
{
	private String _name;
	
	/**
	 * DTDAttributeListDeclarationNode
	 */
	public DTDAttributeListDeclarationNode(String name)
	{
		super(DTDNodeType.ATTRIBUTE_LIST_DECLARATION);
		
		this._name = name;
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
