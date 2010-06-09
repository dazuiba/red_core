package com.aptana.editor.js.parsing.ast;

import beaver.Symbol;

public class JSFalseNode extends JSPrimitiveNode
{
	/**
	 * JSFalseNode
	 * 
	 * @param identifier
	 */
	public JSFalseNode(Symbol identifier)
	{
		this(identifier.getStart(), identifier.getEnd());
	}

	/**
	 * JSFalseNode
	 * 
	 * @param text
	 * @param start
	 * @param end
	 */
	public JSFalseNode(int start, int end)
	{
		super(JSNodeTypes.FALSE, start, end, "false");
	}
}