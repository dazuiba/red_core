package com.aptana.editor.dtd.parsing.ast;

public class DTDPEntityDeclarationNode extends DTDNode
{
	private String _name;
	
	/**
	 * DTDPEntityDeclarationNode
	 * 
	 * @param name
	 */
	public DTDPEntityDeclarationNode(String name)
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
