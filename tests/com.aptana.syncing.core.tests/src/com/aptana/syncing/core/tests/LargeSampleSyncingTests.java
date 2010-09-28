/**
 * This file Copyright (c) 2005-2007 Aptana, Inc. This program is
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
package com.aptana.syncing.core.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import com.aptana.core.ILogger;
import com.aptana.core.util.StringUtil;
import com.aptana.git.core.model.GitExecutable;
import com.aptana.ide.core.io.ConnectionContext;
import com.aptana.ide.core.io.CoreIOPlugin;
import com.aptana.ide.core.io.IConnectionPoint;
import com.aptana.ide.core.io.efs.EFSUtils;
import com.aptana.ide.core.io.vfs.IExtendedFileStore;
import com.aptana.ide.syncing.core.old.Synchronizer;
import com.aptana.ide.syncing.core.old.VirtualFileSyncPair;

/**
 * @author Ingo Muschenetz
 */
@SuppressWarnings("nls")
public abstract class LargeSampleSyncingTests extends TestCase
{
	protected IFileStore clientDirectory;
	protected IFileStore serverDirectory;

	protected IConnectionPoint clientManager;
	protected IConnectionPoint serverManager;
	
	private static Properties cachedProperties;

	protected static final Properties getConfig() {
		if (cachedProperties == null) {
			cachedProperties = new Properties();
			String propertiesFile = System.getenv("junit.properties");
			if (propertiesFile != null && new File(propertiesFile).length() > 0) {
				try {
					cachedProperties.load(new FileInputStream(propertiesFile));
				} catch (IOException ignore) {
				}
			}
		}
		return cachedProperties;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		ConnectionContext context = new ConnectionContext();
		context.put(ConnectionContext.COMMAND_LOG, System.out);
		CoreIOPlugin.setConnectionContext(clientManager, context);

		context.put(ConnectionContext.COMMAND_LOG, System.out);
		CoreIOPlugin.setConnectionContext(serverManager, context);

		clientDirectory = clientManager.getRoot().getFileStore(new Path("/client" + System.currentTimeMillis()));
		assertNotNull(clientDirectory);
		clientDirectory.mkdir(EFS.NONE, null);

		serverDirectory = serverManager.getRoot().getFileStore(new Path("/server" + System.currentTimeMillis()));
		assertNotNull(serverDirectory);
		serverDirectory.mkdir(EFS.NONE, null);

		super.setUp();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		try
		{
			if (clientDirectory.fetchInfo().exists())
			{
				//clientDirectory.delete(EFS.NONE, null);
				//assertFalse(clientDirectory.fetchInfo().exists());
			}
		}
		finally
		{
			if (clientManager.isConnected())
			{
				clientManager.disconnect(null);
			}
		}

		try
		{
			if (serverDirectory.fetchInfo().exists())
			{
				//serverDirectory.delete(EFS.NONE, null);
				//assertFalse(serverDirectory.fetchInfo().exists());
			}
		}
		finally
		{
			if (serverManager.isConnected())
			{
				serverManager.disconnect(null);
			}
		}

		super.tearDown();
	}

	/*
	 * Sync Item Tests
	 */
	public void testSyncIgnoreCloakedFiles() throws IOException, CoreException
	{
		syncTest(false, System.currentTimeMillis());
	}

	/*
	 * Sync Item Tests. Commented out as we evidently can't upload a file that begins with '.' to FTP servers, at least not our local one.
	 */
//	public void testSyncIncludeCloakedFiles() throws IOException, CoreException
//	{
//		syncTest(true, System.currentTimeMillis());
//	}

