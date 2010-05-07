package com.aptana.editor.css.contentassist;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "com.aptana.editor.css.contentassist.messages"; //$NON-NLS-1$
	public static String CSSModelFormatter_Supported_User_Agents;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}