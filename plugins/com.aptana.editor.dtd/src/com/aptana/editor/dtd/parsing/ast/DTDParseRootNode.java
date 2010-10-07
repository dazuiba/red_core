package com.aptana.editor.dtd.parsing.ast;

import beaver.Symbol;

import com.aptana.editor.dtd.parsing.DTDParserConstants;
import com.aptana.parsing.ast.ParseRootNode;

public class DTDParseRootNode extends ParseRootNode
{
	/**
	 * DTDParseRootNode
	 */
	public DTDParseRootNode()
	{
		this(new Symbol[0]);
	}

	/**
	 * DTDParseRootNode
	 * 
	 * @param children
	 */
	public DTDParseRootNode(Symbol[] children)
	{
		super( //
			DTDParserConstants.LANGUAGE, //
			children, //
			(children != null && children.length > 0) ? children[0].getStart() : 0, //
			(children != null && children.length > 0) ? children[0].getEnd() : 0 //
		);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.dtd.parsing.ast.DTDNode#accept(com.aptana.editor.dtd.parsing.ast.DTDTreeWalker)
	 */
	public void accept(DTDTreeWalker walker)
	{
		walker.visit(this);
	}
}
