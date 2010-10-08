package com.aptana.editor.dtd.parsing.ast;

import com.aptana.editor.dtd.parsing.DTDParserConstants;
import com.aptana.parsing.ast.ParseNode;

public class DTDNode extends ParseNode
{
	private DTDNodeType _type;

	/**
	 * DTDNode
	 */
	public DTDNode()
	{
		this(DTDNodeType.EMPTY);
	}

	/**
	 * DTDNodeType
	 * 
	 * @param type
	 */
	protected DTDNode(DTDNodeType type)
	{
		super(DTDParserConstants.LANGUAGE);

		this._type = type;
	}

	/**
	 * accept
	 * 
	 * @param walker
	 */
	public void accept(DTDTreeWalker walker)
	{
		// sub-classes must override this method so their types will be
		// recognized properly
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.parsing.ast.ParseNode#getNodeType()
	 */
	public short getNodeType()
	{
		return this._type.getIndex();
	}
}
