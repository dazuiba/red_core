/**
 * Copyright (c) 2005-2010 Aptana, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * If redistributing this code, this entire header must remain intact.
 */
package com.aptana.filewatcher.poller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.contentobjects.jnotify.IJNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * An implementation to fallback to using a 2 second polling mechanism to scan the directories using the Java File API.
 * 
 * @author cwilliams
 */
public class PollingNotifier implements IJNotify
{

	private int id = 0;
	private Map<Integer, DirectoryWatcher> watchers = new HashMap<Integer, DirectoryWatcher>();

	public int addWatch(String path, final int mask, boolean watchSubtree, final JNotifyListener listener)
			throws JNotifyException
	{
		DirectoryWatcher watcher = new DirectoryWatcher(new File(path), watchSubtree);
		watcher.addListener(new DirectoryChangeListener()
		{

			private Map<File, Long> files = new HashMap<File, Long>();

			@Override
			public void startPoll()
			{
			}

			@Override
			public void stopPoll()
			{
			}

			@Override
			public boolean added(File file)
			{
				files.put(file, file.lastModified());
				if ((mask & IJNotify.FILE_CREATED) == 0)
					return false;
				listener.fileCreated(0, file.getParent(), file.getName());
				return true;
			}

			@Override
			public boolean removed(File file)
			{
				files.remove(file);
				if ((mask & IJNotify.FILE_DELETED) == 0)
					return false;
				listener.fileDeleted(0, file.getParent(), file.getName());
				return true;
			}

			@Override
			public boolean changed(File file)
			{
				files.put(file, file.lastModified());
				if ((mask & IJNotify.FILE_MODIFIED) == 0)
					return false;
				listener.fileModified(0, file.getParent(), file.getName());
				return true;
			}

			@Override
			public boolean isInterested(File file)
			{
				return true;
			}

			@Override
			public Long getSeenFile(File file)
			{
				Long timestamp = files.get(file);
				IResource resource = null;
				IPath location = new Path(file.getAbsolutePath());
				if (file.isDirectory())
				{
					resource = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(location);
				}
				else
				{
					resource = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(location);
				}
				Long resourceTimestamp = null;
				if (resource != null && resource.exists())
					resourceTimestamp = resource.getLocalTimeStamp();
				if (resourceTimestamp == null && timestamp == null)
				{
					return null;
				}
				if (resourceTimestamp != null && (timestamp == null || resourceTimestamp > timestamp))
				{
					files.put(file, resourceTimestamp);
					return resourceTimestamp;
				}
				files.put(file, timestamp);
				return timestamp;
			}
		});
		int thisId = id;
		watchers.put(id++, watcher);
		watcher.start();
		return thisId;
	}

	public boolean removeWatch(int wd) throws JNotifyException
	{
		return watchers.remove(wd) != null;
	}

}
