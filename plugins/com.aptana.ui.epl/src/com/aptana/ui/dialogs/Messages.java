package com.aptana.ui.dialogs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "com.aptana.ui.dialogs.messages"; //$NON-NLS-1$
	public static String ProjectSelectionDialog_filter;
	public static String ProjectSelectionDialog_projectSelectionDescription;
	public static String ProjectSelectionDialog_projectSelectionTitle;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}