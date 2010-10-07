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
