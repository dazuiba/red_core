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
package com.aptana.scripting.model;

import java.io.File;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import net.contentobjects.jnotify.IJNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import com.aptana.filewatcher.FileWatcher;
import com.aptana.scripting.Activator;

public class BundleMonitor implements IResourceChangeListener, IResourceDeltaVisitor, JNotifyListener
{
	// TODO: use constants from BundleManager for bundles, commands, and snippets directory names
	private static final Pattern USER_BUNDLE_PATTERN = Pattern.compile(".+?[/\\\\]bundle\\.rb$", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$
	private static final Pattern USER_FILE_PATTERN = Pattern.compile(".+?[/\\\\](?:commands|snippets)/[^/\\\\]+\\.rb$", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$
	private static final Pattern BUNDLE_PATTERN = Pattern.compile("/.+?/bundles/.+?/bundle\\.rb$", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$
	private static final Pattern FILE_PATTERN = Pattern.compile("/.+?/bundles/.+?/(?:commands|snippets)/[^/]+\\.rb$", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

	private static BundleMonitor INSTANCE;

	private boolean _registered;
	private int _watchId;

	/**
	 * getInstance
	 * 
	 * @return
	 */
	public static BundleMonitor getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new BundleMonitor();
		}

		return INSTANCE;
	}

	/**
	 * BundleMonitor
	 */
	private BundleMonitor()
	{
		this._watchId = -1;
	}

	/**
	 * beginMonitoring
	 * 
	 * @throws JNotifyException
	 */
	public synchronized void beginMonitoring() throws JNotifyException
	{
		if (this._registered == false)
		{
			// begin monitoring resource changes
			ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);

			// Make sure the user bundles directory exists
			String userBundlesPath = BundleManager.getInstance().getUserBundlesPath();
			File bundleDirectory = new File(userBundlesPath);
			boolean directoryExists = true;

			if (bundleDirectory.exists() == false)
			{
				directoryExists = bundleDirectory.mkdirs();
			}
			else
			{
				directoryExists = bundleDirectory.isDirectory();
			}

			if (directoryExists)
			{
				try
				{
					this._watchId = FileWatcher.addWatch(userBundlesPath, IJNotify.FILE_ANY, true, this);
				}
				catch (JNotifyException e)
				{
					Activator.logError(Messages.BundleMonitor_ERROR_REGISTERING_FILE_WATCHER, e);
				}
			}
			else
			{
				String message = MessageFormat.format(
						Messages.BundleMonitor_INVALID_WATCHER_PATH,
						userBundlesPath);

				Activator.logError(message, null);
			}

			this._registered = true;
		}
	}

	/**
	 * endMonitoring
	 * 
	 * @throws JNotifyException
	 */
	public synchronized void endMonitoring() throws JNotifyException
	{
		if (this._registered)
		{
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(INSTANCE);

			if (this._watchId != -1)
			{
				FileWatcher.removeWatch(this._watchId);
				this._watchId = -1;
			}

			this._registered = false;
		}
	}

	/**
	 * fileCreated
	 * 
	 * @param wd
	 * @param rootPath
	 * @param name
	 */
	synchronized public void fileCreated(int wd, String rootPath, String name)
	{
		fileCreatedHelper(rootPath, name);
		
		// used by unit tests
		this.notifyAll();
	}

	/**
	 * fileCreatedHelper
	 * 
	 * @param rootPath
	 * @param name
	 */
	private void fileCreatedHelper(String rootPath, String name)
	{
		if (isUserBundleFile(rootPath, name))
		{
			BundleManager manager = BundleManager.getInstance();
			File file = new File(rootPath, name);

			if (USER_BUNDLE_PATTERN.matcher(name).matches())
			{
				// load the entire bundle now that we have a bundle.rb file
				manager.loadBundle(file.getParentFile());
			}
			else
			{
				// load the script. isUserBundleFile only returns true if this
				// is part of an existing bundle; otherwise, it will get loaded
				// later once its bundle.rb file has been created
				manager.loadScript(file);
			}
		}
	}

	/**
	 * fileDeleted
	 * 
	 * @param wd
	 * @param rootPath
	 * @param name
	 */
	synchronized public void fileDeleted(int wd, String rootPath, String name)
	{
		fileDeletedHelper(rootPath, name);
		
		// used by unit tests
		this.notifyAll();
	}

	/**
	 * fileDeletedHelper
	 * 
	 * @param rootPath
	 * @param name
	 */
	private void fileDeletedHelper(String rootPath, String name)
	{
		if (isUserBundleFile(rootPath, name))
		{
			BundleManager manager = BundleManager.getInstance();
			File file = new File(rootPath, name);

			if (USER_BUNDLE_PATTERN.matcher(name).matches())
			{
				// unload entire bundle now that we don't have bundle.rb file
				// anymore
				manager.unloadBundle(file.getParentFile());
			}
			else
			{
				manager.unloadScript(file);
			}
		}
		else
		{
			reloadDependentScripts(new File(rootPath, name));
		}
	}

	/**
	 * fileModified
	 * 
	 * @param wd
	 * @param rootPath
	 * @param name
	 */
	synchronized public void fileModified(int wd, String rootPath, String name)
	{
		if (isUserBundleFile(rootPath, name))
		{
			File file = new File(rootPath, name);

			BundleManager.getInstance().reloadScript(file);
		}
		else
		{
			reloadDependentScripts(new File(rootPath, name));
		}
		
		// used by unit tests
		this.notifyAll();
	}

	/**
	 * fileRenamed
	 * 
	 * @param wd
	 * @param rootPath
	 * @param oldName
	 * @param newName
	 */
	synchronized public void fileRenamed(int wd, String rootPath, String oldName, String newName)
	{
		this.fileDeletedHelper(rootPath, oldName);
		this.fileCreatedHelper(rootPath, newName);
		
		// used by unit tests
		this.notifyAll();
	}

	/**
	 * isProjectBundleFile
	 * 
	 * @param delta
	 * @return
	 */
	private boolean isProjectBundleFile(IResourceDelta delta)
	{
		String fullProjectPath = delta.getFullPath().toString();
		boolean result = false;
		
		if (BUNDLE_PATTERN.matcher(fullProjectPath).matches())
		{
			// always return true for bundle.rb files
			result = true;
		}
		else if (FILE_PATTERN.matcher(fullProjectPath).matches())
		{
			// only return true if the script is part of an existing bundle.
			File script = delta.getResource().getLocation().toFile();
			
			result = this.isScriptInExistingBundle(script);
		}
		
		return result;
	}

	/**
	 * isScriptInExistingBundle
	 * 
	 * @param script
	 * @return
	 */
	private boolean isScriptInExistingBundle(File script)
	{
		BundleManager manager = BundleManager.getInstance();
		File bundleDirectory = manager.getBundleDirectory(script);
		
		return manager.hasBundleAtPath(bundleDirectory);
	}
	
	/**
	 * isUserbundlesFile
	 * @param rootPath
	 * @param name
	 * 
	 * @return
	 */
	private boolean isUserBundleFile(String rootPath, String name)
	{
		boolean result = false;
		
		if (USER_BUNDLE_PATTERN.matcher(name).matches())
		{
			// always return true for bundle.rb files
			result = true;
		}
		else if (USER_FILE_PATTERN.matcher(name).matches())
		{
			// only return true if the script is part of an existing bundle.
			File script = new File(rootPath, name);
			
			result = this.isScriptInExistingBundle(script);
		}
		
		return result;
	}

	/**
	 * processFile
	 * 
	 * @param delta
	 */
	private void processFile(IResourceDelta delta)
	{
		IResource resource = delta.getResource();

		if (resource != null && resource.getLocation() != null)
		{
			BundleManager manager = BundleManager.getInstance();
			File file = resource.getLocation().toFile();
			String fullProjectPath = delta.getFullPath().toString();

			BundlePrecedence scope = manager.getBundlePrecedence(file);

			// don't process user bundles that are projects since file watcher will handle those
			if (scope != BundlePrecedence.USER)
			{
				switch (delta.getKind())
				{
					case IResourceDelta.ADDED:
						if (BUNDLE_PATTERN.matcher(fullProjectPath).matches())
						{
							manager.loadBundle(file.getParentFile());
						}
						else
						{
							manager.loadScript(file);
						}
						break;

					case IResourceDelta.REMOVED:
						if (BUNDLE_PATTERN.matcher(fullProjectPath).matches())
						{
							// NOTE: we have to both unload all scripts associated with this bundle
							// and the bundle file itself. Technically, the bundle file doesn't
							// exist any more so it won't get unloaded
							manager.unloadBundle(file.getParentFile());
						}
						
						manager.unloadScript(file);
						break;

					case IResourceDelta.CHANGED:
						if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0)
						{
							IPath movedFromPath = delta.getMovedFromPath();
							IResource movedFrom = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(
									movedFromPath);

							if (movedFrom != null && movedFrom instanceof IFile)
							{
								manager.unloadScript(movedFrom.getLocation().toFile());
								manager.loadScript(file);
							}
						}
						else if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0)
						{
							IPath movedToPath = delta.getMovedToPath();
							IResource movedTo = ResourcesPlugin.getWorkspace().getRoot()
									.getFileForLocation(movedToPath);

							if (movedTo != null && movedTo instanceof IFile)
							{
								manager.unloadScript(file);
								manager.loadScript(movedTo.getLocation().toFile());
							}
						}
						else if ((delta.getFlags() & IResourceDelta.REPLACED) != 0)
						{
							manager.reloadScript(file);
						}
						else if ((delta.getFlags() & IResourceDelta.CONTENT) != 0)
						{
							manager.reloadScript(file);
						}
						break;
				}
			}
		}
	}

	/**
	 * realoadDependentScripts
	 * 
	 * @param file
	 */
	private void reloadDependentScripts(File file)
	{
		BundleManager manager = BundleManager.getInstance();
		String fullPath = file.getAbsolutePath();
		LibraryCrossReference xref = LibraryCrossReference.getInstance();
		
		if (xref.hasLibrary(fullPath))
		{
			for (String script : xref.getPathsFromLibrary(fullPath))
			{
				manager.reloadScript(new File(script));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent
	 * )
	 */
	public void resourceChanged(IResourceChangeEvent event)
	{
		try
		{
			event.getDelta().accept(this);
		}
		catch (CoreException e)
		{
			Activator.logError(Messages.BundleMonitor_Error_Processing_Resource_Change, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	 */
	public boolean visit(IResourceDelta delta) throws CoreException
	{
		// process project bundle files, but ignore user bundles since file watcher will take care of those
		if (isProjectBundleFile(delta))
		{
			this.processFile(delta);
		}
		else
		{
			if (delta.getKind() == IResourceDelta.CHANGED && (delta.getFlags() & IResourceDelta.CONTENT) != 0)
			{
				IResource resource = delta.getResource();
				File file = resource.getLocation().toFile();
				
				this.reloadDependentScripts(file);
			}
		}

		return true;
	}
}
