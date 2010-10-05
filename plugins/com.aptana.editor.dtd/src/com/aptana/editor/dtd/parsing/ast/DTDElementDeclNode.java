package com.aptana.editor.dtd.parsing.ast;

import com.aptana.parsing.ast.IParseNode;

public class DTDElementDeclNode extends DTDNode
{
	private String _name;
	
	/**
	 * DTDElementDeclarationNode
	 */
	public DTDElementDeclNode(String name)
	{
		super(DTDNodeType.ELEMENT_DECLARATION);
		
		this._name = name;
	}
	
	/**
	 * getChildExpression
	 * 
	 * @return
	 */
	public IParseNode getChildExpression()
	{
		return this.getFirstChild();
	}
	
	/**
	 * getName
	 */
	public String getName()
	{
		return this._name;
	}
}
