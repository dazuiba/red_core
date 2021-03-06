package com.aptana.ide.ui.io.dialogs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{

	private static final String BUNDLE_NAME = "com.aptana.ide.ui.io.dialogs.messages"; //$NON-NLS-1$

	public static String GenericConnectionPropertyDialog_CreateText;
	public static String GenericConnectionPropertyDialog_CreateTitle;
	public static String GenericConnectionPropertyDialog_EditText;
	public static String GenericConnectionPropertyDialog_EditTitle;
	public static String GenericConnectionPropertyDialog_FailedToCreate;
	public static String GenericConnectionPropertyDialog_Name;
	public static String GenericConnectionPropertyDialog_NewConnection;
	public static String GenericConnectionPropertyDialog_SpecifyShortcut;
	public static String GenericConnectionPropertyDialog_SpecifyValidAbsoluteURI;
	public static String GenericConnectionPropertyDialog_SpecifyValidURI;
	public static String GenericConnectionPropertyDialog_URI;
	public static String LocalConnectionPropertyDialog_CreateText;
	public static String LocalConnectionPropertyDialog_CreateTitle;
	public static String LocalConnectionPropertyDialog_EditText;
	public static String LocalConnectionPropertyDialog_EditTitle;
	public static String LocalConnectionPropertyDialog_FailedToCreate;
	public static String LocalConnectionPropertyDialog_LocalPath;
	public static String LocalConnectionPropertyDialog_LocationNotExist;
	public static String LocalConnectionPropertyDialog_NewShortcut;
	public static String LocalConnectionPropertyDialog_ShortcutName;
	public static String LocalConnectionPropertyDialog_SpecifyShortcutName;
	public static String WorkspaceConnectionPropertyDialog_CreateText;
	public static String WorkspaceConnectionPropertyDialog_CreateTitle;
	public static String WorkspaceConnectionPropertyDialog_EditText;
	public static String WorkspaceConnectionPropertyDialog_EditTitle;
	public static String WorkspaceConnectionPropertyDialog_FailedToCreate;
	public static String WorkspaceConnectionPropertyDialog_NewShortcut;
	public static String WorkspaceConnectionPropertyDialog_ResourceNotExist;
	public static String WorkspaceConnectionPropertyDialog_ShortcutName;
	public static String WorkspaceConnectionPropertyDialog_SpecifyLocation;
	public static String WorkspaceConnectionPropertyDialog_SpecifyShortcutName;
	public static String WorkspaceConnectionPropertyDialog_WorkspacePath;

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
