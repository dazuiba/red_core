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

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.dtd.parsing.ast.DTDNode#accept(com.aptana.editor.dtd.parsing.ast.DTDTreeWalker)
	 */
	public void accept(DTDTreeWalker walker)
	{
		walker.visit(this);
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
