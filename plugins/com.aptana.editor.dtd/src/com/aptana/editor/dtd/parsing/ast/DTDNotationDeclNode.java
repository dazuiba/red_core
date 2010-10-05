package com.aptana.editor.dtd.parsing.ast;

public class DTDNotationDeclNode extends DTDNode
{
	private String _name;

	/**
	 * DTDNotationDeclarationNode
	 */
	public DTDNotationDeclNode(String name)
	{
		super(DTDNodeType.NOTATION_DECLARATION);

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
