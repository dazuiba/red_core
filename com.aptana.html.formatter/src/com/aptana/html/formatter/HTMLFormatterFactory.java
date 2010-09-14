package com.aptana.html.formatter;

import java.net.URL;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.texteditor.ITextEditor;

import com.aptana.editor.common.AbstractThemeableEditor;
import com.aptana.editor.html.HTMLSourceViewerConfiguration;
import com.aptana.formatter.AbstractScriptFormatterFactory;
import com.aptana.formatter.ui.IFormatterModifyDialog;
import com.aptana.formatter.ui.IFormatterModifyDialogOwner;
import com.aptana.formatter.ui.IScriptFormatter;
import com.aptana.ui.preferences.PreferenceKey;

/**
 * HTML formatter factory
 * 
 * @author Shalom Gibly <sgibly@aptana.com>
 */
public class HTMLFormatterFactory extends AbstractScriptFormatterFactory
{
	private static final PreferenceKey FORMATTER_PREF_KEY = new PreferenceKey(HTMLFormatterPlugin.PLUGIN_ID,
			HTMLFormatterConstants.FORMATTER_ID);
	
	private static final String FORMATTER_PREVIEW_FILE = "formatterPreview.html"; //$NON-NLS-1$

	private static final String[] KEYS = {
	// RubyFormatterConstants.FORMATTER_TAB_CHAR,
	// RubyFormatterConstants.FORMATTER_INDENTATION_SIZE, RubyFormatterConstants.FORMATTER_TAB_SIZE,
	// RubyFormatterConstants.INDENT_CLASS, RubyFormatterConstants.INDENT_MODULE,
	// RubyFormatterConstants.INDENT_METHOD, RubyFormatterConstants.INDENT_BLOCKS,
	// RubyFormatterConstants.INDENT_IF, RubyFormatterConstants.INDENT_CASE, RubyFormatterConstants.INDENT_WHEN,
	// RubyFormatterConstants.LINES_FILE_AFTER_REQUIRE, RubyFormatterConstants.LINES_FILE_BETWEEN_MODULE,
	// RubyFormatterConstants.LINES_FILE_BETWEEN_CLASS, RubyFormatterConstants.LINES_FILE_BETWEEN_METHOD,
	// RubyFormatterConstants.LINES_BEFORE_FIRST, RubyFormatterConstants.LINES_BEFORE_MODULE,
	// RubyFormatterConstants.LINES_BEFORE_CLASS, RubyFormatterConstants.LINES_BEFORE_METHOD,
	// RubyFormatterConstants.LINES_PRESERVE, RubyFormatterConstants.WRAP_COMMENTS,
	// RubyFormatterConstants.WRAP_COMMENTS_LENGTH
	};

	public PreferenceKey[] getPreferenceKeys()
	{
		final PreferenceKey[] result = new PreferenceKey[KEYS.length];
		for (int i = 0; i < KEYS.length; ++i)
		{
			final String key = KEYS[i];
			result[i] = new PreferenceKey(HTMLFormatterPlugin.PLUGIN_ID, key);
		}
		return result;
	}

	public IScriptFormatter createFormatter(String lineDelimiter, Map<String, String> preferences)
	{
		return new HTMLFormatter(preferences);
	}

	public URL getPreviewContent()
	{
		return getClass().getResource(FORMATTER_PREVIEW_FILE);
	}

	public IFormatterModifyDialog createDialog(IFormatterModifyDialogOwner dialogOwner)
	{
		// TODO
		// return new RubyFormatterModifyDialog(dialogOwner, this);
		return null;
	}

	@Override
	public SourceViewerConfiguration createSimpleSourceViewerConfiguration(ISharedTextColors colorManager,
			IPreferenceStore preferenceStore, ITextEditor editor, boolean configureFormatter)
	{
		return new HTMLSourceViewerConfiguration(preferenceStore, (AbstractThemeableEditor) editor);
	}

	@Override
	public PreferenceKey getFormatterPreferenceKey()
	{
		return FORMATTER_PREF_KEY;
	}

	@Override
	public IPreferenceStore getPreferenceStore()
	{
		// TODO Auto-generated method stub
		return HTMLFormatterPlugin.getDefault().getPreferenceStore();
	}
}