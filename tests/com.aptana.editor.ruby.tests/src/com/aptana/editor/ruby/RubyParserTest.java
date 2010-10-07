package com.aptana.editor.ruby;

import junit.framework.TestCase;

import com.aptana.editor.ruby.core.IRubyElement;
import com.aptana.editor.ruby.core.IRubyField;
import com.aptana.editor.ruby.core.IRubyMethod;
import com.aptana.editor.ruby.core.IRubyType;
import com.aptana.editor.ruby.parsing.RubyParser;
import com.aptana.editor.ruby.parsing.ast.RubyElement;
import com.aptana.parsing.ParseState;
import com.aptana.parsing.ast.IParseNode;

public class RubyParserTest extends TestCase
{

	private RubyParser fParser;
	private ParseState fParseState;

	@Override
	protected void setUp() throws Exception
	{
		fParser = new RubyParser();
		fParseState = new ParseState();
	}

	@Override
	protected void tearDown() throws Exception
	{
		fParser = null;
		fParseState.clearEditState();
	}

	public void testClassWithFieldAndMethod() throws Exception
	{
		String source = "class Person\n\tattr_reader :name, :age\n\tdef initialize(name, age)\n\t\t@name, @age = name, age\n\tend\nend";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		IParseNode[] classes = result.getChildren();
		assertEquals(1, classes.length); // one class declaration
		assertEquals(IRubyElement.TYPE, classes[0].getNodeType()); // class type
		assertEquals("Person", classes[0].toString()); // class name

		IRubyType rubyClass = (IRubyType) classes[0];
		assertEquals(true, rubyClass.isClass());
		assertEquals("Object", rubyClass.getSuperclassName());
		// checks fields
		assertFields(rubyClass, new String[] { "@name", "@age" });
		// checks methods
		assertMethods(rubyClass, new String[] { "name()", "age()", "initialize(name, age)" });
	}

	public void testModuleWithConst() throws Exception
	{
		String source = "module Mod\n\tinclude Math\n\tCONST = 1\nend";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		IParseNode[] children = result.getChildren();
		assertEquals(1, children.length); // one module declaration
		assertEquals(IRubyElement.TYPE, children[0].getNodeType());
		IRubyType module = (IRubyType) children[0];
		assertEquals(false, module.isClass());
		assertEquals(true, module.isModule());
		assertEquals("Mod", children[0].toString()); // module name

		assertFields(module, new String[] { "CONST" });
	}

	public void testSingletonMethod() throws Exception
	{
		String source = "def foo.size\n\t0\nend";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		IParseNode[] children = result.getChildren();
		assertEquals(1, children.length);
		assertEquals(IRubyElement.METHOD, children[0].getNodeType());
		IRubyMethod method = (IRubyMethod) children[0];
		assertEquals(true, method.isSingleton());
	}

	public void testRequire() throws Exception
	{
		String source = "require 'yaml'";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		IParseNode[] children = result.getChildren();
		assertEquals(1, children.length); // the container for require/load statements
		assertEquals(IRubyElement.IMPORT_CONTAINER, children[0].getNodeType());
		assertEquals("require/load declarations", children[0].toString());
		assertEquals(0, ((RubyElement) children[0]).getStart());
		assertEquals(14, ((RubyElement) children[0]).getEnd());

		IParseNode[] imports = children[0].getChildren();
		assertEquals(1, imports.length); // one require statement
		assertEquals(IRubyElement.IMPORT_DECLARATION, imports[0].getNodeType());
		assertEquals("yaml", imports[0].toString());
		assertEquals(imports[0], children[0].getNodeAtOffset(5));
	}

	public void testGlobalVar() throws Exception
	{
		String source = "$foo = 5";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		IParseNode[] children = result.getChildren();
		assertEquals(1, children.length); // one global variable
		assertEquals(IRubyElement.GLOBAL, children[0].getNodeType());
		assertEquals("$foo", children[0].toString());
	}

	public void testClassVar() throws Exception
	{
		String source = "@@foo = 5";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		IParseNode[] children = result.getChildren();
		assertEquals(1, children.length); // one global variable
		assertEquals(IRubyElement.CLASS_VAR, children[0].getNodeType());
		assertEquals("@@foo", children[0].toString());
	}

	public void testAlias() throws Exception
	{
		String source = "alias :foo :bar";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		IParseNode[] children = result.getChildren();
		assertEquals(1, children.length);
		assertEquals(IRubyElement.METHOD, children[0].getNodeType());
		assertEquals("foo()", children[0].toString());
	}

	public void testArray() throws Exception
	{
		String source = "foo = [1, 2, \"3\"] + [a, b]";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		IParseNode[] children = result.getChildren();
		assertEquals(1, children.length);
		assertEquals(IRubyElement.LOCAL_VAR, children[0].getNodeType());
		assertEquals("foo", children[0].toString());
	}

	public void testHash() throws Exception
	{
		String source = "foo = {1 => 2, \"2\" => \"4\"}";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		IParseNode[] children = result.getChildren();
		assertEquals(1, children.length);
		assertEquals(IRubyElement.LOCAL_VAR, children[0].getNodeType());
		assertEquals("foo", children[0].toString());
	}

	public void testRegex() throws Exception
	{
		String source = "def foo(s)\\n\\t(s =~ /<0(x|X)(\\d|[a-f]|[A-F])+>/) != nil\nend";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		assertEquals(IRubyElement.SCRIPT, result.getNodeType());
		assertEquals(0, result.getStartingOffset());
		assertEquals(59, result.getEndingOffset());
	}

	public void testCase() throws Exception
	{
		String source = "case i\nwhen1, 2..5\n\tputs \"1..5\"\nwhen 6..10\n\tputs \"6..10\"\nend";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		assertEquals(0, result.getStartingOffset());
		assertEquals(60, result.getEndingOffset());
	}

	public void testWhile() throws Exception
	{
		String source = "puts i+=1 while i<3";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		assertEquals(0, result.getStartingOffset());
		assertEquals(20, result.getEndingOffset());
	}

	public void testFor() throws Exception
	{
		String source = "for foo in (1..3)\n\tputs foo\nend";
		fParseState.setEditState(source, source, 0, 0);

		IParseNode result = fParser.parse(fParseState);
		IParseNode[] children = result.getChildren();
		assertEquals(1, children.length);
		assertEquals(IRubyElement.LOCAL_VAR, children[0].getNodeType());
		assertEquals("foo", children[0].toString());
	}

//	public void testIfElse() throws Exception
//	{
//		String source = "if (foo > 0)\n\tputs foo\nelse\n\tputs -foo\nend";
//		fParseState.setEditState(source, source, 0, 0);
//
//		IParseNode result = fParser.parse(fParseState);
//		assertEquals(0, result.getStartingOffset());
//		assertEquals(43, result.getEndingOffset());
//	}

	private void assertFields(IRubyType rubyClass, String[] fieldNames)
	{
		IRubyField[] fields = rubyClass.getFields();
		assertEquals(fieldNames.length, fields.length);
		for (int i = 0; i < fields.length; ++i)
		{
			assertEquals(fieldNames[i], fields[i].toString());
		}
	}

	private void assertMethods(IRubyType rubyClass, String[] methodNames)
	{
		IRubyMethod[] methods = rubyClass.getMethods();
		assertEquals(methodNames.length, methods.length);
		for (int i = 0; i < methods.length; ++i)
		{
			assertEquals(methodNames[i], methods[i].toString());
		}
	}
}
