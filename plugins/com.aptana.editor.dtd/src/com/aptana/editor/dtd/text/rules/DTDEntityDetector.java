package com.aptana.editor.dtd.text.rules;

import org.eclipse.jface.text.rules.IWordDetector;

public class DTDEntityDetector implements IWordDetector
{
	private DTDNameDetector _detector = new DTDNameDetector();
	private int _index;
	private char _startingChararacter;
	
	/**
	 * DTDEntityDetector
	 * 
	 * @param startingCharacter
	 */
	public DTDEntityDetector(char startingCharacter)
	{
		this._startingChararacter = startingCharacter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c)
	{
		this._index++;
		
		if (this._index == 1)
		{
			return this._detector.isWordStart(c);
		}
		else
		{
			return this._detector.isWordPart(c) || c == ';';
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	@Override
	public boolean isWordStart(char c)
	{
		this._index = 0;
		
		return c == this._startingChararacter;
	}
}
