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
package com.aptana.deploy.internal.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.aptana.deploy.Activator;
import com.aptana.deploy.preferences.DeployPreferenceUtil;
import com.aptana.ide.core.io.IBaseRemoteConnectionPoint;
import com.aptana.ide.core.io.IConnectionPoint;
import com.aptana.ide.syncing.core.ISiteConnection;
import com.aptana.ide.syncing.core.SiteConnectionUtils;
import com.aptana.ide.syncing.ui.preferences.IPreferenceConstants.SyncDirection;
import com.aptana.ide.syncing.ui.preferences.SyncPreferenceUtil;
import com.aptana.ide.ui.ftp.internal.FTPConnectionPropertyComposite;

@SuppressWarnings("restriction")
public class FTPDeployWizardPage extends WizardPage implements FTPConnectionPropertyComposite.Listener
{

	public static final String NAME = "FTPDeployment"; //$NON-NLS-1$
	private static final String ICON_PATH = "icons/ftp.png"; //$NON-NLS-1$

	private IProject project;
	private FTPDeployComposite ftpConnectionComposite;
	private IBaseRemoteConnectionPoint connectionPoint;

	protected FTPDeployWizardPage(IProject project)
	{
		super(NAME, Messages.FTPDeployWizardPage_Title, Activator.getImageDescriptor(ICON_PATH));
		this.project = project;
		// checks if the project already has an associated FTP connection and fills the info automatically if one exists
		ISiteConnection[] sites = SiteConnectionUtils.findSitesForSource(project, true);
		String lastConnection = DeployPreferenceUtil.getDeployEndpoint(project);
		IConnectionPoint connection;
		for (ISiteConnection site : sites)
		{
			connection = site.getDestination();
			if ((connection != null && connection.getName().equals(lastConnection))
					|| (lastConnection == null && connection instanceof IBaseRemoteConnectionPoint))
			{
				connectionPoint = (IBaseRemoteConnectionPoint) connection;
				break;
			}
		}
	}

	public IBaseRemoteConnectionPoint getConnectionPoint()
	{
		return ftpConnectionComposite.getConnectionPoint();
	}

	public boolean isAutoSyncSelected()
	{
		return ftpConnectionComposite.isAutoSyncSelected();
	}

	public SyncDirection getSyncDirection()
	{
		return ftpConnectionComposite.getSyncDirection();
	}

	public boolean completePage()
	{
		boolean complete = ftpConnectionComposite.completeConnection();
		// persists the auto-sync setting
		boolean autoSync = isAutoSyncSelected();
		SyncPreferenceUtil.setAutoSync(project, autoSync);
		if (autoSync)
		{
			SyncPreferenceUtil.setAutoSyncDirection(project, getSyncDirection());
		}

		return complete;
	}

	public void createControl(Composite parent)
	{
		ftpConnectionComposite = new FTPDeployComposite(parent, SWT.NONE, connectionPoint, this);
		ftpConnectionComposite
				.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
		setControl(ftpConnectionComposite);

		initializeDialogUnits(parent);
		Dialog.applyDialogFont(ftpConnectionComposite);

		ftpConnectionComposite.validate();
	}

	@Override
	public IWizardPage getNextPage()
	{
		return null;
	}

	public boolean close()
	{
		return false;
	}

	public void error(String message)
	{
		if (message == null)
		{
			setErrorMessage(null);
			setMessage(null);
		}
		else
		{
			setErrorMessage(message);
		}
		setPageComplete(message == null);
	}

	public void layoutShell()
	{
	}

	public void lockUI(boolean lock)
	{
	}

	public void setValid(boolean valid)
	{
	}
}
