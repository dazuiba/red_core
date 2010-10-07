package com.aptana.editor.xml.contentassist.model;

import java.util.HashSet;
import java.util.Set;

import com.aptana.editor.dtd.parsing.ast.DTDElementNode;
import com.aptana.editor.dtd.parsing.ast.DTDParseRootNode;
import com.aptana.editor.dtd.parsing.ast.DTDTreeWalker;
import com.aptana.parsing.IParser;
import com.aptana.parsing.IParserPool;
import com.aptana.parsing.ParseState;
import com.aptana.parsing.ParserPoolFactory;
import com.aptana.parsing.ast.IParseRootNode;

public class DTDTransformer
{
	private class ElementCollector extends DTDTreeWalker
	{
		private Set<String> _elements = new HashSet<String>();
		
		public Set<String> getElements()
		{
			return this._elements;
		}
		
		public void visit(DTDElementNode node)
		{
			this.visitChildren(node);
		}
	}
	
	private String _language;
	
	/**
	 * DTDTransfomer
	 * 
	 * @param language
	 */
	public DTDTransformer(String language)
	{
		this._language = language;
	}
	
	/**
	 * transform
	 */
	public void transform(String source)
	{
		IParseRootNode root = this.parse(source);
		
		if (root instanceof DTDParseRootNode)
		{
			ElementCollector collector = new ElementCollector();
		
			collector.visit((DTDParseRootNode) root);
			
			for (String element : collector.getElements())
			{
				System.out.println(element);
			}
		}
	}
	
	protected IParseRootNode parse(String source)
	{
		IParseRootNode result = null;
		
		// create parser and associated parse state
		IParserPool pool = ParserPoolFactory.getInstance().getParserPool(this._language);

		if (pool != null)
		{
			IParser parser = pool.checkOut();

			// apply the source to the parse state and parse
			ParseState parseState = new ParseState();
			parseState.setEditState(source, null, 0, 0);
			try
			{
				result = parser.parse(parseState);
			}
			catch (Exception e)
			{
			}
			finally
			{
				pool.checkIn(parser);
			}
		}
		
		return result;
	}
}
