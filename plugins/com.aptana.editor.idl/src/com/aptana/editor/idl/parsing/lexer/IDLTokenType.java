package com.aptana.editor.idl.parsing.lexer;

import java.util.EnumSet;

public enum IDLTokenType
{
	UNDEFINED("source.idl"), // -1
	EOF("source.idl"), // 0
	
	IDENTIFIER("source.idl"), // 1
	SEMICOLON("punctuation.terminator.statement.idl"), // 2
	DOUBLE_COLON("keyword.operator.idl"), // 3
	LONG("keyword.operator.idl"), // 4
	LBRACKET("meta.brace.square.idl"), // 5
	SHORT("keyword.operator.idl"), // 6
	SEQUENCE("keyword.operator.idl"), // 7
	OBJECT("keyword.operator.idl"), // 8
	ANY("keyword.operator.idl"), // 9
	BOOLEAN("keyword.operator.idl"), // 10
	DOUBLE("keyword.operator.idl"), // 11
	FLOAT("keyword.operator.idl"), // 12
	OCTET("keyword.operator.idl"), // 13
	DOMSTRING("keyword.operator.idl"), // 14
	UNSIGNED("keyword.operator.idl"), // 15
	QUESTION("keyword.operator.idl"), // 16
	LPAREN("meta.brace.round.idl"), // 17
	LCURLY("meta.brace.curly.idl"), // 18
	ATTRIBUTE("keyword.operator.idl"), // 19
	GETTER("keyword.operator.idl"), // 20
	SETTER("keyword.operator.idl"), // 21
	CREATOR("keyword.operator.idl"), // 22
	DELETER("keyword.operator.idl"), // 23
	CALLER("keyword.operator.idl"), // 24
	CONST("keyword.operator.idl"), // 25
	VOID("constant.language.void.idl"), // 26
	MODULE("keyword.operator.idl"), // 27
	INTERFACE("keyword.operator.idl"), // 28
	EXCEPTION("keyword.operator.idl"), // 29
	EQUAL("keyword.operator.idl"), // 30
	TYPEDEF("keyword.operator.idl"), // 31
	LESS_THAN("keyword.operator.idl"), // 32
	GREATER_THAN("keyword.operator.idl"), // 33
	IMPLEMENTS("keyword.operator.idl"), // 34
	COLON("keyword.operator.idl"), // 35
	NUM_INTEGER("constant.numeric.js"), // 36
	NUM_FLOAT("constant.numeric.js"), // 37
	TRUE("constant.language.boolean.true.idl"), // 38
	FALSE("constant.language.boolean.false.idl"), // 39
	STRINGIFIER("keyword.operator.idl"), // 40
	GETRAISES("keyword.operator.idl"), // 41
	SETRAISES("keyword.operator.idl"), // 42
	OMITTABLE("keyword.operator.idl"), // 43
	RAISES("keyword.operator.idl"), // 44
	IN("keyword.operator.idl"), // 45
	OPTIONAL("keyword.operator.idl"), // 46
	ELLIPSIS("keyword.operator.idl"), // 47
	COMMA("meta.delimiter.object.comma.idl"), // 48
	STRING("source.idl"), // 49
	OTHER("source.idl"), // 50
	RCURLY("meta.brace.curly.idl"), // 51
	RPAREN("meta.brace.round.idl"), // 52
	RBRACKET("meta.brace.square.idl"), // 53
	READONLY("keyword.operator.idl"); // 54

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
