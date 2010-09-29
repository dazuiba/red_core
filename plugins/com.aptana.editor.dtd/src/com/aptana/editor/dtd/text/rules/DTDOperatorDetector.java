package com.aptana.editor.dtd.text.rules;

import org.eclipse.jface.text.rules.IWordDetector;

public class DTDOperatorDetector implements IWordDetector
{
	private int _position;

	@Override
	public boolean isWordPart(char c)
	{
		this._position++;

		if (this._position == 1)
		{
			switch (c)
			{
				case '*':
					return true;
					
				default:
					return false;
			}
		}
		
		return false;
	}

	@Override
	public boolean isWordStart(char c)
	{
		this._position = 0;

		switch (c)
		{
			case ')':
				return true;

			default:
				return false;
		}
	}

}
