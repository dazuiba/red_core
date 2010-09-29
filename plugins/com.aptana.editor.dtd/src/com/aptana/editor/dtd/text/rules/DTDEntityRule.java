package com.aptana.editor.dtd.text.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;

import com.aptana.editor.common.text.rules.ExtendedWordRule;

public class DTDEntityRule extends ExtendedWordRule
{
	private char _startingChararacter;
	
	/**
	 * DTDEntityRule
	 * 
	 * @param firstChar
	 * @param defaultToken
	 */
	public DTDEntityRule(char firstChar, IToken defaultToken)
	{
		super(new DTDEntityDetector(firstChar), defaultToken, true);
		
		this._startingChararacter = firstChar;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.common.text.rules.ExtendedWordRule#wordOK(java.lang.String, org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	@Override
	protected boolean wordOK(String word, ICharacterScanner scanner)
	{
		boolean result = false;
		
		if (word != null && word.length() >= 2)
		{
			result = (word.charAt(0) == this._startingChararacter) && word.charAt(word.length() - 1) == ';';
		}
		
		return result;
	}
}
