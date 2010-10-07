package com.aptana.editor.dtd.parsing.lexer;

import java.util.EnumSet;

public enum DTDTokenType
{
	UNDEFINED(""),	// -1
	EOF(""),	// 0
	NAME("source.dtd"),	// 1
	GREATER_THAN("source.dtd"), // 2
	STRING("keyword.operator.dtd"),	// 3
	LPAREN("source.dtd"),	 // 4
	RPAREN("source.dtd"),	// 5
	STAR("source.dtd"),	// 6
	ENTITY("keyword.operator.dtd"),	// 7
	ELEMENT("keyword.operator.dtd"),	// 8
	SECTION_START("keyword.operator.dtd"),	// 9
	ATTLIST("keyword.operator.dtd"),	// 10
	NOTATION("keyword.operator.dtd"),	// 11
	PIPE("source.dtd"),	// 12
	PE_REF("entity.reference.parameter.dtd"),	// 13
	PI("source.dtd"),	// 14
	COMMENT("comment.dtd"),	// 15
	PUBLIC("keyword.operator.dtd"),	 // 16
	QUESTION("source.dtd"),	// 17
	PLUS("source.dtd"),	// 18
	SYSTEM("keyword.operator.dtd"),	 // 19
	SECTION_END("keyword.operator.dtd"),	// 20
	COMMA("source.dtd"),	// 21
	PERCENT("keyword.operator.dtd"),	// 22
	EMPTY("keyword.operator.dtd"),	// 23
	ANY("keyword.operator.dtd"),	// 24
	PCDATA("keyword.operator.dtd"),	// 25
	INCLUDE("keyword.operator.dtd"),	// 26
	LBRACKET("keyword.operator.dtd"),	// 27
	FIXED("keyword.operator.dtd"),	// 28
	NOTATION_TYPE("keyword.operator.dtd"),	// 29
	REQUIRED("keyword.operator.dtd"),	// 30
	IMPLIED("keyword.operator.dtd"),	// 31
	NDATA("keyword.operator.dtd"),	// 32
	IGNORE("keyword.operator.dtd"),	// 33
	CDATA_TYPE("keyword.operator.dtd"),	// 34
	ID_TYPE("keyword.operator.dtd"),	// 35
	IDREF_TYPE("keyword.operator.dtd"),	// 36
	IDREFS_TYPE("keyword.operator.dtd"),	// 37
	ENTITY_TYPE("keyword.operator.dtd"),	// 38
	ENTITIES_TYPE("keyword.operator.dtd"),	// 39
	NMTOKEN_TYPE("keyword.operator.dtd"),	// 40
	NMTOKENS_TYPE("keyword.operator.dtd");	// 41	
	
	
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