	/**
	 * Tests synchronization using a large sample size. Test does the following:
	 * 1) Checks out a git repo to a local directory (Directory A) at a particular tag (version 1.4)
	 * 2) Checkout out the same git repo to another local directory (Directory B)  at an older tag (say version 1.3)
	 * 3) Copies the local git repo to a second directory (Directory C)
	 * 4) Uploads Directory B to the remote server (Directory D)
	 * 5) Compares A and C to make sure nothing changed during the upload
	 * 6) Compares C and D to make sure the upload succeeded
	 * 7) Does a Synchronization between A and D, deleting any orphaned files on D. This would simulate a user uploading a new website
	 * 8) Compare A & C again to make sure nothing changed on A during the sync.
	 * 9) Compare A & D to make sure they are identical
	 * @throws IOException
	 * @throws ConnectionException
	 */
	public void syncTest(boolean includeCloakedFiles, long sysTime) throws IOException, CoreException
	{
		String CLIENT_TEST = "client_test";
		String CLIENT_CONTROL = "client_control";
		String SERVER_LOCAL = "server_local";
		String SERVER_TEST = "server_test";
		int timeTolerance = 1000; //150000;

		IFileStore clientTestDirectory = clientDirectory.getFileStore(new Path("/" + CLIENT_TEST + sysTime));
		IFileStore clientControlDirectory = clientDirectory.getFileStore(new Path("/" + CLIENT_CONTROL + sysTime));
		IFileStore serverLocalDirectory = clientDirectory.getFileStore(new Path("/" + SERVER_LOCAL + sysTime));
		IFileStore serverTestDirectory = serverDirectory.getFileStore(new Path("/" + SERVER_TEST + sysTime));		
		serverTestDirectory.mkdir(EFS.NONE, null);

		// clone version x of github-services to local directory (newer)
		System.out.println("1) Writing github repo to " + EFSUtils.getAbsolutePath(clientTestDirectory) );
		runGitClone("http://github.com/DmitryBaranovskiy/raphael.git", clientDirectory, clientTestDirectory.getName());
		
		System.out.println("2) Writing github repo to " + EFSUtils.getAbsolutePath(serverLocalDirectory));
		runGitClone("http://github.com/DmitryBaranovskiy/raphael.git", serverDirectory, serverLocalDirectory.getName());

		// checkout specific tags
		System.out.println("Checking out tag 1.4.0 on client_test");
		runGitTag(clientTestDirectory, "1.4.0");
		
		System.out.println("Checking out tag 1.3.0 on server_local");
		runGitTag(serverLocalDirectory, "1.3.0");

		System.out.println("3) Copying github repo to " + EFSUtils.getAbsolutePath(clientControlDirectory));
		clientTestDirectory.copy(clientControlDirectory, EFS.OVERWRITE, null);
		
		Synchronizer syncManager = new Synchronizer(true, timeTolerance, includeCloakedFiles);
		syncManager.setLogger(new ILogger() {

			public void logWarning(String message, Throwable th) {
				System.out.println(message);
			}

			public void logInfo(String message, Throwable th) {
				System.out.println(message);
			}

			public void logError(String message, Throwable th) {
				System.out.println(message);
			}
		});
		
		System.out.println("4) upload from server_local to server_test");
		VirtualFileSyncPair[] items = syncManager.getSyncItems(clientManager, serverManager, serverLocalDirectory,
				serverTestDirectory, null);
		syncManager.upload(items, null);
	
		System.out.println("5) ensure local version matches remote version");
		testDirectoriesEqual(serverLocalDirectory, serverTestDirectory, includeCloakedFiles, timeTolerance);

		System.out.println("6) test to make sure client_test and client_control are identical, as nothing should have changed locally");
		testDirectoriesEqual(clientTestDirectory, clientControlDirectory, includeCloakedFiles, timeTolerance);

		// Now get sync items between local and remote directories
		System.out.println("7) sync between client_test and server_test, deleting on remote");
		VirtualFileSyncPair[] items2 = syncManager.getSyncItems(clientManager, serverManager, clientTestDirectory,
				serverTestDirectory, null);
		syncManager.uploadAndDelete(items2, null);

		System.out.println("8) test to make sure client_test and client_control are identical, as nothing should have changed locally");
		testDirectoriesEqual(clientTestDirectory, clientControlDirectory, includeCloakedFiles, timeTolerance);

		System.out.println("9) test to make sure client_test and server_test are identical after sync");
		testDirectoriesEqual(clientTestDirectory, serverTestDirectory, includeCloakedFiles, timeTolerance);
	}

