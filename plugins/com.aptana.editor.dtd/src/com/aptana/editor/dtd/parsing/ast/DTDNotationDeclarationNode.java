package com.aptana.editor.dtd.parsing.ast;

public class DTDNotationDeclarationNode extends DTDNode
{
	private String _name;

	/**
	 * DTDNotationDeclarationNode
	 */
	public DTDNotationDeclarationNode(String name)
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
