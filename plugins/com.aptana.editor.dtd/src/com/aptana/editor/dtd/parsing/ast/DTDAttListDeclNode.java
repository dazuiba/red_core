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
