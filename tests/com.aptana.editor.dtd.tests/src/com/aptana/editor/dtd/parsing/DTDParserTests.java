package com.aptana.editor.dtd.parsing;

import java.io.IOException;

import junit.framework.TestCase;
import beaver.Parser.Exception;

import com.aptana.editor.dtd.parsing.ast.DTDElementDeclNode;
import com.aptana.editor.dtd.parsing.ast.DTDElementNode;
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
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.EMPTY
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		
		assertEquals("svg", elementDecl.getName());
	}
	
	/**
	 * testAnyElement
	 */
	public void testAnyElement()
	{
		String source = "<!ELEMENT svg ANY>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.ANY
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		
		assertEquals("svg", elementDecl.getName());
	}
	
	/**
	 * testPCDataElement
	 */
	public void testPCDataElement()
	{
		String source = "<!ELEMENT svg (#PCDATA)>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.PCDATA
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		
		assertEquals("svg", elementDecl.getName());
	}
	
	/**
	 * testZeroOrMorePCDataElement
	 */
	public void testZeroOrMorePCDataElement()
	{
		String source = "<!ELEMENT svg (#PCDATA)*>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.ZERO_OR_MORE,
			DTDNodeType.PCDATA
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		
		assertEquals("svg", elementDecl.getName());
	}
	
	/**
	 * testZeroOrMorePCDataAndNamesElement
	 */
	public void testZeroOrMorePCDataAndNamesElement()
	{
		String source = "<!ELEMENT svg (#PCDATA circle)*>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.ZERO_OR_MORE,
			DTDNodeType.PCDATA,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode kleene = elementDecl.getFirstChild();
		assertNotNull(kleene);
		
		IParseNode element = kleene.getLastChild();
		assertNotNull(element);
		assertTrue(element instanceof DTDElementNode);
		
		assertEquals("circle", ((DTDElementNode) element).getName());
	}
	
	/**
	 * testSingleChild
	 */
	public void testSingleChild()
	{
		String source = "<!ELEMENT svg (circle)>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode element = elementDecl.getFirstChild();
		assertNotNull(element);
		assertTrue(element instanceof DTDElementNode);
		
		assertEquals("circle", ((DTDElementNode) element).getName());
	}
	
	/**
	 * testZeroOrMoreSingleChild
	 */
	public void testZeroOrMoreSingleChild()
	{
		String source = "<!ELEMENT svg (circle)*>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.ZERO_OR_MORE,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode kleene = elementDecl.getFirstChild();
		assertNotNull(kleene);
		
		IParseNode element = kleene.getFirstChild();
		assertNotNull(element);
		assertTrue(element instanceof DTDElementNode);
		
		assertEquals("circle", ((DTDElementNode) element).getName());
	}
	
	/**
	 * testOneOrMoreSingleChild
	 */
	public void testOneOrMoreSingleChild()
	{
		String source = "<!ELEMENT svg (circle)+>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.ONE_OR_MORE,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode positive = elementDecl.getFirstChild();
		assertNotNull(positive);
		
		IParseNode element = positive.getFirstChild();
		assertNotNull(element);
		assertTrue(element instanceof DTDElementNode);
		
		assertEquals("circle", ((DTDElementNode) element).getName());
	}
	
	/**
	 * testOptionalSingleChild
	 */
	public void testOptionalSingleChild()
	{
		String source = "<!ELEMENT svg (circle)?>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.OPTIONAL,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode optional = elementDecl.getFirstChild();
		assertNotNull(optional);
		
		IParseNode element = optional.getFirstChild();
		assertNotNull(element);
		assertTrue(element instanceof DTDElementNode);
		
		assertEquals("circle", ((DTDElementNode) element).getName());
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
		
		IParseNode current = result;
		
		// check node types
		for (DTDNodeType type : types)
		{
			current = current.getNextNode();
		
			assertNotNull(current);
			assertEquals(type.getIndex(), current.getNodeType());
		}
		
		return result;
	}
}
