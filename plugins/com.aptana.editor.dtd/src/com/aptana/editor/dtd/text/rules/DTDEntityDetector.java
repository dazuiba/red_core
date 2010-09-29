package com.aptana.editor.dtd.text.rules;

import org.eclipse.jface.text.rules.IWordDetector;

public class DTDEntityDetector implements IWordDetector
{
	private DTDNameDetector _detector = new DTDNameDetector();
	private int _index;
	private char _startingChararacter;
	private boolean _done;
	
	/**
	 * DTDEntityDetector
	 * 
	 * @param startingCharacter
	 */
	public DTDEntityDetector(char startingCharacter)
	{
		this._startingChararacter = startingCharacter;
		this._done = false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c)
	{
		boolean result = false;
		
		this._index++;
		
		if (this._done == false)
		{
			if (this._index == 1)
			{
				result = this._detector.isWordStart(c);
			}
			else if (c == ';')
			{
				this._done = true;

				result = true;
			}
			else
			{
				result = this._detector.isWordPart(c);
			}
		}
		
		return result;
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
