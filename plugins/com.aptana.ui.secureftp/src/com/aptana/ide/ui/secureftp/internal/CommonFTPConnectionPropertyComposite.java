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

package com.aptana.ide.ui.secureftp.internal;

import java.io.File;
import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import com.aptana.core.util.StringUtil;
import com.aptana.filesystem.secureftp.IFTPSConnectionPoint;
import com.aptana.filesystem.secureftp.ISFTPConnectionPoint;
import com.aptana.filesystem.secureftp.SFTPConnectionPoint;
import com.aptana.filesystem.secureftp.SecureUtils;
import com.aptana.ide.core.io.ConnectionPointType;
import com.aptana.ide.core.io.CoreIOPlugin;
import com.aptana.ide.core.io.IBaseRemoteConnectionPoint;
import com.aptana.filesystem.ftp.IBaseFTPConnectionPoint;
import com.aptana.filesystem.ftp.Policy;
import com.aptana.ide.ui.ftp.internal.FTPAdvancedOptionsComposite;
import com.aptana.ide.ui.ftp.internal.FTPConnectionPropertyComposite;
import com.aptana.ide.ui.ftp.internal.IOptionsComposite;
import com.aptana.ide.ui.io.dialogs.IDialogConstants;
import com.aptana.ide.ui.secureftp.dialogs.Messages;
import com.aptana.ui.UIUtils;

/**
 * 
 * @author Max Stepanov
 *
 */
@SuppressWarnings("restriction")
public class CommonFTPConnectionPropertyComposite extends FTPConnectionPropertyComposite
{

	private static final ConnectionPointType[] PROTOCOL_TYPES = new ConnectionPointType[] {
			CoreIOPlugin.getConnectionPointManager().getType(IBaseFTPConnectionPoint.TYPE_FTP),
			CoreIOPlugin.getConnectionPointManager().getType(IFTPSConnectionPoint.TYPE_FTPS),
			CoreIOPlugin.getConnectionPointManager().getType(ISFTPConnectionPoint.TYPE_SFTP) };

	private Combo protocolButton;
	private Composite keyAuthComposite;
	private Button keyAuthButton;
	private Label keyPathLabel;
	private Composite[] advancedOptionsComposites;

	private ConnectionPointType connectionType;
	private Listener listener;

	public CommonFTPConnectionPropertyComposite(Composite parent, int style,
			IBaseRemoteConnectionPoint connectionPoint, Listener listener)
	{
		super(parent, style, connectionPoint, listener);
		this.listener = listener;
	}

	@Override
	public ConnectionPointType getConnectionPointType()
	{
		if (connectionType != null)
		{
			return connectionType;
		}
		return super.getConnectionPointType();
	}

	public void setConnectionPointType(ConnectionPointType type)
	{
		changeProtocolType(type);
	}

	@Override
	public void setConnectionPoint(IBaseRemoteConnectionPoint connectionPoint)
	{
		super.setConnectionPoint(connectionPoint);
		if (ftpConnectionPoint != null)
		{
			setConnectionPointType(CoreIOPlugin.getConnectionPointManager().getType(ftpConnectionPoint));
		}
	}

	@Override
	public boolean isValid()
	{
		if (!super.isValid())
		{
			return false;
		}
		String message = null;
		if (keyAuthButton.getSelection())
		{
			IPath keyFilePath = Path.fromPortableString(keyPathLabel.getText());
			try
			{
				boolean passphraseProtected = SecureUtils.isKeyPassphraseProtected(keyFilePath.toFile());
				char[] password = passwordText.getText().toCharArray();
				if (passphraseProtected && password.length > 0)
				{
					if (!SecureUtils.isPassphraseValid(keyFilePath.toFile(), password))
					{
						message = Messages.CommonFTPConnectionPointPropertyDialog_IncorrectPassphrase;
					}
				}
			}
			catch (CoreException e)
			{
				message = e.getLocalizedMessage();
			}
		}
		listener.error(message);
		return message == null;
	}

	@Override
	protected void createSiteSection(Composite parent)
	{
		super.createSiteSection(parent);

		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(GridDataFactory.swtDefaults().hint(
				new PixelConverter(label).convertHorizontalDLUsToPixels(IDialogConstants.LABEL_WIDTH), SWT.DEFAULT)
				.create());
		label.setText(StringUtil.makeFormLabel(Messages.CommonFTPConnectionPointPropertyDialog_Protocol));

		protocolButton = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
		String[] items = new String[PROTOCOL_TYPES.length];
		for (int i = 0; i < items.length; ++i)
		{
			items[i] = PROTOCOL_TYPES[i].getName();
		}
		protocolButton.setItems(items);
		updateProtocolButton();
		protocolButton.setLayoutData(GridDataFactory.swtDefaults().hint(
				Math.max(new PixelConverter(protocolButton)
						.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH), protocolButton.computeSize(
						SWT.DEFAULT, SWT.DEFAULT, true).x), SWT.DEFAULT).create());

