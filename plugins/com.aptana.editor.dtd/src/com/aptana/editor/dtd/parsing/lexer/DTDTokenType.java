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
	STRING("keyword.operator.dtd"),	// 5
	PIPE("source.dtd"),	// 6
	QUESTION("source.dtd"),	// 7
	STAR("source.dtd"),	// 8
	PLUS("source.dtd"),	// 9
	PUBLIC("keyword.operator.dtd"),	 // 10
	SYSTEM_LITERAL("source.dtd"),	// 11
	NMTOKEN("source.dtd"),	// 12
	SYSTEM("keyword.operator.dtd"),	 // 13
	ELEMENT("keyword.operator.dtd"),	// 14
	ENTITY("keyword.operator.dtd"),	// 15
	ATTLIST("keyword.operator.dtd"),	// 16
	NOTATION("keyword.operator.dtd"),	// 17
	RPAREN_STAR("source.dtd"),	// 18
	PE_REF("source.dtd"),	// 19
	PI("source.dtd"),	// 20
	COMMA("source.dtd"),	// 21
	EMPTY("keyword.operator.dtd"),	// 22
	ANY("keyword.operator.dtd"),	// 23
	PERCENT("keyword.operator.dtd"),	// 24
	NOTATION_TYPE("keyword.operator.dtd"),	// 25
	PCDATA("keyword.operator.dtd"),	// 26
	FIXED("keyword.operator.dtd"),	// 27
	NDATA("keyword.operator.dtd"),	// 28
	REQUIRED("keyword.operator.dtd"),	// 29
	IMPLIED("keyword.operator.dtd"),	// 30
	CDATA_TYPE("keyword.operator.dtd"),	// 31
	ID_TYPE("keyword.operator.dtd"),	// 32
	IDREF_TYPE("keyword.operator.dtd"),	// 33
	IDREFS_TYPE("keyword.operator.dtd"),	// 34
	ENTITY_TYPE("keyword.operator.dtd"),	// 35
	ENTITIES_TYPE("keyword.operator.dtd"),	// 36
	NMTOKEN_TYPE("keyword.operator.dtd"),	// 37
	NMTOKENS_TYPE("keyword.operator.dtd"),	// 38
	
	NDATA_TYPE("keyword.operator.dtd"),
	SECTION_START("keyword.operator.dtd"),
	SECTION_END("keyword.operator.dtd"),
	IGNORE("keyword.operator.dtd"),
	INCLUDE("keyword.operator.dtd"),
	LBRACKET("keyword.operator.dtd"),
	
	COMMENT("");
	
	//ENTITY_REF("source.dtd"),
	
	//ATT_VALUE("source.dtd"),
	//ENTITY_VALUE("source.dtd"),
	//PUBID_LITERAL("source.dtd"),
	
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