	protected void testDirectoriesEqual(IFileStore root1, IFileStore root2, boolean includeCloakedFiles, int timeTolerance) throws CoreException {
		IFileStore[] ctd = EFSUtils.getFiles(root1, true, includeCloakedFiles);		
		IFileStore[] ccd = EFSUtils.getFiles(root2, true, includeCloakedFiles);

		// create map of files on destination
		HashMap<String, IFileStore> map = new HashMap<String, IFileStore>();
		for (int i = 0; i < ccd.length; i++) {
			IFileStore fsTest = ccd[i];
			String fileRelPath = EFSUtils.getRelativePath(root2, fsTest);
			map.put(fileRelPath, fsTest);
		}
		
		assertEquals(ctd.length, ccd.length);

		// iterate through source and ensure all files made it to dest
		for (int i = 0; i < ctd.length; i++) {
			IFileStore fsControl = ctd[i];
			testFilesExists(map, root1, fsControl, timeTolerance);
		}
	}

	protected void testFilesEqual(IFileStore root1, IFileStore root2, IFileStore file1, IFileStore file2) throws CoreException {
		String file1RelPath = EFSUtils.getRelativePath(root1, file1);
		String file2RelPath = EFSUtils.getRelativePath(root2, file2);
		assertEquals(StringUtil.format("File {0} and {1} not equal", new String[] {file1RelPath, file2RelPath}), file1RelPath, file2RelPath);
		IFileInfo f1 = file1.fetchInfo(IExtendedFileStore.DETAILED, null);
		IFileInfo f2 = file2.fetchInfo(IExtendedFileStore.DETAILED, null);
		if(!f1.isDirectory()) {
			assertEquals(StringUtil.format("File {0} and {1} modification times differ", new String[] {file1RelPath, file2RelPath}), f1.getLastModified(), f2.getLastModified());
			assertEquals(StringUtil.format("File {0} and {1} different sizes", new String[] {file1RelPath, file2RelPath}), f1.getLength(), f2.getLength());
		}
	}

	protected void testFilesExists(HashMap<String, IFileStore> destMap, IFileStore sourceRoot, IFileStore sourceFile, int timeTolerance) throws CoreException {
		String relPath = EFSUtils.getRelativePath(sourceRoot, sourceFile);
		IFileStore destFile = destMap.get(relPath);
		//System.out.println("Comparing " + relPath);

		assertNotNull(StringUtil.format("File {0} not found on destination", relPath), destFile);
		IFileInfo f1 = sourceFile.fetchInfo(IExtendedFileStore.DETAILED, null);
		IFileInfo f2 = destFile.fetchInfo(IExtendedFileStore.DETAILED, null);
		if(!f1.isDirectory()) {
			long sourceFileTime = f1.getLastModified();
			long destFileTime = f2.getLastModified();
			long timeDiff = destFileTime - sourceFileTime;

			assertTrue(StringUtil.format("File {0} is {1} seconds newer on destination", new Object[] {relPath, (int)timeDiff/1000}), -timeTolerance <= timeDiff && timeDiff <= timeTolerance);
			assertEquals(StringUtil.format("File {0} different sizes", new String[] {relPath}), f1.getLength(), f2.getLength());
		}
	}

	protected void runGitClone(String url, IFileStore basePath, String directory) {
		Map<Integer, String> results = GitExecutable.instance().runInBackground(new Path(EFSUtils.getAbsolutePath(basePath)), "clone", url, directory);
		if (results != null && results.keySet().iterator().next() != 0)
		{
			fail("Git clone failed");
		}
	}

	protected void runGitTag(IFileStore basePath, String tag) {
		Map<Integer, String> results = GitExecutable.instance().runInBackground(new Path(EFSUtils.getAbsolutePath(basePath)), "checkout", "-b", tag);
		if (results != null && results.keySet().iterator().next() != 0)
		{
			fail("Git tag failed");
		}
	}

}
