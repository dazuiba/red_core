package com.aptana.editor.dtd.parsing.ast;

public class DTDElementDeclarationNode extends DTDNode
{
	private String _name;
	
	/**
	 * DTDElementDeclarationNode
	 */
	public DTDElementDeclarationNode(String name)
	{
		super(DTDNodeType.ELEMENT_DECLARATION);
		
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
