package com.aptana.editor.dtd.parsing;

import java.io.IOException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import beaver.Scanner;
import beaver.Symbol;

import com.aptana.editor.dtd.DTDSourceScanner;
import com.aptana.editor.dtd.parsing.lexer.DTDTokenType;

public class DTDScanner extends Scanner
{
	private DTDSourceScanner _sourceScanner;
	private IDocument _document;

	/**
	 * DTDScanner
	 */
	public DTDScanner()
	{
		this._sourceScanner = new DTDSourceScanner()
		{
			/* (non-Javadoc)
			 * @see com.aptana.editor.dtd.DTDSourceScanner#createToken(com.aptana.editor.dtd.parsing.lexer.DTDTokenType)
			 */
			@Override
			protected IToken createToken(DTDTokenType type)
			{
				// TODO Auto-generated method stub
				return new Token(type);
			}
		};
	}
	
	/**
	 * createSymbol
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	protected Symbol createSymbol(Object data) throws Exception
	{
		int offset = this._sourceScanner.getTokenOffset();
		int length = this._sourceScanner.getTokenLength();
		DTDTokenType type = (data == null) ? DTDTokenType.EOF : (DTDTokenType) data;

		try
		{
			int totalLength = this._document.getLength();

			if (offset > totalLength)
			{
				offset = totalLength;
			}
			if (length == -1)
			{
				length = 0;
			}

			return new Symbol(type.getIndex(), offset, offset + length - 1, this._document.get(offset, length));
		}
		catch (BadLocationException e)
		{
			throw new Scanner.Exception(e.getLocalizedMessage());
		}
	}

	/**
	 * isComment
	 * 
	 * @param data
	 * @return
	 */
	protected boolean isComment(Object data)
	{
		return (data != null && ((DTDTokenType) data) == DTDTokenType.COMMENT);
	}
	
	/*
	 * (non-Javadoc)
	 * @see beaver.Scanner#nextToken()
	 */
	@Override
	public Symbol nextToken() throws IOException, Exception
	{
		IToken token = this._sourceScanner.nextToken();
		Object data = token.getData();

		while (token.isWhitespace() || isComment(data))
		{
			token = this._sourceScanner.nextToken();
			data = token.getData();
		}

		return this.createSymbol(data);
	}

	/**
	 * setSource
	 * 
	 * @param document
	 */
	public void setSource(IDocument document)
	{
		this._document = document;
		this._sourceScanner.setRange(document, 0, document.getLength());
	}

	/**
	 * setSource
	 * 
	 * @param text
	 */
	public void setSource(String text)
	{
		setSource(new Document(text));
	}
}
