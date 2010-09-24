package com.aptana.editor.idl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

import com.aptana.editor.common.text.rules.CharacterMapRule;
import com.aptana.editor.common.text.rules.WhitespaceDetector;
import com.aptana.editor.ide.text.rules.IDLNumberRule;
import com.aptana.editor.idl.parsing.lexer.IDLTokenType;

public class IDLScanner extends RuleBasedScanner
{
	/**
	 * A key word detector.
	 */
	static class WordDetector implements IWordDetector
	{
		/*
		 * (non-Javadoc) Method declared on IWordDetector
		 */
		public boolean isWordStart(char c)
		{
			return Character.isLetter(c) || '_' == c;
		}

		/*
		 * (non-Javadoc) Method declared on IWordDetector
		 */
		public boolean isWordPart(char c)
		{
			return Character.isLetterOrDigit(c) || '_' == c;
		}
	}
	
	/**
	 * DTDScanner
	 */
	public IDLScanner()
	{
		List<IRule> rules = new ArrayList<IRule>();
		
		rules.add(new WhitespaceRule(new WhitespaceDetector()));
		
		rules.add(new SingleLineRule("\"", "\"", createToken(IDLTokenType.STRING)));
		
		// TODO: rule for other
		// TODO: rule for ::
		
		WordRule wordRule = new WordRule(new WordDetector(), createToken(IDLTokenType.IDENTIFIER));
		wordRule.addWord("any", createToken(IDLTokenType.ANY));
		wordRule.addWord("attribute", createToken(IDLTokenType.ATTRIBUTE));
		wordRule.addWord("boolean", createToken(IDLTokenType.BOOLEAN));
		wordRule.addWord("const", createToken(IDLTokenType.CONST));
		wordRule.addWord("DOMString", createToken(IDLTokenType.DOMSTRING));
		wordRule.addWord("exception", createToken(IDLTokenType.EXCEPTION));
		wordRule.addWord("false", createToken(IDLTokenType.FALSE));
		wordRule.addWord("float", createToken(IDLTokenType.FLOAT));
		wordRule.addWord("getraises", createToken(IDLTokenType.GETRAISES));
		wordRule.addWord("in", createToken(IDLTokenType.IN));
		wordRule.addWord("interface", createToken(IDLTokenType.INTERFACE));
		wordRule.addWord("long", createToken(IDLTokenType.LONG));
		wordRule.addWord("module", createToken(IDLTokenType.MODULE));
		wordRule.addWord("Object", createToken(IDLTokenType.OBJECT));
		wordRule.addWord("octet", createToken(IDLTokenType.OCTET));
		wordRule.addWord("raises", createToken(IDLTokenType.RAISES));
		wordRule.addWord("readonly", createToken(IDLTokenType.READONLY));
		wordRule.addWord("sequence", createToken(IDLTokenType.SEQUENCE));
		wordRule.addWord("setraises", createToken(IDLTokenType.SETRAISES));
		wordRule.addWord("short", createToken(IDLTokenType.SHORT));
		wordRule.addWord("true", createToken(IDLTokenType.TRUE));
		wordRule.addWord("typedef", createToken(IDLTokenType.TYPEDEF));
		wordRule.addWord("unsigned", createToken(IDLTokenType.UNSIGNED));
		wordRule.addWord("valuetype", createToken(IDLTokenType.VALUETYPE));
		wordRule.addWord("void", createToken(IDLTokenType.VOID));
		rules.add(wordRule);
		
		// single-character operators and punctuation
		CharacterMapRule cmRule = new CharacterMapRule();
		cmRule.add('{', createToken(IDLTokenType.LCURLY));
		cmRule.add('}', createToken(IDLTokenType.RCURLY));
		cmRule.add(';', createToken(IDLTokenType.SEMICOLON));
		cmRule.add(':', createToken(IDLTokenType.COLON));
		cmRule.add('<', createToken(IDLTokenType.LESS_THAN));
		cmRule.add('>', createToken(IDLTokenType.GREATER_THAN));
		cmRule.add('=', createToken(IDLTokenType.EQUAL));
		cmRule.add('(', createToken(IDLTokenType.LPAREN));
		cmRule.add(')', createToken(IDLTokenType.RPAREN));
		cmRule.add(',', createToken(IDLTokenType.COMMA));
		cmRule.add('[', createToken(IDLTokenType.LBRACKET));
		cmRule.add(']', createToken(IDLTokenType.RBRACKET));
		rules.add(cmRule);
		
		// numbers
		// TODO: rule for integer
		rules.add(new IDLNumberRule(createToken(IDLTokenType.NUM_FLOAT)));

		this.setRules(rules.toArray(new IRule[rules.size()]));
		//this.setDefaultReturnToken(this.createToken("text"));
	}

	/**
	 * createToken
	 * 
	 * @param string
	 * @return
	 */
	protected IToken createToken(IDLTokenType type)
	{
		return new Token(type.getScope());
	}
}
