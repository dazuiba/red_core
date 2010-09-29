package com.aptana.editor.dtd.parsing.ast;

import java.util.EnumSet;

public enum DTDNodeType
{
	EMPTY,
	ELEMENT_DECLARATION,
	ATTRIBUTE_LIST_DECLARATION,
	P_ENTITY_DECLARATION,
	G_ENTITY_DECLARATION,
	NOTATION_DECLARATION,
	PROCESSING_INSTRUCTION;
	
	private short _index;
	
	/**
	 * static initializer
	 */
	static
	{
		short index = 0;
		
		for (DTDNodeType type : EnumSet.allOf(DTDNodeType.class))
		{
			type._index = index++;
		}
	}
	
	/**
	 * getIndex
	 * 
	 * @return
	 */
	public short getIndex()
	{
		return this._index;
	}
}
