package com.aptana.editor.dtd;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Tests for com.aptana.editor.dtd");
		//$JUnit-BEGIN$
		suite.addTestSuite(DTDSourceScannerTests.class);
		//$JUnit-END$
		return suite;
	}

}
