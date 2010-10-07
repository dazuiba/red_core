package com.aptana.editor.xml.contentassist.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aptana.editor.dtd.parsing.DTDParserConstants;
import com.aptana.editor.dtd.parsing.ast.DTDAttListDeclNode;
import com.aptana.editor.dtd.parsing.ast.DTDAttributeNode;
import com.aptana.editor.dtd.parsing.ast.DTDElementDeclNode;
import com.aptana.editor.dtd.parsing.ast.DTDParseRootNode;
import com.aptana.editor.dtd.parsing.ast.DTDTreeWalker;
import com.aptana.parsing.IParser;
import com.aptana.parsing.IParserPool;
import com.aptana.parsing.ParseState;
import com.aptana.parsing.ParserPoolFactory;
import com.aptana.parsing.ast.IParseNode;
import com.aptana.parsing.ast.IParseRootNode;

public class DTDTransformer
{
	private class ElementCollector extends DTDTreeWalker
	{
		private Map<String, ElementElement> _elementMap = new HashMap<String, ElementElement>();

		public List<ElementElement> getElements()
		{
			return new ArrayList<ElementElement>(this._elementMap.values());
		}

		public void visit(DTDElementDeclNode node)
		{
			String elementName = node.getName();

			if (this._elementMap.containsKey(elementName) == false)
			{
				ElementElement element = new ElementElement();

				element.setName(elementName);

				this._elementMap.put(elementName, element);
			}
		}
		
		public void visit(DTDAttributeNode node)
		{
			IParseNode parent = node.getParent();
			
			if (parent instanceof DTDAttListDeclNode)
			{
				String elementName = ((DTDAttListDeclNode) parent).getName();
				ElementElement element = this._elementMap.get(elementName);
				
				if (element != null)
				{
					String attributeName = node.getName();
					AttributeElement attribute = new AttributeElement();
					
					attribute.setName(attributeName);
					attribute.setElement(elementName);
					
					element.addAttribute(attribute);
				}
			}
		}
	}

	/**
	 * DTDTransfomer
	 */
	public DTDTransformer()
	{
	}

	/**
	 * transform
	 */
	public List<ElementElement> transform(String source)
	{
		IParseRootNode root = this.parse(source);
		List<ElementElement> result = Collections.emptyList();

		if (root instanceof DTDParseRootNode)
		{
			ElementCollector collector = new ElementCollector();

			collector.visit((DTDParseRootNode) root);

			result = collector.getElements();
		}

		return result;
	}

	/**
	 * parser
	 * 
	 * @param source
	 * @return
	 */
	protected IParseRootNode parse(String source)
	{
		IParseRootNode result = null;

		// create parser and associated parse state
		IParserPool pool = ParserPoolFactory.getInstance().getParserPool(DTDParserConstants.LANGUAGE);

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
