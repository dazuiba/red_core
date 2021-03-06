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
package com.aptana.deploy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.core.internal.preferences.Base64;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

@SuppressWarnings("restriction")
public class HerokuAPI
{

	/**
	 * Authorization header name. Used for Basic Auth over HTTP/S.
	 */
	private static final String AUTHORIZATION_HEADER = "Authorization"; //$NON-NLS-1$

	/**
	 * User agent header name. Used to identify studio as the "client".
	 */
	private static final String USER_AGENT = "User-Agent"; //$NON-NLS-1$

	/**
	 * Content Type Accept header. Tells Heroku we can accept given content types. We default to XML and JSON.
	 */
	private static final String ACCEPT_HEADER = "Accept"; //$NON-NLS-1$
	private static final String ACCEPT_CONTENT_TYPES = "application/xml, application/json"; //$NON-NLS-1$

	/**
	 * Special API Version header for Heroku. We're coded against v2 right now.
	 */
	private static final String HEROKU_API_VERSION_HEADER = "X-Heroku-API-Version"; //$NON-NLS-1$
	private static final String API_VERSION_NUMBER = "2"; //$NON-NLS-1$

	private String userId;
	private String password;

	public HerokuAPI(String userId, String password)
	{
		this.userId = userId;
		this.password = password;
	}

	public static File getCredentialsFile()
	{
		String userHome = System.getProperty("user.home"); //$NON-NLS-1$
		if (userHome == null || userHome.trim().length() == 0)
		{
			userHome = ""; // FIXME What should we use if we can't resolve user home???? //$NON-NLS-1$
		}
		File herokuDir = new File(userHome, ".heroku"); //$NON-NLS-1$
		return new File(herokuDir, "credentials"); //$NON-NLS-1$
	}

	public IStatus authenticate()
	{
		HttpURLConnection connection = null;
		try
		{
			URL url = new URL("https://api.heroku.com/apps"); //$NON-NLS-1$
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty(USER_AGENT, "Aptana Studio 3.0.0"); // FIXME Grab proper version number //$NON-NLS-1$
			connection.setRequestProperty(HEROKU_API_VERSION_HEADER, API_VERSION_NUMBER);
			connection.setRequestProperty(ACCEPT_HEADER, ACCEPT_CONTENT_TYPES);
			connection.setUseCaches(false);
			connection.setAllowUserInteraction(false);
			// FIXME If auth fails, it seems to pop up a UI dialog asking for credentials!
			String usernamePassword = userId + ":" + password; //$NON-NLS-1$
			connection.setRequestProperty(AUTHORIZATION_HEADER,
					"Basic " + new String(Base64.encode(usernamePassword.getBytes()))); //$NON-NLS-1$
			int code = connection.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK)
			{
				return Status.OK_STATUS;
			}

			if (code == HttpURLConnection.HTTP_UNAUTHORIZED || code == HttpURLConnection.HTTP_FORBIDDEN)
			{
				return new Status(IStatus.ERROR, Activator.getPluginIdentifier(), Messages.HerokuAPI_AuthFailed_Error);
			}
			// some other response code...
			return new Status(IStatus.ERROR, Activator.getPluginIdentifier(),
					Messages.HerokuAPI_AuthConnectionFailed_Error);
		}
		catch (Exception e)
		{
			return new Status(IStatus.ERROR, Activator.getPluginIdentifier(), e.getMessage(), e);
		}
		finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
		}
	}

	public boolean writeCredentials()
	{
		File credentials = getCredentialsFile();
		credentials.getParentFile().mkdirs();
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new FileWriter(credentials));
			writer.write(userId);
			writer.newLine();
			writer.write(password);
			return true;
		}
		catch (IOException e)
		{
			Activator.logError(e);
		}
		finally
		{
			try
			{
				if (writer != null)
				{
					writer.close();
				}
			}
			catch (IOException e)
			{
				// ignore
			}
		}
		return false;
	}

	public static HerokuAPI fromCredentials()
	{
		File file = getCredentialsFile();
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(file));
			String userId = reader.readLine();
			String password = reader.readLine();
			return new HerokuAPI(userId, password);
		}
		catch (Exception e)
		{
			Activator.logError(e);
		}
		finally
		{
			try
			{
				if (reader != null)
				{
					reader.close();
				}
			}
			catch (IOException e)
			{
				// ignore
			}
		}
		// Return a special HerokuAPI that returns false/error for auth?
		return new HerokuAPI(null, null)
		{
			@Override
			public IStatus authenticate()
			{
				return new Status(IStatus.ERROR, Activator.getPluginIdentifier(),
						Messages.HerokuAPI_UnableToGetHerokuCredentialsError);
			}

			@Override
			public boolean writeCredentials()
			{
				// do nothing
				return false;
			}
		};
	}
}
