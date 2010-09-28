package com.aptana.editor.dtd.text.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;

import com.aptana.editor.common.text.rules.ExtendedWordRule;

public class DTDEntityRule extends ExtendedWordRule
{

	public DTDEntityRule(IWordDetector detector, IToken defaultToken, boolean ignoreCase)
	{
		super(new DTDEntityDetector('&'), defaultToken, false);
	}

	@Override
	protected boolean wordOK(String word, ICharacterScanner scanner)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
