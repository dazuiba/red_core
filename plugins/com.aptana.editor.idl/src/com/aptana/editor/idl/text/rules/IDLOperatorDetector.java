package com.aptana.editor.idl.text.rules;

import org.eclipse.jface.text.rules.IWordDetector;

public class IDLOperatorDetector implements IWordDetector
{
	private int _position;

	@Override
	public boolean isWordPart(char c)
	{
		this._position++;

		switch (this._position)
		{
			case 1:
				switch (c)
				{
					case ':':
					case '.':
						return true;
						
					default:
						return false;
				}

			case 2:
				return (c == '.');

			default:
				return false;
		}
	}

	@Override
	public boolean isWordStart(char c)
	{
		this._position = 0;

		switch (c)
		{
			case ':':
			case '.':
				return true;

			default:
				return false;
		}
	}

}
