package com.aptana.editor.idl;

import junit.framework.TestCase;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.aptana.editor.idl.parsing.lexer.IDLTokenType;

public class IDLSouceScannerTests extends TestCase
{
	private IDLSourceScanner _scanner;
	
	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		this._scanner = new IDLSourceScanner()
		{
			/* (non-Javadoc)
			 * @see com.aptana.editor.dtd.DTDSourceScanner#createToken(com.aptana.editor.dtd.parsing.lexer.DTDTokenType)
			 */
			@Override
			protected IToken createToken(IDLTokenType type)
			{
				// TODO Auto-generated method stub
				return new Token(type);
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		this._scanner = null;

		super.tearDown();
	}
	
	/**
	 * typeTests
	 * 
	 * @param source
	 * @param types
	 */
	public void typeTests(String source, IDLTokenType... types)
	{
		IDocument document = new Document(source);
		
		this._scanner.setRange(document, 0, source.length());
		
		for (IDLTokenType type : types)
		{
			IToken token = this._scanner.nextToken();
			Object data = token.getData();
			
			assertEquals(type, data);
		}
	}
}
