/**
 * This file Copyright (c) 2005-2009 Aptana, Inc. This program is
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
package com.aptana.terminal.connector;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.internal.core.StreamsProxy;
import org.eclipse.tm.internal.terminal.provisional.api.ITerminalControl;
import org.eclipse.tm.internal.terminal.provisional.api.TerminalState;
import org.eclipse.tm.internal.terminal.provisional.api.provider.TerminalConnectorImpl;

import com.aptana.terminal.Activator;
import com.aptana.terminal.IProcessConfiguration;
import com.aptana.terminal.internal.IProcessListener;
import com.aptana.terminal.internal.ProcessConfigurations;
import com.aptana.terminal.internal.ProcessLauncher;
import com.aptana.terminal.internal.StreamsProxyOutputStream;

/**
 * @author Max Stepanov
 *
 */
@SuppressWarnings("restriction")
public class LocalTerminalConnector extends TerminalConnectorImpl implements IProcessListener {

	public static final String ID = "com.aptana.terminal.connector.local"; //$NON-NLS-1$
	
	private static final String ENCODING = "ISO-8859-1"; //$NON-NLS-1$

	// TODO: These shouldn't be in here. We're pulling the values from the explorer plugin
	// so as not to create a dependency on the two projects.
	private static final String ACTIVE_PROJECT_PROPERTY = "activeProject"; //$NON-NLS-1$
	private static final String EXPLORER_PLUGIN_ID = "com.aptana.explorer"; //$NON-NLS-1$
	
	private static final String USER_HOME_PROPERTY = "user.home"; //$NON-NLS-1$

	private ProcessLauncher processLauncher;
	private StreamsProxy streamsProxy;
	private OutputStream processInputStream;
	
	private int currentWidth = 0;
	private int currentHeight = 0;
	
	private IPath initialDirectory;
	
	/* (non-Javadoc)
	 * @see org.eclipse.tm.internal.terminal.provisional.api.provider.TerminalConnectorImpl#getSettingsSummary()
	 */
	@Override
	public String getSettingsSummary() {
		return "TODO - LocalTerminalConnector.getSettingsSummary()"; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tm.internal.terminal.provisional.api.provider.TerminalConnectorImpl#getTerminalToRemoteStream()
	 */
	@Override
	public OutputStream getTerminalToRemoteStream() {
		return processInputStream;
	}

	@Override
	public void connect(ITerminalControl control) {
		super.connect(control);
		control.setState(TerminalState.CONNECTING);
		startProcess(control);
		control.setState(TerminalState.CONNECTED);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tm.internal.terminal.provisional.api.provider.TerminalConnectorImpl#setTerminalSize(int, int)
	 */
	@Override
	public void setTerminalSize(int newWidth, int newHeight) {
		if (currentWidth == newWidth && currentHeight == newHeight) {
			return;
		}
		currentWidth = newWidth;
		currentHeight = newHeight;
		try {
			streamsProxy.write("\u001b[8;"+Integer.toString(currentHeight)+";"+Integer.toString(currentWidth)+"t"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (IOException e) {
			Activator.logError("Send terminal size failed.", e); //$NON-NLS-1$
		}
	}
	
	/* (non-Javadoc)
	 * @see com.aptana.terminal.internal.IProcessListener#processCompleted()
	 */
	@Override
	public void processCompleted() {
		if (streamsProxy != null) {
			streamsProxy.close();
			streamsProxy = null;
		}
	}
	
	public void setWorkingDirectory(IPath workingDirectory) {
		this.initialDirectory = workingDirectory;
	}

	/**
	 * @return the initialDirectory
	 */
	public IPath getWorkingDirectory() {
		return initialDirectory;
	}

	private void startProcess(ITerminalControl control) {
		try {
			
			processLauncher = new ProcessLauncher(getCurrentConfiguration(), initialDirectory = getInitialDirectory());
			processLauncher.addProcessListener(this);
			processLauncher.launch();
			
			streamsProxy = new StreamsProxy(processLauncher.getProcess(), ENCODING);
			
			// Hook up standard input:
			//
			processInputStream = new BufferedOutputStream(new StreamsProxyOutputStream(streamsProxy, ENCODING), 1024);
			
			// Hook up standard output:
			//
			IStreamMonitor outputMonitor = streamsProxy.getOutputStreamMonitor();
			LocalTerminalOutputListener outputListener = new LocalTerminalOutputListener(control);
			outputMonitor.addListener(outputListener);
			outputListener.streamAppended(outputMonitor.getContents(), outputMonitor);

			// Hook up standard error:
			//
			IStreamMonitor errorMonitor = streamsProxy.getErrorStreamMonitor();
			LocalTerminalOutputListener errorListener = new LocalTerminalOutputListener(control);
			errorMonitor.addListener(errorListener);
			errorListener.streamAppended(errorMonitor.getContents(), errorMonitor);

		} catch (IOException e) {
			Activator.logError("Starting terminal process failed.", e); //$NON-NLS-1$
		}
	}
	
	private IProcessConfiguration getCurrentConfiguration() {
		return ProcessConfigurations.getInstance().getProcessConfigurations()[0];
	}

	private IPath getInitialDirectory() {
		if (initialDirectory != null && initialDirectory.toFile().isDirectory()) {
			return initialDirectory;
		}
		String activeProjeectName = Platform.getPreferencesService().getString(EXPLORER_PLUGIN_ID, ACTIVE_PROJECT_PROPERTY, null, null);
		if (activeProjeectName != null) {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(activeProjeectName);
			if (project != null) {
				IPath location = project.getLocation();
				if (location != null) {
					return location;
				}
			}
		}
		String home = System.getProperty(USER_HOME_PROPERTY);
		if (home != null) {
			IPath homePath = Path.fromOSString(home);
			if (homePath.toFile().isDirectory()) {
				return homePath;
			}
		}
		return null;
	}

}