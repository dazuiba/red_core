package com.aptana.editor.dtd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

import com.aptana.editor.common.text.rules.CharacterMapRule;
import com.aptana.editor.common.text.rules.WhitespaceDetector;
import com.aptana.editor.dtd.parsing.lexer.DTDTokenType;
import com.aptana.editor.dtd.text.rules.DTDEntityDetector;
import com.aptana.editor.dtd.text.rules.DTDNameDetector;
import com.aptana.editor.dtd.text.rules.DTDOperatorDetector;

public class DTDScanner extends RuleBasedScanner
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
			return Character.isLetter(c) || c == '<' || c == '#';
		}

		/*
		 * (non-Javadoc) Method declared on IWordDetector
		 */
		public boolean isWordPart(char c)
		{
			return Character.isLetter(c) || c == '!';
		}
	}
	
	/**
	 * DTDScanner
	 */
	public DTDScanner()
	{
		List<IRule> rules = new ArrayList<IRule>();
		
		rules.add(new WhitespaceRule(new WhitespaceDetector()));
		
		// TODO: comment, string, pubid?
		// TODO: att value, entity value
		
		WordRule operatorRule = new WordRule(new DTDOperatorDetector(), Token.UNDEFINED);
		operatorRule.addWord("<?", createToken(DTDTokenType.PI_START));
		operatorRule.addWord("?>", createToken(DTDTokenType.PI_END));
		operatorRule.addWord(")*", createToken(DTDTokenType.RPAREN_STAR));
		rules.add(operatorRule);
		
		WordRule wordRule = new WordRule(new WordDetector(), Token.UNDEFINED);
		wordRule.addWord("<!ATTLIST", createToken(DTDTokenType.ATTLIST));
		wordRule.addWord("<!ELEMENT", createToken(DTDTokenType.ELEMENT));
		wordRule.addWord("<!ENTITY", createToken(DTDTokenType.ENTITY));
		wordRule.addWord("<!NOTATION", createToken(DTDTokenType.NOTATION));
		wordRule.addWord("#FIXED", createToken(DTDTokenType.FIXED));
		wordRule.addWord("#IMPLIED", createToken(DTDTokenType.IMPLIED));
		wordRule.addWord("#PCDATA", createToken(DTDTokenType.PCDATA));
		wordRule.addWord("#REQUIRED", createToken(DTDTokenType.REQUIRED));
		wordRule.addWord("ANY", createToken(DTDTokenType.ANY));
		wordRule.addWord("CDATA", createToken(DTDTokenType.CDATA_TYPE));
		wordRule.addWord("EMPTY", createToken(DTDTokenType.EMPTY));
		wordRule.addWord("ENTITY", createToken(DTDTokenType.ENTITY));
		wordRule.addWord("ENTITIES", createToken(DTDTokenType.ENTITIES_TYPE));
		wordRule.addWord("ID", createToken(DTDTokenType.ID_TYPE));
		wordRule.addWord("IDREF", createToken(DTDTokenType.IDREF_TYPE));
		wordRule.addWord("IDREFS", createToken(DTDTokenType.IDREFS_TYPE));
		wordRule.addWord("NDATA", createToken(DTDTokenType.NMTOKEN_TYPE));
		wordRule.addWord("NMTOKEN", createToken(DTDTokenType.NMTOKEN_TYPE));
		wordRule.addWord("NMTOKENS", createToken(DTDTokenType.NMTOKENS_TYPE));
		wordRule.addWord("NOTATION", createToken(DTDTokenType.NOTATION_TYPE));
		wordRule.addWord("PUBLIC", createToken(DTDTokenType.PUBLIC));
		wordRule.addWord("SYSTEM", createToken(DTDTokenType.SYSTEM));
		rules.add(wordRule);
		
		// PERef
		rules.add(new WordRule(new DTDEntityDetector('%'), createToken(DTDTokenType.PE_REF)));
		
		CharacterMapRule cmRule = new CharacterMapRule();
		cmRule.add('>', createToken(DTDTokenType.GREATER_THAN));
		cmRule.add('(', createToken(DTDTokenType.LPAREN));
		cmRule.add('|', createToken(DTDTokenType.PIPE));
		cmRule.add(')', createToken(DTDTokenType.RPAREN));
		cmRule.add('?', createToken(DTDTokenType.QUESTION));
		cmRule.add('*', createToken(DTDTokenType.STAR));
		cmRule.add('+', createToken(DTDTokenType.PLUS));
		cmRule.add(',', createToken(DTDTokenType.COMMA));
		cmRule.add('%', createToken(DTDTokenType.PERCENT));
		rules.add(cmRule);
		
		// Name
		rules.add(new WordRule(new DTDNameDetector(), createToken(DTDTokenType.NAME)));
		
		// EntityRef
		rules.add(new WordRule(new DTDEntityDetector('&'), createToken(DTDTokenType.ENTITY_REF)));

		this.setRules(rules.toArray(new IRule[rules.size()]));
		//this.setDefaultReturnToken(this.createToken("text"));
	}

	/**
	 * createToken
	 * 
	 * @param string
	 * @return
	 */
	protected IToken createToken(DTDTokenType type)
	{
		return new Token(type.getScope());
	}
}
