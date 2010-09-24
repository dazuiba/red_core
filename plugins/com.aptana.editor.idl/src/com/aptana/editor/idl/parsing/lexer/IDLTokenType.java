package com.aptana.editor.idl.parsing.lexer;

import java.util.EnumSet;

public enum IDLTokenType
{
	UNDEFINED("source.idl"), // -1
	EOF("source.idl"), // 0
	
	SEMICOLON("punctuation.terminator.statement.idl"), // 1
	IDENTIFIER("source.idl"), // 2
	DOUBLE_COLON("keyword.operator.idl"), // 3
	DOMSTRING("keyword.operator.idl"), // 4
	SHORT("keyword.operator.idl"), // 5
	LONG("keyword.operator.idl"), // 6
	UNSIGNED("keyword.operator.idl"), // 7
	LBRACKET("meta.brace.square.idl"), // 8
	BOOLEAN("keyword.operator.idl"), // 9
	OCTET("keyword.operator.idl"), // 10
	FLOAT("keyword.operator.idl"), // 11
	ANY("keyword.operator.idl"), // 12
	OBJECT("keyword.operator.idl"), // 13
	SEQUENCE("keyword.operator.idl"), // 14
	LCURLY("meta.brace.curly.idl"), // 15
	LPAREN("meta.brace.round.idl"), // 16
	LESS_THAN("keyword.operator.idl"), // 17
	GREATER_THAN("keyword.operator.idl"), // 18
	ATTRIBUTE("keyword.operator.idl"), // 19
	IN("keyword.operator.idl"), // 20
	CONST("keyword.operator.idl"), // 21
	MODULE("keyword.operator.idl"), // 22
	EXCEPTION("keyword.operator.idl"), // 23
	EQUAL("keyword.operator.idl"), // 24
	INTERFACE("keyword.operator.idl"), // 25
	TYPEDEF("keyword.operator.idl"), // 26
	VALUETYPE("keyword.operator.idl"), // 27
	NUM_INTEGER("constant.numeric.js"), // 28
	NUM_FLOAT("constant.numeric.js"), // 29
	COLON("keyword.operator.idl"), // 30
	TRUE("constant.language.boolean.true.idl"), // 31
	FALSE("constant.language.boolean.false.idl"), // 32
	GETRAISES("keyword.operator.idl"), // 33
	RAISES("keyword.operator.idl"), // 34
	SETRAISES("keyword.operator.idl"), // 35
	COMMA("meta.delimiter.object.comma.idl"), // 36
	STRING("source.idl"), // 37
	OTHER("source.idl"), // 38
	RCURLY("meta.brace.curly.idl"), // 39
	RPAREN("meta.brace.round.idl"), // 40
	RBRACKET("meta.brace.square.idl"), // 41
	READONLY("keyword.operator.idl"), // 42
	VOID("constant.language.void.idl"), // 43
	
	OMITTABLE("keyword.operator.idl"),
	DOUBLE("keyword.operator.idl"),
	QUESTION("keyword.operator.idl"),
	ELLIPSIS("keyword.operator.idl"),
	OPTIONAL("keyword.operator.idl"),
	GETTER("keyword.operator.idl"),
	SETTER("keyword.operator.idl"),
	CREATOR("keyword.operator.idl"),
	DELETER("keyword.operator.idl"),
	CALLER("keyword.operator.idl"),
	STRINGIFIER("keyword.operator.idl"),
	IMPLEMENTS("keyword.operator.idl");

	private short _index;
	private String _scope;
	
	/**
	 * static initializer
	 */
	static
	{
		short index = -1;
		
		for (IDLTokenType type : EnumSet.allOf(IDLTokenType.class))
		{
			type._index = index++;
		}
	}
	
	/**
	 * IDLTokenType
	 * 
	 * @param scope
	 */
	private IDLTokenType(String scope)
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