		/* -- */
		protocolButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				changeProtocolType(PROTOCOL_TYPES[protocolButton.getSelectionIndex()]);
			}
		});
	}

	@Override
	protected void createPasswordSection(Composite parent)
	{
		keyAuthComposite = new Composite(parent, SWT.NONE);
		keyAuthComposite.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).grab(true, false).create());
		makeVisible(keyAuthComposite, false);
		keyAuthComposite.setLayout(GridLayoutFactory.fillDefaults().spacing(LayoutConstants.getSpacing().x, 0)
				.numColumns(2).create());

		/* row 1 */
		Label label = new Label(keyAuthComposite, SWT.NONE);
		label.setLayoutData(GridDataFactory.swtDefaults().hint(
				new PixelConverter(label).convertHorizontalDLUsToPixels(IDialogConstants.LABEL_WIDTH), SWT.DEFAULT)
				.create());

		keyAuthButton = new Button(keyAuthComposite, SWT.CHECK);
		keyAuthButton.setLayoutData(GridDataFactory.fillDefaults().create());
		keyAuthButton.setText(Messages.CommonFTPConnectionPointPropertyDialog_UsePublicKeyAuthentication);

		/* row 2 */
		label = new Label(keyAuthComposite, SWT.NONE);
		label.setLayoutData(GridDataFactory.swtDefaults().hint(
				new PixelConverter(label).convertHorizontalDLUsToPixels(IDialogConstants.LABEL_WIDTH), SWT.DEFAULT)
				.create());

		keyPathLabel = new Label(keyAuthComposite, SWT.NONE);
		keyPathLabel.setLayoutData(GridDataFactory.fillDefaults().create());
		keyPathLabel.setFont(smallFont);
		keyPathLabel.setText(Messages.CommonFTPConnectionPointPropertyDialog_NoPrivateKeySelected);

		super.createPasswordSection(parent);
		updateKeyAuth();

		/* -- */
		keyAuthButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				changeKeyAuthentication();
				validate();
			}
		});
	}

	@Override
	protected void createAdvancedOptions(Composite parent)
	{
		advancedOptionsComposites = new Composite[] { new FTPAdvancedOptionsComposite(parent, SWT.NONE, this),
				new FTPSAdvancedOptionsComposite(parent, SWT.NONE, this),
				new SFTPAdvancedOptionsComposite(parent, SWT.NONE, this) };
		for (Composite composite : advancedOptionsComposites)
		{
			composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			makeVisible(composite, false);
		}
		updateAdvancedOptions();
	}

	@Override
	protected String getAuthId(IBaseRemoteConnectionPoint connectionPoint)
	{
		if (ISFTPConnectionPoint.TYPE_SFTP.equals(getConnectionPointType().getType()))
		{
			if (keyAuthButton.getSelection())
			{
				return Policy.generateAuthId(
						getConnectionPointType().getType().toUpperCase() + "/PUBLICKEY", connectionPoint);} //$NON-NLS-1$
		}
		return super.getAuthId(connectionPoint);
	}

	@Override
	protected void loadPropertiesFrom(IBaseRemoteConnectionPoint connectionPoint)
	{
		if (connectionPoint instanceof SFTPConnectionPoint)
		{
			SFTPConnectionPoint sftpConnectionPoint = (SFTPConnectionPoint) connectionPoint;
			IPath keyFilePath = sftpConnectionPoint.getKeyFilePath();
			makeVisible(passwordLabel, true);
			makeVisible(passwordText, true);
			makeVisible(savePasswordButton, true);
			if (keyFilePath != null && !keyFilePath.isEmpty())
			{
				keyAuthButton.setSelection(true);
				keyPathLabel.setText(keyFilePath.toOSString());
				passwordLabel.setText(StringUtil
						.makeFormLabel(Messages.CommonFTPConnectionPointPropertyDialog_Passphrase));
				try
				{
					boolean passphraseProtected = SecureUtils.isKeyPassphraseProtected(keyFilePath.toFile());
					makeVisible(passwordLabel, passphraseProtected);
					makeVisible(passwordText, passphraseProtected);
					makeVisible(savePasswordButton, passphraseProtected);
				}
				catch (CoreException e)
				{
				}
			}
			updateLayout();
		}
		super.loadPropertiesFrom(connectionPoint);
	}

	@Override
	protected boolean savePropertiesTo(IBaseRemoteConnectionPoint connectionPoint)
	{
		boolean updated = false;
		if (connectionPoint instanceof SFTPConnectionPoint)
		{
			SFTPConnectionPoint sftpConnectionPoint = (SFTPConnectionPoint) connectionPoint;
			if (keyAuthButton.getSelection())
			{
				IPath keyFilePath = Path.fromPortableString(keyPathLabel.getText());
				if (!connectionPoint.getPath().equals(keyFilePath))
				{
					sftpConnectionPoint.setKeyFilePath(keyFilePath);
					updated = true;
				}
			}
		}
		return super.savePropertiesTo(connectionPoint) || updated;
	}

	private void updateProtocolButton()
	{
		int currentIndex = Arrays.asList(PROTOCOL_TYPES).indexOf(getConnectionPointType());
		if (currentIndex == -1)
		{
			currentIndex = 0;
		}
		protocolButton.setText(PROTOCOL_TYPES[currentIndex].getName());
	}

	private void updateKeyAuth()
	{
		boolean keyAuthEnabled = ISFTPConnectionPoint.TYPE_SFTP.equals(getConnectionPointType().getType());
		makeVisible(keyAuthComposite, keyAuthEnabled);
		keyAuthButton.setSelection(false);
		changeKeyAuthentication();
	}

	private void updateAdvancedOptions()
	{
		int currentIndex = Arrays.asList(PROTOCOL_TYPES).indexOf(getConnectionPointType());
		if (currentIndex == -1)
		{
			currentIndex = 0;
		}
		for (Composite composite : advancedOptionsComposites)
		{
			makeVisible(composite, false);
		}
		Composite advancedOptionComposite = advancedOptionsComposites[currentIndex];
		makeVisible(advancedOptionComposite, true);
		advancedOptions = (IOptionsComposite) advancedOptionComposite;
	}

	private void changeProtocolType(ConnectionPointType newType)
	{
		if (newType == null || newType == getConnectionPointType())
		{
			return;
		}
		connectionType = newType;
		updateProtocolButton();
		updateKeyAuth();
		updateAdvancedOptions();

		updateLayout();

		ftpConnectionPoint = getOrCreateConnectionPoint(connectionType);
		advancedOptions.loadPropertiesFrom(ftpConnectionPoint);
	}

	private void changeKeyAuthentication()
	{
		boolean enabled = keyAuthButton.getSelection();

		if (enabled)
		{
			while (true)
			{
				FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
				dlg.setText(Messages.CommonFTPConnectionPointPropertyDialog_SpecifyPrivateKey);
				String ssh_home = SecureUtils.getSSH_HOME();
				if (ssh_home != null && ssh_home.length() != 0)
				{
					File dir = new File(ssh_home);
					if (dir.exists() && dir.isDirectory())
					{
						dlg.setFilterPath(ssh_home);
						for (String key : SecureUtils.getPrivateKeys())
						{
							if (new File(dir, key).exists())
							{
								dlg.setFileName(key);
								break;
							}
						}
					}
				}
				String keyFilePath = dlg.open();
				if (keyFilePath == null)
				{
					keyAuthButton.setSelection(false);
					return;
				}
				try
				{
					boolean passphraseProtected = SecureUtils.isKeyPassphraseProtected(new File(keyFilePath));
					makeVisible(passwordLabel, passphraseProtected);
					makeVisible(passwordText, passphraseProtected);
					makeVisible(savePasswordButton, passphraseProtected);
				}
				catch (CoreException e)
				{
					UIUtils.showErrorMessage(Messages.CommonFTPConnectionPointPropertyDialog_ERR_PrivateKey, e
							.getLocalizedMessage());
					continue;
				}
				keyPathLabel.setText(keyFilePath);
				break;
			}
			passwordText.setText(""); //$NON-NLS-1$
		}
		else
		{
			keyPathLabel.setText(Messages.CommonFTPConnectionPointPropertyDialog_NoPrivateKeySelected);
			makeVisible(passwordLabel, true);
			makeVisible(passwordText, true);
			makeVisible(savePasswordButton, true);
		}
		updateLayout();
		passwordLabel.setText(StringUtil
				.makeFormLabel(enabled ? Messages.CommonFTPConnectionPointPropertyDialog_Passphrase
						: Messages.CommonFTPConnectionPointPropertyDialog_Password));
		savePasswordButton.setSelection(false);
	}

	private void updateLayout()
	{
		Composite parent = getParent();
		if (parent != null)
		{
			parent.layout(true, true);
			if (listener != null)
			{
				listener.layoutShell();
			}
		}
	}

	private static void makeVisible(Control control, boolean visible)
	{
		control.setVisible(visible);
		((GridData) control.getLayoutData()).exclude = !visible;
	}
}
