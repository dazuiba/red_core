package com.aptana.editor.dtd.parsing.ast;

public class DTDParsedEntityDeclNode extends DTDNode
{
	private String _name;
	
	/**
	 * DTDPEntityDeclarationNode
	 * 
	 * @param name
	 */
	public DTDParsedEntityDeclNode(String name)
	{
		super(DTDNodeType.P_ENTITY_DECLARATION);
		
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
