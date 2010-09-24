package com.aptana.editor.dtd.parsing.lexer;

import java.util.EnumSet;

public enum DTDTokenType
{
	ELEMENT("keyword.operator.dtd"),
	GREATER_THAN("source.dtd"),
	ATTLIST("keyword.operator.dtd"),
	NOTATION("keyword.operator.dtd"),
	PI_START("source.dtd"),
	PI_END("source.dtd"),
	EMPTY("keyword.operator.dtd"),
	ANY("keyword.operator.dtd"),
	ENTITY("keyword.operator.dtd"),
	SYSTEM("keyword.operator.dtd"),
	PUBLIC("keyword.operator.dtd"),
	PCDATA("keyword.operator.dtd"),
	LPAREN("source.dtd"),
	PIPE("source.dtd"),
	RPAREN("source.dtd"),
	QUESTION("source.dtd"),
	STAR("source.dtd"),
	PLUS("source.dtd"),
	FIXED("keyword.operator.dtd"),
	IMPLIED("keyword.operator.dtd"),
	REQUIRED("keyword.operator.dtd"),
	COMMA("source.dtd"),
	CDATA_TYPE("keyword.operator.dtd"),
	ID_TYPE("keyword.operator.dtd"),
	IDREF_TYPE("keyword.operator.dtd"),
	IDREFS_TYPE("keyword.operator.dtd"),
	ENTITY_TYPE("keyword.operator.dtd"),
	ENTITIES_TYPE("keyword.operator.dtd"),
	NMTOKEN_TYPE("keyword.operator.dtd"),
	NMTOKENS_TYPE("keyword.operator.dtd"),
	NDATA("keyword.operator.dtd"),
	NOTATION_TYPE("keyword.operator.dtd");
	
	private short _index;
	private String _scope;
	
	/**
	 * static initializer
	 */
	static
	{
		short index = -1;
		
		for (DTDTokenType type : EnumSet.allOf(DTDTokenType.class))
		{
			type._index = index++;
		}
	}
	
	/**
	 * DTDTokenType
	 * 
	 * @param scope
	 */
	private DTDTokenType(String scope)
	{
		this._scope = scope;
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
	
	/**
	 * getScope
	 * 
	 * @return
	 */
	public String getScope()
	{
		return this._scope;
	}
}
