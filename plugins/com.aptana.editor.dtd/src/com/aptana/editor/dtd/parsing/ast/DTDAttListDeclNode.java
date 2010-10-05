package com.aptana.editor.dtd.parsing.ast;

public class DTDAttListDeclNode extends DTDNode
{
	private String _name;
	
	/**
	 * DTDAttributeListDeclarationNode
	 */
	public DTDAttListDeclNode(String name)
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
