package com.aptana.editor.dtd.parsing.lexer;

import java.util.EnumSet;

public enum DTDTokenType
{
	UNDEFINED(""),	// -1
	EOF(""),	// 0
	GREATER_THAN("source.dtd"), // 1
	NAME("source.dtd"),	// 2
	LPAREN("source.dtd"),	 // 3
	RPAREN("source.dtd"),	// 4
	PIPE("source.dtd"),	// 5
	QUESTION("source.dtd"),	// 6
	STAR("source.dtd"),	// 7
	PLUS("source.dtd"),	// 8
	PUBLIC("keyword.operator.dtd"),	 // 9
	SYSTEM_LITERAL("source.dtd"),	// 10
	NMTOKEN("source.dtd"),	// 11
	SYSTEM("keyword.operator.dtd"),	 // 12
	PI_START("source.dtd"),	// 13
	ENTITY("keyword.operator.dtd"),	// 14
	ELEMENT("keyword.operator.dtd"),	// 15
	ATTLIST("keyword.operator.dtd"),	// 16
	NOTATION("keyword.operator.dtd"),	// 17
	PUBID_LITERAL("source.dtd"),	// 18
	RPAREN_STAR("source.dtd"),	// 19
	ATT_VALUE("source.dtd"),	// 20
	PE_REF("source.dtd"),	// 21
	COMMENT(""),	// 22
	ENTITY_VALUE("source.dtd"),	// 23
	COMMA("source.dtd"),	// 24
	NOT_PI_END("source.dtd"),	// 25
	PI_END("source.dtd"),	// 26
	PERCENT("keyword.operator.dtd"),	// 27
	NOTATION_TYPE("keyword.operator.dtd"),	// 28
	PCDATA("keyword.operator.dtd"),	// 29
	FIXED("keyword.operator.dtd"),	// 30
	NDATA("keyword.operator.dtd"),	// 31
	EMPTY("keyword.operator.dtd"),	// 32
	ANY("keyword.operator.dtd"),	// 33
	REQUIRED("keyword.operator.dtd"),	// 34
	IMPLIED("keyword.operator.dtd"),	// 35
	CDATA_TYPE("keyword.operator.dtd"),	// 36
	ID_TYPE("keyword.operator.dtd"),	// 37
	IDREF_TYPE("keyword.operator.dtd"),	// 38
	IDREFS_TYPE("keyword.operator.dtd"),	// 39
	ENTITY_TYPE("keyword.operator.dtd"),	// 40
	ENTITIES_TYPE("keyword.operator.dtd"),	// 41
	NMTOKEN_TYPE("keyword.operator.dtd"),	// 42
	NMTOKENS_TYPE("keyword.operator.dtd");	// 43
	//ENTITY_REF("source.dtd"),
	
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
