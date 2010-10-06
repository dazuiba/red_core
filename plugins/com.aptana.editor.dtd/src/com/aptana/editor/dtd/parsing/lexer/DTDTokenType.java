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
	ENTITY("keyword.operator.dtd"),	// 6
	ELEMENT("keyword.operator.dtd"),	// 7
	SECTION_START("keyword.operator.dtd"),	// 8
	ATTLIST("keyword.operator.dtd"),	// 9
	NOTATION("keyword.operator.dtd"),	// 10
	PE_REF("entity.reference.parameter.dtd"),	// 11
	PI("source.dtd"),	// 12
	COMMENT("comment.dtd"),	// 13
	STAR("source.dtd"),	// 14	
	SECTION_END("keyword.operator.dtd"),	// 15
	PIPE("source.dtd"),	// 16
	PUBLIC("keyword.operator.dtd"),	 // 17
	NMTOKEN("source.dtd"),	// 18
	QUESTION("source.dtd"),	// 19
	PLUS("source.dtd"),	// 20
	SYSTEM("keyword.operator.dtd"),	 // 21
	LBRACKET("keyword.operator.dtd"),	// 22
	COMMA("source.dtd"),	// 23
	PERCENT("keyword.operator.dtd"),	// 24
	EMPTY("keyword.operator.dtd"),	// 25
	ANY("keyword.operator.dtd"),	// 26
	PCDATA("keyword.operator.dtd"),	// 27
	NOTATION_TYPE("keyword.operator.dtd"),	// 28
	INCLUDE("keyword.operator.dtd"),	// 29
	IGNORE("keyword.operator.dtd"),	// 30
	FIXED("keyword.operator.dtd"),	// 31
	NDATA("keyword.operator.dtd"),	// 32
	REQUIRED("keyword.operator.dtd"),	// 33
	IMPLIED("keyword.operator.dtd"),	// 34
	CDATA_TYPE("keyword.operator.dtd"),	// 35
	ID_TYPE("keyword.operator.dtd"),	// 36
	IDREF_TYPE("keyword.operator.dtd"),	// 37
	IDREFS_TYPE("keyword.operator.dtd"),	// 38
	ENTITY_TYPE("keyword.operator.dtd"),	// 39
	ENTITIES_TYPE("keyword.operator.dtd"),	// 40
	NMTOKEN_TYPE("keyword.operator.dtd"),	// 41
	NMTOKENS_TYPE("keyword.operator.dtd"),	// 42	
	
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
