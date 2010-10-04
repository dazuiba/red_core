package com.aptana.editor.dtd.parsing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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
	private static class DTDParserScanner extends DTDSourceScanner
	{
		protected IToken createToken(DTDTokenType type)
		{
			return new Token(type);
		}
		
		protected IDocument getDocument()
		{
			return this.fDocument;
		}
	}
	
	private DTDParserScanner _sourceScanner;
	private IDocument _document;
	private Map<String, String> _entities;
	private Stack<DTDParserScanner> _nestedScanners;

	/**
	 * DTDScanner
	 */
	public DTDScanner()
	{
		this._sourceScanner = new DTDParserScanner();
		this._nestedScanners = new Stack<DTDParserScanner>();
	}

	/**
	 * createNestedScanner
	 * 
	 * @param text
	 */
	protected void createNestedScanner(String text)
	{
		DTDParserScanner nestedScanner = new DTDParserScanner();
		IDocument document = new Document(text);

		nestedScanner.setRange(document, 0, document.getLength());

		this._nestedScanners.push(nestedScanner);
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
		DTDParserScanner scanner;
		IDocument document;
		
		if (this._nestedScanners.size() > 0)
		{
			scanner = this._nestedScanners.peek();
			document = scanner.getDocument();
		}
		else
		{
			scanner = this._sourceScanner;
			document = this._document;
		}
		
		int offset = scanner.getTokenOffset();
		int length = scanner.getTokenLength();
		DTDTokenType type = (data == null) ? DTDTokenType.EOF : (DTDTokenType) data;

		try
		{
			int totalLength = document.getLength();

			if (offset > totalLength)
			{
				offset = totalLength;
			}
			if (length == -1)
			{
				length = 0;
			}

			return new Symbol(type.getIndex(), offset, offset + length - 1, document.get(offset, length));
		}
		catch (BadLocationException e)
		{
			throw new Scanner.Exception(e.getLocalizedMessage());
		}
	}

	/**
	 * getToken
	 * 
	 * @return
	 */
	protected IToken getToken()
	{
		IToken token = null;

		while (this._nestedScanners.size() > 0)
		{
			DTDSourceScanner nestedScanner = this._nestedScanners.peek();

			token = nestedScanner.nextToken();
			
			if (token.isWhitespace() == false && token.getData() == null)
			{
				this._nestedScanners.pop();
				token = null;
			}
			else
			{
				break;
			}
		}

		if (token == null)
		{
			token = this._sourceScanner.nextToken();
		}

		return token;
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
		IToken token = this.getToken();
		Object data = token.getData();

		while (token.isWhitespace() || isComment(data))
		{
			token = this.getToken();
			data = token.getData();
		}

		Symbol result = this.createSymbol(data);

		if (data == DTDTokenType.PE_REF)
		{
			// grab key minus the leading '%' and trailing ';'
			String key = (String) result.value;
			key = key.substring(1, key.length() - 1);

			// grab entity's value
			String text = this.getValue(key);

			// create new scanner
			this.createNestedScanner(text);

			result = this.nextToken();
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
			this._entities = new HashMap<String, String>();
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
		this._nestedScanners.clear();
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
