package com.aptana.editor.dtd.parsing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	private Map<String,String> _entities;

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
	 * getValue
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key)
	{
		String result = null;
		
		if (this._entities != null)
		{
			result = this._entities.get(key);
		}
		
		return result;
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
		
		Symbol result = this.createSymbol(data);
		
		if (data == DTDTokenType.PE_REF)
		{
			// grab key
			String key = (String) result.value;
			
			// grab content
			key = key.substring(1, key.length() - 1);
			
			// grab value
			String value = this.getValue(key);
			
			System.out.println("PERef: " + key + " = " + value);
		}

		return result;
	}

	/**
	 * register
	 * 
	 * @param key
	 * @param value
	 */
	public void register(String key, String value)
	{
		if (this._entities == null)
		{
			this._entities = new HashMap<String,String>();
		}
		
		this._entities.put(key, value);
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
