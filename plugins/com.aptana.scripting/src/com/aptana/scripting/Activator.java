/**
 * This file Copyright (c) 2005-2010 Aptana, Inc. This program is
 * dual-licensed under both the Aptana Public License and the GNU General
 * Public license. You may elect to use one or the other of these licenses.
 * 
 * This program is distributed in the hope that it will be useful, but
 * AS-IS and WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, TITLE, or
 * NONINFRINGEMENT. Redistribution, except as permitted by whichever of
 * the GPL or APL you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or modify this
 * program under the terms of the GNU General Public License,
 * Version 3, as published by the Free Software Foundation.  You should
 * have received a copy of the GNU General Public License, Version 3 along
 * with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Aptana provides a special exception to allow redistribution of this file
 * with certain other free and open source software ("FOSS") code and certain additional terms
 * pursuant to Section 7 of the GPL. You may view the exception and these
 * terms on the web at http://www.aptana.com/legal/gpl/.
 * 
 * 2. For the Aptana Public License (APL), this program and the
 * accompanying materials are made available under the terms of the APL
 * v1.0 which accompanies this distribution, and is available at
 * http://www.aptana.com/legal/apl/.
 * 
 * You may view the GPL, Aptana's exception and additional terms, and the
 * APL in the file titled license.html at the root of the corresponding
 * plugin containing this source file.
 * 
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.scripting;

import net.contentobjects.jnotify.JNotifyException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.BundleContext;

import com.aptana.scripting.keybindings.internal.KeybindingsManager;
import com.aptana.scripting.model.BundleManager;
import com.aptana.scripting.model.BundleMonitor;
import com.aptana.scripting.model.RunType;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin
{
	public static final String PLUGIN_ID = "com.aptana.scripting"; //$NON-NLS-1$
	private static Activator plugin;

	/**
	 * Context id set by workbench part to indicate they are scripting aware.
	 */
	public static final String CONTEXT_ID = "com.aptana.scripting.context"; //$NON-NLS-1$

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault()
	{
		return plugin;
	}

	/**
	 * This returns the default run type to be used by ScriptingEngine and CommandElement.
	 * 
	 * @return
	 */
	public static RunType getDefaultRunType()
	{
		return RunType.CURRENT_THREAD;
	}

	/**
	 * logError
	 * 
	 * @param msg
	 * @param e
	 */
	public static void logError(String msg, Throwable e)
	{
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, msg, e));
	}

	/**
	 * logInfo
	 * 
	 * @param string
	 */
	public static void logInfo(String string)
	{
		getDefault().getLog().log(new Status(IStatus.INFO, PLUGIN_ID, string));
	}

	/**
	 * logWarning
	 * 
	 * @param msg
	 */
	public static void logWarning(String msg)
	{
		getDefault().getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, msg));
	}

	/**
	 * trace
	 * 
	 * @param string
	 */
	public static void trace(String string)
	{
		getDefault().getLog().log(new Status(IStatus.OK, PLUGIN_ID, string));
	}

	private FileTypeAssociationListener fileTypeListener;

	/**
	 * The constructor
	 */
	public Activator()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
		Job startupJob = new Job("Start bundle manager") //$NON-NLS-1$
		{

			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				fileTypeListener = new FileTypeAssociationListener();
				BundleManager.getInstance().addBundleChangeListener(fileTypeListener);

				// go ahead and process the workspace now to process bundles that exist already
				BundleManager.getInstance().loadBundles();

				// install Keybinding Manager
				KeybindingsManager.install();

				// turn on project and file monitoring
				try
				{
					BundleMonitor.getInstance().beginMonitoring();
				}
				catch (JNotifyException e)
				{
					Activator.logError(Messages.EarlyStartup_Error_Initializing_File_Monitoring, e);
				}
				return Status.OK_STATUS;
			}
		};
		startupJob.schedule();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		try
		{
			BundleMonitor.getInstance().endMonitoring();
			KeybindingsManager.uninstall();
			if (fileTypeListener != null)
			{
				fileTypeListener.cleanup();
				BundleManager.getInstance().removeBundleChangeListener(fileTypeListener);
			}
			fileTypeListener = null;
			// FIXME Clean up the bundle manager singleton!
		}
		catch (Exception e)
		{
			// ignore
		}
		finally
		{
			plugin = null;
			super.stop(context);
		}
	}
}
