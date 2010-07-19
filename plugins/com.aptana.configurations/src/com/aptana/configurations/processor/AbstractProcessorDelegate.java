package com.aptana.configurations.processor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;

import com.aptana.core.util.ProcessUtil;

/**
 * A abstract configuration processor delegate that provides some common operations of running a command and retrieving
 * its output.
 * 
 * @author Shalom Gibly <sgibly@aptana.com>
 */
public abstract class AbstractProcessorDelegate implements IConfigurationProcessorDelegate
{
	protected Map<String, String> supportedCommands;

	/**
	 * Constructs a new processor delegate.
	 */
	public AbstractProcessorDelegate()
	{
		supportedCommands = new HashMap<String, String>(5);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.configurations.processor.IConfigurationProcessorDelegate#getSupportedCommands()
	 */
	@Override
	public Set<String> getSupportedCommands()
	{
		return Collections.unmodifiableSet(supportedCommands.keySet());
	}

	/**
	 * Provides a common implementation of validating the command type and executing the command on a shell.
	 * 
	 * @param commandType
	 *            One of the supported command types
	 * @return The run command output
	 * @throws IllegalArgumentException
	 *             In case the command type is not supported or a shell was not found
	 */
	@Override
	public Object runCommand(String commandType)
	{
		String command = supportedCommands.get(commandType);
		if (command == null)
		{
			throw new IllegalArgumentException("Command not supported - " + commandType); //$NON-NLS-1$
		}
		String shellCommandPath = getShellPath();
		if (shellCommandPath == null)
		{
			throw new IllegalArgumentException("Shell command path is null"); //$NON-NLS-1$
		}
		String commandSwitch = "-c"; //$NON-NLS-1$
		if (shellCommandPath.equals("cmd")) { //$NON-NLS-1$
			// Treat it differently
			commandSwitch = "/C"; //$NON-NLS-1$
		}
		command = getSupportedApplication() + ' ' + command;
		String versionOutput = ProcessUtil.outputForCommand(shellCommandPath, null, new String[] { commandSwitch,
				command });
		return versionOutput;
	}

	/**
	 * Return the shell command path.
	 * 
	 * @return The shell path.
	 * @throws IllegalArgumentException
	 *             In case we could not find any such path.
	 */
	protected String getShellPath()
	{
		// Get the shell path
		String shellCommandPath = AbstractConfigurationProcessor.getShellPath();
		if (shellCommandPath == null)
		{
			if (Platform.OS_WIN32.equals(Platform.getOS()))
			{
				// In case we are on Windows, try to get the result by executing 'cmd'
				shellCommandPath = "cmd"; //$NON-NLS-1$
			}
			else
			{
				throw new IllegalArgumentException("Could not locate a shell to run the command"); //$NON-NLS-1$
			}
		}
		return shellCommandPath;
	}

	public abstract String getSupportedApplication();
}