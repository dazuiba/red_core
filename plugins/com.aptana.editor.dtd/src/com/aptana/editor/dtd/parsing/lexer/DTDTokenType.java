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
	ENTITY("keyword.operator.dtd"),	// 10
	ELEMENT("keyword.operator.dtd"),	// 11
	SECTION_START("keyword.operator.dtd"),	// 12
	ATTLIST("keyword.operator.dtd"),	// 13
	NOTATION("keyword.operator.dtd"),	// 14
	PE_REF("entity.reference.parameter.dtd"),	// 15
	PI("source.dtd"),	// 16
	COMMENT("comment.dtd"),	// 17
	PUBLIC("keyword.operator.dtd"),	 // 18
	SYSTEM_LITERAL("source.dtd"),	// 19
	NMTOKEN("source.dtd"),	// 20
	SYSTEM("keyword.operator.dtd"),	 // 21
	LBRACKET("keyword.operator.dtd"),	// 22
	SECTION_END("keyword.operator.dtd"),	// 23
	RPAREN_STAR("source.dtd"),	// 24
	COMMA("source.dtd"),	// 25
	PERCENT("keyword.operator.dtd"),	// 26
	EMPTY("keyword.operator.dtd"),	// 27
	ANY("keyword.operator.dtd"),	// 28
	NOTATION_TYPE("keyword.operator.dtd"),	// 29
	INCLUDE("keyword.operator.dtd"),	// 30
	IGNORE("keyword.operator.dtd"),	// 31
	PCDATA("keyword.operator.dtd"),	// 32
	FIXED("keyword.operator.dtd"),	// 33
	NDATA("keyword.operator.dtd"),	// 34
	REQUIRED("keyword.operator.dtd"),	// 35
	IMPLIED("keyword.operator.dtd"),	// 36
	CDATA_TYPE("keyword.operator.dtd"),	// 37
	ID_TYPE("keyword.operator.dtd"),	// 38
	IDREF_TYPE("keyword.operator.dtd"),	// 39
	IDREFS_TYPE("keyword.operator.dtd"),	// 40
	ENTITY_TYPE("keyword.operator.dtd"),	// 41
	ENTITIES_TYPE("keyword.operator.dtd"),	// 42
	NMTOKEN_TYPE("keyword.operator.dtd"),	// 43
	NMTOKENS_TYPE("keyword.operator.dtd"),	// 44	
	
	NDATA_TYPE("keyword.operator.dtd");
	
	
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
