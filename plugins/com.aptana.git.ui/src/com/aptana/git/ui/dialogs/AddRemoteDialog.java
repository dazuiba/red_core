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
package com.aptana.git.ui.dialogs;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddRemoteDialog extends InputDialog
{

	private Text remoteURIText;
	protected String remoteURI;

	public AddRemoteDialog(final Shell parentShell, String remoteName, String defaultURI)
	{
		super(parentShell, Messages.AddRemoteDialog_AddRemoteDialog_Title,
				Messages.AddRemoteDialog_AddRemoteDialog_Message, remoteName, new IInputValidator()
				{

					public String isValid(String newText)
					{
						if (newText == null || newText.trim().length() == 0)
							return Messages.AddRemoteDialog_NonEmptyRemoteNameMessage;
						if (newText.trim().contains(" ") || newText.trim().contains("\t")) //$NON-NLS-1$ //$NON-NLS-2$
							return Messages.AddRemoteDialog_NoWhitespaceRemoteNameMessage;
						// TODO What else do we need to do to verify the remote name?
						return null;
					}
				});
		remoteURI = defaultURI;
	}

	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite composite = (Composite) super.createDialogArea(parent);

		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.AddRemoteDialog_RemoteURILabel);

		remoteURIText = new Text(composite, getInputTextStyle());
		remoteURIText.setText(remoteURI);
		remoteURIText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		remoteURIText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				remoteURI = remoteURIText.getText();
				// TODO Validate the remote URI (can't match existing remote)
			}
		});

		return composite;
	}

	public String getRemoteURL()
	{
		return remoteURI;
	}
}
