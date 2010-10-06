package com.aptana.editor.dtd.parsing;

import java.io.IOException;

import junit.framework.TestCase;
import beaver.Parser.Exception;

import com.aptana.editor.dtd.parsing.ast.DTDAndExpressionNode;
import com.aptana.editor.dtd.parsing.ast.DTDAttListDeclNode;
import com.aptana.editor.dtd.parsing.ast.DTDAttributeNode;
import com.aptana.editor.dtd.parsing.ast.DTDElementDeclNode;
import com.aptana.editor.dtd.parsing.ast.DTDElementNode;
import com.aptana.editor.dtd.parsing.ast.DTDEnumerationTypeNode;
import com.aptana.editor.dtd.parsing.ast.DTDNodeType;
import com.aptana.editor.dtd.parsing.ast.DTDNotationTypeNode;
import com.aptana.editor.dtd.parsing.ast.DTDOneOrMoreNode;
import com.aptana.editor.dtd.parsing.ast.DTDOptionalNode;
import com.aptana.editor.dtd.parsing.ast.DTDOrExpressionNode;
import com.aptana.editor.dtd.parsing.ast.DTDTypeNode;
import com.aptana.editor.dtd.parsing.ast.DTDZeroOrMoreNode;
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
	 * testOrExpression
	 */
	public void testOrExpression()
	{
		String source = "<!ELEMENT svg (circle | ellipse)>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.OR_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode orExpr = elementDecl.getFirstChild();
		assertTrue(orExpr instanceof DTDOrExpressionNode);
		assertEquals(2, orExpr.getChildCount());
		
		IParseNode circle = orExpr.getFirstChild();
		assertTrue(circle instanceof DTDElementNode);
		assertEquals("circle", ((DTDElementNode) circle).getName());
		
		IParseNode ellipse = orExpr.getLastChild();
		assertTrue(ellipse instanceof DTDElementNode);
		assertEquals("ellipse", ((DTDElementNode) ellipse).getName());
	}
	
	/**
	 * testZeroOrMoreOrExpression
	 */
	public void testZeroOrMoreOrExpression()
	{
		String source = "<!ELEMENT svg (circle | ellipse)*>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.ZERO_OR_MORE,
			DTDNodeType.OR_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode zom = elementDecl.getFirstChild();
		assertTrue(zom instanceof DTDZeroOrMoreNode);
		
		IParseNode orExpr = zom.getFirstChild();
		assertTrue(orExpr instanceof DTDOrExpressionNode);
		assertEquals(2, orExpr.getChildCount());
		
		IParseNode circle = orExpr.getFirstChild();
		assertTrue(circle instanceof DTDElementNode);
		assertEquals("circle", ((DTDElementNode) circle).getName());
		
		IParseNode ellipse = orExpr.getLastChild();
		assertTrue(ellipse instanceof DTDElementNode);
		assertEquals("ellipse", ((DTDElementNode) ellipse).getName());
	}
	
	/**
	 * testOneOrMoreOrExpression
	 */
	public void testOneOrMoreOrExpression()
	{
		String source = "<!ELEMENT svg (circle | ellipse)+>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.ONE_OR_MORE,
			DTDNodeType.OR_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode oom = elementDecl.getFirstChild();
		assertTrue(oom instanceof DTDOneOrMoreNode);
		
		IParseNode orExpr = oom.getFirstChild();
		assertTrue(orExpr instanceof DTDOrExpressionNode);
		assertEquals(2, orExpr.getChildCount());
		
		IParseNode circle = orExpr.getFirstChild();
		assertTrue(circle instanceof DTDElementNode);
		assertEquals("circle", ((DTDElementNode) circle).getName());
		
		IParseNode ellipse = orExpr.getLastChild();
		assertTrue(ellipse instanceof DTDElementNode);
		assertEquals("ellipse", ((DTDElementNode) ellipse).getName());
	}
	
	/**
	 * testOptionalOrExpression
	 */
	public void testOptionalOrExpression()
	{
		String source = "<!ELEMENT svg (circle | ellipse)?>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.OPTIONAL,
			DTDNodeType.OR_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode opt = elementDecl.getFirstChild();
		assertTrue(opt instanceof DTDOptionalNode);
		
		IParseNode orExpr = opt.getFirstChild();
		assertTrue(orExpr instanceof DTDOrExpressionNode);
		assertEquals(2, orExpr.getChildCount());
		
		IParseNode circle = orExpr.getFirstChild();
		assertTrue(circle instanceof DTDElementNode);
		assertEquals("circle", ((DTDElementNode) circle).getName());
		
		IParseNode ellipse = orExpr.getLastChild();
		assertTrue(ellipse instanceof DTDElementNode);
		assertEquals("ellipse", ((DTDElementNode) ellipse).getName());
	}
	
	/**
	 * testAndExpression
	 */
	public void testAndExpression()
	{
		String source = "<!ELEMENT svg (circle, ellipse)>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.AND_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode andExpr = elementDecl.getFirstChild();
		assertTrue(andExpr instanceof DTDAndExpressionNode);
		assertEquals(2, andExpr.getChildCount());
		
		IParseNode circle = andExpr.getFirstChild();
		assertTrue(circle instanceof DTDElementNode);
		assertEquals("circle", ((DTDElementNode) circle).getName());
		
		IParseNode ellipse = andExpr.getLastChild();
		assertTrue(ellipse instanceof DTDElementNode);
		assertEquals("ellipse", ((DTDElementNode) ellipse).getName());
	}
	
	/**
	 * testZeroOrMoreAndExpression
	 */
	public void testZeroOrMoreAndExpression()
	{
		String source = "<!ELEMENT svg (circle, ellipse)*>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.ZERO_OR_MORE,
			DTDNodeType.AND_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode zom = elementDecl.getFirstChild();
		assertTrue(zom instanceof DTDZeroOrMoreNode);
		
		IParseNode andExpr = zom.getFirstChild();
		assertTrue(andExpr instanceof DTDAndExpressionNode);
		assertEquals(2, andExpr.getChildCount());
		
		IParseNode circle = andExpr.getFirstChild();
		assertTrue(circle instanceof DTDElementNode);
		assertEquals("circle", ((DTDElementNode) circle).getName());
		
		IParseNode ellipse = andExpr.getLastChild();
		assertTrue(ellipse instanceof DTDElementNode);
		assertEquals("ellipse", ((DTDElementNode) ellipse).getName());
	}
	
	/**
	 * testOneOrMoreAndExpression
	 */
	public void testOneOrMoreAndExpression()
	{
		String source = "<!ELEMENT svg (circle, ellipse)+>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.ONE_OR_MORE,
			DTDNodeType.AND_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode oom = elementDecl.getFirstChild();
		assertTrue(oom instanceof DTDOneOrMoreNode);
		
		IParseNode andExpr = oom.getFirstChild();
		assertTrue(andExpr instanceof DTDAndExpressionNode);
		assertEquals(2, andExpr.getChildCount());
		
		IParseNode circle = andExpr.getFirstChild();
		assertTrue(circle instanceof DTDElementNode);
		assertEquals("circle", ((DTDElementNode) circle).getName());
		
		IParseNode ellipse = andExpr.getLastChild();
		assertTrue(ellipse instanceof DTDElementNode);
		assertEquals("ellipse", ((DTDElementNode) ellipse).getName());
	}
	
	/**
	 * testOptionalAndExpression
	 */
	public void testOptionalAndExpression()
	{
		String source = "<!ELEMENT svg (circle, ellipse)?>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.OPTIONAL,
			DTDNodeType.AND_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
		
		DTDElementDeclNode elementDecl = (DTDElementDeclNode) root.getFirstChild();
		assertEquals("svg", elementDecl.getName());
		
		IParseNode opt = elementDecl.getFirstChild();
		assertTrue(opt instanceof DTDOptionalNode);
		
		IParseNode andExpr = opt.getFirstChild();
		assertTrue(andExpr instanceof DTDAndExpressionNode);
		assertEquals(2, andExpr.getChildCount());
		
		IParseNode circle = andExpr.getFirstChild();
		assertTrue(circle instanceof DTDElementNode);
		assertEquals("circle", ((DTDElementNode) circle).getName());
		
		IParseNode ellipse = andExpr.getLastChild();
		assertTrue(ellipse instanceof DTDElementNode);
		assertEquals("ellipse", ((DTDElementNode) ellipse).getName());
	}
	
	/**
	 * testNestedAndExpression
	 */
	public void testNestedAndExpression()
	{
		String source = "<!ELEMENT svg ((circle, ellipse), (rectangle, path))>";
		
		this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.AND_EXPRESSION,
			DTDNodeType.AND_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT,
			DTDNodeType.AND_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
	}
	
	/**
	 * testNestedOrExpression
	 */
	public void testNestedOrExpression()
	{
		String source = "<!ELEMENT svg ((circle | ellipse) | (rectangle | path))>";
		
		this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.OR_EXPRESSION,
			DTDNodeType.OR_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT,
			DTDNodeType.OR_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
	}
	
	/**
	 * testMixedExpression
	 */
	public void testMixedExpression()
	{
		String source = "<!ELEMENT svg ((circle | ellipse), (rectangle | path))>";
		
		this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.AND_EXPRESSION,
			DTDNodeType.OR_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT,
			DTDNodeType.OR_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
	}
	
	/**
	 * testMixedExpression2
	 */
	public void testMixedExpression2()
	{
		String source = "<!ELEMENT svg ((circle, ellipse) | (rectangle, path))>";
		
		this.parse(
			source,
			DTDNodeType.ELEMENT_DECLARATION,
			DTDNodeType.OR_EXPRESSION,
			DTDNodeType.AND_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT,
			DTDNodeType.AND_EXPRESSION,
			DTDNodeType.ELEMENT,
			DTDNodeType.ELEMENT
		);
	}
	
	/**
	 * testEmptyAttList
	 */
	public void testEmptyAttList()
	{
		String source = "<!ATTLIST svg>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
	}
	
	/**
	 * testRequiredStringAttribute
	 */
	public void testRequiredStringAttribute()
	{
		String source = "<!ATTLIST svg name CDATA #REQUIRED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#REQUIRED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("CDATA", ((DTDTypeNode) type).getType());
	}
	
	/**
	 * testRequiredIDAttribute
	 */
	public void testRequiredIDAttribute()
	{
		String source = "<!ATTLIST svg name ID #REQUIRED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#REQUIRED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("ID", ((DTDTypeNode) type).getType());
	}
	
	/**
	 * testRequiredIDRefAttribute
	 */
	public void testRequiredIDRefAttribute()
	{
		String source = "<!ATTLIST svg name IDREF #REQUIRED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#REQUIRED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("IDREF", ((DTDTypeNode) type).getType());
	}
	
	/**
	 * testRequiredIDRefsAttribute
	 */
	public void testRequiredIDRefsAttribute()
	{
		String source = "<!ATTLIST svg name IDREFS #REQUIRED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#REQUIRED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("IDREFS", ((DTDTypeNode) type).getType());
	}
	
	/**
	 * testRequiredEntityAttribute
	 */
	public void testRequiredEntityAttribute()
	{
		String source = "<!ATTLIST svg name ENTITY #REQUIRED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#REQUIRED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("ENTITY", ((DTDTypeNode) type).getType());
	}
	
	/**
	 * testRequiredEntitiesAttribute
	 */
	public void testRequiredEntitiesAttribute()
	{
		String source = "<!ATTLIST svg name ENTITIES #REQUIRED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#REQUIRED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("ENTITIES", ((DTDTypeNode) type).getType());
	}
	
	/**
	 * testRequiredNMTokenAttribute
	 */
	public void testRequiredNMTokenAttribute()
	{
		String source = "<!ATTLIST svg name NMTOKEN #REQUIRED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#REQUIRED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("NMTOKEN", ((DTDTypeNode) type).getType());
	}
	
	/**
	 * testRequiredNMTokensAttribute
	 */
	public void testRequiredNMTokensAttribute()
	{
		String source = "<!ATTLIST svg name NMTOKENS #REQUIRED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#REQUIRED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("NMTOKENS", ((DTDTypeNode) type).getType());
	}
	
	/**
	 * testRequiredNotationAttribute
	 */
	public void testRequiredNotationAttribute()
	{
		String source = "<!ATTLIST svg name NOTATION(abc) #REQUIRED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.NOTATION
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#REQUIRED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDNotationTypeNode);
	}
	
	/**
	 * testRequiredEnumerationAttribute
	 */
	public void testRequiredEnumerationAttribute()
	{
		String source = "<!ATTLIST svg name (abc) #REQUIRED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.ENUMERATION
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#REQUIRED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDEnumerationTypeNode);
	}
	
	/**
	 * testImpliedStringAttribute
	 */
	public void testImpliedStringAttribute()
	{
		String source = "<!ATTLIST svg name CDATA #IMPLIED>";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#IMPLIED", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("CDATA", ((DTDTypeNode) type).getType());
	}
	
	/**
	 * testStringAttribute
	 */
	public void testStringAttribute()
	{
		String source = "<!ATTLIST svg name CDATA \"default\">";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("default", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("CDATA", ((DTDTypeNode) type).getType());
	}
	
	/**
	 * testFixedStringAttribute
	 */
	public void testFixedStringAttribute()
	{
		String source = "<!ATTLIST svg name CDATA #FIXED \"default\">";
		
		IParseNode root = this.parse(
			source,
			DTDNodeType.ATTRIBUTE_LIST_DECLARATION,
			DTDNodeType.ATTRIBUTE,
			DTDNodeType.TYPE
		);
		
		DTDAttListDeclNode attListDecl = (DTDAttListDeclNode) root.getFirstChild();
		assertEquals("svg", attListDecl.getName());
		
		IParseNode att = attListDecl.getFirstChild();
		assertTrue(att instanceof DTDAttributeNode);
		assertEquals("name", ((DTDAttributeNode) att).getName());
		assertEquals("#FIXED default", ((DTDAttributeNode) att).getMode());
		
		IParseNode type = att.getFirstChild();
		assertTrue(type instanceof DTDTypeNode);
		assertEquals("CDATA", ((DTDTypeNode) type).getType());
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
