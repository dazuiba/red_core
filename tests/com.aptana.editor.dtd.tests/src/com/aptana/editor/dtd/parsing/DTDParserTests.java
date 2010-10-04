package com.aptana.editor.dtd.parsing;

import java.io.IOException;

import junit.framework.TestCase;
import beaver.Parser.Exception;

import com.aptana.editor.dtd.parsing.ast.DTDElementDeclarationNode;
import com.aptana.editor.dtd.parsing.ast.DTDNodeType;
import com.aptana.parsing.ast.IParseNode;

public class DTDParserTests extends TestCase
{
	/**
	 * testEmptyElement
	 */
	public void testEmptyElement()
	{
		String source = "<!ELEMENT svg EMPTY>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION
		);
		
		DTDElementDeclarationNode elementDecl = (DTDElementDeclarationNode) root.getFirstChild();
		
		assertEquals("svg", elementDecl.getName());
	}
	
	/**
	 * parse
	 * 
	 * @param source
	 * @param types
	 * @throws IOException
	 * @throws Exception
	 */
	protected IParseNode parse(String source, DTDNodeType... types)
	{
		// create parser
		DTDParser parser = new DTDParser();
		
		// create scanner and associate source
		DTDScanner scanner = new DTDScanner();
		scanner.setSource(source);

		// parse it
		IParseNode result = null;
		
		try
		{
			result = (IParseNode) parser.parse(scanner);
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
		
		// make sure we got something
		assertNotNull(result);
		
		// check node types
		for (DTDNodeType type : types)
		{
			IParseNode current = result.getNextNode();
		
			assertNotNull(current);
			assertEquals(type.getIndex(), current.getNodeType());
		}
		
		return result;
	}
}
