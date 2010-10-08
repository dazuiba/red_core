package com.aptana.red.core.tests.startup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.tests.session.PerformanceSessionTestSuite;
import org.eclipse.core.tests.session.Setup;
import org.eclipse.core.tests.session.SetupManager.SetupException;
import org.eclipse.core.tests.session.UIPerformanceSessionTestSuite;

public class AllTests extends TestCase
{
	public static final String PLUGIN_ID = "com.aptana.red.core.tests.all";

	public static Test suite()
	{
		TestSuite suite = new TestSuite(AllTests.class.getName());

		// make sure that the first run of the startup test is not recorded - it is heavily
		// influenced by the presence and validity of the cached information
		try
		{
			PerformanceSessionTestSuite firstRun = new PerformanceSessionTestSuite(PLUGIN_ID, 1, StartupTest.class);
			Setup setup = firstRun.getSetup();
			setup.setSystemProperty("eclipseTest.ReportResults", "false");
			suite.addTest(firstRun);
		}
		catch (SetupException e)
		{
			fail("Unable to create warm up test");
		}

		// For this test to take advantage of the new runtime processing, we set "-eclipse.activateRuntimePlugins=false"
		try
		{
			PerformanceSessionTestSuite headlessSuite = new PerformanceSessionTestSuite(PLUGIN_ID, 5, StartupTest.class);
			Setup headlessSetup = headlessSuite.getSetup();
			headlessSetup.setSystemProperty("eclipse.activateRuntimePlugins", "false");
			suite.addTest(headlessSuite);
		}
		catch (SetupException e)
		{
			fail("Unable to setup headless startup performance test");
		}

		suite.addTest(new UIPerformanceSessionTestSuite(PLUGIN_ID, 5, UIStartupTest.class));
		suite.addTest(new UIPerformanceSessionTestSuite(PLUGIN_ID, 5, UIStartupWithJobsTest.class));
		return suite;
	}
}