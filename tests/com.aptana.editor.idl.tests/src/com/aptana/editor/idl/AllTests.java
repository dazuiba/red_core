package com.aptana.editor.idl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for com.aptana.editor.idl");
		//$JUnit-BEGIN$
		suite.addTestSuite(IDLSouceScannerTests.class);
		//$JUnit-END$
		return suite;
	}

}
