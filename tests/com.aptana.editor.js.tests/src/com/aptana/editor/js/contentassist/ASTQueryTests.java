package com.aptana.editor.js.contentassist;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.aptana.core.util.IOUtil;
import com.aptana.core.util.ResourceUtil;
import com.aptana.editor.js.Activator;
import com.aptana.editor.js.contentassist.JSASTQueryHelper.Classification;
import com.aptana.editor.js.parsing.JSParser;
import com.aptana.parsing.ParseState;
import com.aptana.parsing.ast.IParseNode;

public class ASTQueryTests extends TestCase
{
	private static final String CURSOR = "${cursor}";
	private static final int CURSOR_LENGTH = CURSOR.length();
	private JSASTQueryHelper _queryHelper;

	/**
	 * getContent
	 * 
	 * @param file
	 * @return
	 */
	protected String getContent(File file)
	{
		String result = "";

		try
		{
			FileInputStream input = new FileInputStream(file);

			result = IOUtil.read(input);
		}
		catch (IOException e)
		{
		}

		return result;
	}

	/**
	 * getFile
	 * 
	 * @param path
	 * @return
	 */
	protected File getFile(IPath path)
	{
		File result = null;

		try
		{
			URL url = FileLocator.find(Activator.getDefault().getBundle(), path, null);
			URL fileURL = FileLocator.toFileURL(url);
			URI fileURI = ResourceUtil.toURI(fileURL);

			result = new File(fileURI);
		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
		catch (URISyntaxException e)
		{
			fail(e.getMessage());
		}

		assertNotNull(result);
		assertTrue(result.exists());

		return result;
	}

	/**
	 * getAST
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	protected IParseNode getAST(String source) throws Exception
	{
		JSParser parser = new JSParser();
		ParseState parseState = new ParseState();

		parseState.setEditState(source, source, 0, 0);
		parser.parse(parseState);

		return parseState.getParseResult();
	}

	/**
	 * getTestContext
	 * 
	 * @param resource
	 * @param expectedTestPointCount
	 * @return
	 * @throws Exception
	 */
	protected List<IParseNode> getTestContext(String resource, int expectedTestPointCount) throws Exception
	{
		// get source from resource
		File file = this.getFile(new Path(resource));
		String source = this.getContent(file);

		// find all test points and clean up source along the way
		List<Integer> offsets = new LinkedList<Integer>();
		int offset = source.indexOf(CURSOR);

		while (offset != -1)
		{
			offsets.add(offset);
			source = source.substring(0, offset) + source.substring(offset + CURSOR_LENGTH);
			offset = source.indexOf(CURSOR);
		}

		// parse and grab the AST
		IParseNode ast = this.getAST(source);

		// find target nodes
		List<IParseNode> targetNodes = new LinkedList<IParseNode>();

		for (int o : offsets)
		{
			IParseNode targetNode = (ast != null && ast.contains(o)) ? ast.getNodeAt(o) : ast;

			targetNodes.add(targetNode);
		}

		assertNotNull(targetNodes);
		assertEquals(expectedTestPointCount, targetNodes.size());

		return targetNodes;
	}

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		this._queryHelper = new JSASTQueryHelper();
	}

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		this._queryHelper = null;
	}

	/**
	 * testGlobalNamedFunction
	 * 
	 * @throws Exception
	 */
	public void testGlobalNamedFunction() throws Exception
	{
		// make sure we have the expected number of test points
		List<IParseNode> targetNodes = this.getTestContext("ast-queries/globalNamedfunction.js", 1);

		List<String> globals = this._queryHelper.getGlobalFunctions(targetNodes.get(0));
		assertNotNull(globals);
		assertEquals(1, globals.size());
		assertEquals("globalFunction", globals.get(0));
	}

	/**
	 * testGlobalVarFunction
	 * 
	 * @throws Exception
	 */
	public void testGlobalVarFunction() throws Exception
	{
		List<IParseNode> targetNodes = this.getTestContext("ast-queries/globalVarfunction.js", 1);
		
		List<String> globals = this._queryHelper.getGlobalFunctions(targetNodes.get(0));
		assertNotNull(globals);
		assertEquals(1, globals.size());
		assertEquals("globalVarFunction", globals.get(0));
	}

	/**
	 * testGlobalNamedVarFunction
	 * 
	 * @throws Exception
	 */
	public void testGlobalNamedVarFunction() throws Exception
	{
		List<IParseNode> targetNodes = this.getTestContext("ast-queries/globalNamedVarfunction.js", 1);
		
		List<String> globals = this._queryHelper.getGlobalFunctions(targetNodes.get(0));
		assertNotNull(globals);
		assertEquals(2, globals.size());
		assertTrue(globals.contains("globalFunction"));
		assertTrue(globals.contains("globalVarFunction"));
	}

	/**
	 * testGlobalVars
	 * 
	 * @throws Exception
	 */
	public void testGlobalVars() throws Exception
	{
		List<IParseNode> targetNodes = this.getTestContext("ast-queries/globalVars.js", 1);
		IParseNode node = targetNodes.get(0);
		
		List<String> globals = this._queryHelper.getGlobalFunctions(node);
		assertNotNull(globals);
		assertEquals(0, globals.size());
		
		globals = this._queryHelper.getNonFunctionDeclarations(node);
		assertNotNull(globals);
		assertEquals(6, globals.size());
		assertTrue(globals.contains("localVar1"));
		assertTrue(globals.contains("localVar2"));
		assertTrue(globals.contains("localVar3"));
		assertTrue(globals.contains("localVar4"));
		assertTrue(globals.contains("localVar5"));
		assertTrue(globals.contains("localVar6"));
	}

	/**
	 * testLocalVars
	 * 
	 * @throws Exception
	 */
	public void testLocalVars() throws Exception
	{
		List<IParseNode> targetNodes = this.getTestContext("ast-queries/localVars.js", 2);
		int index = 0;
		
		// position 1
		List<String> globals = this._queryHelper.getGlobalFunctions(targetNodes.get(index));
		assertNotNull(globals);
		assertEquals(0, globals.size());
		
		List<String> locals = this._queryHelper.getNonFunctionDeclarations(targetNodes.get(index++));
		assertNotNull(locals);
		assertEquals(6, locals.size());
		assertTrue(locals.contains("localVar1"));
		assertTrue(locals.contains("localVar2"));
		assertTrue(locals.contains("localVar3"));
		assertTrue(locals.contains("localVar4"));
		assertTrue(locals.contains("localVar5"));
		assertTrue(locals.contains("localVar6"));
		
		// position 2
		globals = this._queryHelper.getGlobalFunctions(targetNodes.get(index++));
		assertNotNull(globals);
		assertEquals(1, globals.size());
		assertEquals("globalFunction", globals.get(0));
	}
	
	/**
	 * testParameters
	 * 
	 * @throws Exception
	 */
	public void testParameters() throws Exception
	{
		List<IParseNode> targetNodes = this.getTestContext("ast-queries/parameters.js", 2);
		int index = 0;
		
		// position 1
		Map<String,Classification> globals = this._queryHelper.getSymbolsInScope(targetNodes.get(index++));
		assertNotNull(globals);
		assertEquals(2, globals.size());
		assertTrue(globals.containsKey("parameter1"));
		assertTrue(globals.containsKey("parameter2"));
		
		// position 2
		globals = this._queryHelper.getSymbolsInScope(targetNodes.get(index++));
		assertNotNull(globals);
		assertEquals(0, globals.size());
	}

	/**
	 * testNestedFunctions
	 * 
	 * @throws Exception
	 */
	public void testNestedFunctions() throws Exception
	{
		List<IParseNode> targetNodes = this.getTestContext("ast-queries/nestedFunctions.js", 3);
		int index = 0;
		
		// position 1
		Map<String,Classification> globals = this._queryHelper.getSymbolsInScope(targetNodes.get(index++));
		assertNotNull(globals);
		assertEquals(4, globals.size());
		assertTrue(globals.containsKey("outerParam1"));
		assertTrue(globals.containsKey("outerParam2"));
		assertTrue(globals.containsKey("innerParam1"));
		assertTrue(globals.containsKey("innerParam2"));
		
		// position 2
		globals = this._queryHelper.getSymbolsInScope(targetNodes.get(index++));
		assertNotNull(globals);
		assertEquals(2, globals.size());
		assertTrue(globals.containsKey("outerParam1"));
		assertTrue(globals.containsKey("outerParam2"));
		
		// position 3
		globals = this._queryHelper.getSymbolsInScope(targetNodes.get(index++));
		assertNotNull(globals);
		assertEquals(0, globals.size());
	}
}