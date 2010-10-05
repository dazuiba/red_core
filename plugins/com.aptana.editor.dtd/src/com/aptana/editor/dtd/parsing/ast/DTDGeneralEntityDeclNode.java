package com.aptana.editor.dtd.parsing.ast;

public class DTDGeneralEntityDeclNode extends DTDNode
{
	private String _name;

	/**
	 * DTDGEntityDeclarationNode
	 * 
	 * @param name
	 */
	public DTDGeneralEntityDeclNode(String name)
	{
		super(DTDNodeType.G_ENTITY_DECLARATION);

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
