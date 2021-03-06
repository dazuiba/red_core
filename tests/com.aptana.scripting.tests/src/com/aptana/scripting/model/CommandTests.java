package com.aptana.scripting.model;

public class CommandTests extends BundleTestBase
{
	/**
	 * executeCommand
	 * 
	 * @param bundleName
	 * @param commandName
	 * @return
	 */
	protected String executeCommand(String bundleName, String commandName)
	{
		BundleElement bundle = this.loadBundle(bundleName, BundlePrecedence.PROJECT);

		// get command
		CommandElement command = bundle.getCommandByName(commandName);
		assertNotNull(command);
		
		// run command and grab result
		CommandResult result = command.execute();
		assertNotNull(result);
		
		// return string result
		return result.getOutputString();
	}
	
	/**
	 * invokeStringCommandTest
	 */
	public void testInvokeStringCommandTest()
	{
		String resultText = this.executeCommand("invokeString", "Test");
		
		// NOTE: we have to use endsWith here because msysgit prints out /etc/motd
		// when using 'bash -l'. Most likely users will turn this off, but we need
		// to perform the test this way to pass in a default install
		assertTrue(resultText.endsWith("hello string"));
	}

	/**
	 * invokeBlockCommandTest
	 */
	public void tsetInvokeBlockCommandTest()
	{
		String resultText = this.executeCommand("invokeBlock", "Test");
		
		assertEquals("hello", resultText);
	}
	
	/**
	 * testRequireInBlock
	 */
	public void testRequireInBlock()
	{
		String resultText = this.executeCommand("requireInCommand", "MyCommand");
		
		assertEquals("My Thing Name", resultText);
	}
}
