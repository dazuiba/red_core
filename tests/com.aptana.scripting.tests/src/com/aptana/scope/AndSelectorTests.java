package com.aptana.scope;

import junit.framework.TestCase;

public class AndSelectorTests extends TestCase
{
	/**
	 * testNamesArePrefixes
	 */
	public void testNamesArePrefixes()
	{
		ScopeSelector selector = new ScopeSelector("source string.quoted");

		assertTrue(selector.matches("source.ruby string.quoted.double.ruby"));
	}

	/**
	 * testPrefixThenExact
	 */
	public void testPrefixThenExact()
	{
		ScopeSelector selector = new ScopeSelector("source string.quoted.double.ruby");

		assertTrue(selector.matches("source.ruby string.quoted.double.ruby"));
	}

	/**
	 * testExactThenPrefix
	 */
	public void testExactThenPrefix()
	{
		ScopeSelector selector = new ScopeSelector("source.ruby string.quoted");

		assertTrue(selector.matches("source.ruby string.quoted.double.ruby"));
	}

	/**
	 * testNamesAreExact
	 */
	public void testNamesAreExact()
	{
		ScopeSelector selector = new ScopeSelector("source.ruby string.quoted.double.ruby");

		assertTrue(selector.matches("source.ruby string.quoted.double.ruby"));
	}

	/**
	 * testFirstNonMatching
	 */
	public void testFirstNonMatching()
	{
		ScopeSelector selector = new ScopeSelector("source.ruby string.quoted.double.ruby");

		assertFalse(selector.matches("source.php string.quoted.double.ruby"));
	}

	/**
	 * testSecondNonMatching
	 */
	public void testSecondNonMatching()
	{
		ScopeSelector selector = new ScopeSelector("source.ruby string.quoted.double.ruby");

		assertFalse(selector.matches("source.ruby string.quoted.double.php"));
	}

	/**
	 * testEmptySelector
	 */
	public void testEmptySelector()
	{
		ScopeSelector selector = new ScopeSelector("");

		assertFalse(selector.matches("source.ruby string.quoted.double.ruby"));
	}

	/**
	 * testEmptyScope
	 */
	public void testEmptyScope()
	{
		ScopeSelector selector = new ScopeSelector("source.ruby string.quoted.double.ruby");

		assertFalse(selector.matches(""));
	}

	/**
	 * testNullSelector
	 */
	public void testNullSelector()
	{
		ScopeSelector selector = new ScopeSelector((String) null);

		assertFalse(selector.matches("source.ruby string.quoted.double.ruby"));
	}

	/**
	 * testNullScope
	 */
	public void testNullScope()
	{
		ScopeSelector selector = new ScopeSelector("source.ruby string.quoted.double.ruby");

		assertFalse(selector.matches((String) null));
	}
	
	/**
	 * testBeginsWith
	 */
	public void testBeginsWith()
	{
		ScopeSelector selector = new ScopeSelector("source.ruby");

		assertTrue(selector.matches("source.ruby string.quoted.double.ruby"));
	}
	
	/**
	 * testWithin
	 */
	public void testWithin()
	{
		ScopeSelector selector = new ScopeSelector("source.ruby");

		assertTrue(selector.matches("text.html.ruby source.ruby.rails.embedded.html string.quoted.double.ruby punctuation.definition.string.end.ruby"));
	}
	
	/**
	 * testEndsWith
	 */
	public void testEndsWith()
	{
		ScopeSelector selector = new ScopeSelector("source.ruby");

		assertTrue(selector.matches("text.html.ruby source.ruby.rails.embedded.html"));
	}
}
