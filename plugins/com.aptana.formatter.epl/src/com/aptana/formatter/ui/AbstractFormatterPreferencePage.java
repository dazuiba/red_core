/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package com.aptana.formatter.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;

import com.aptana.formatter.ui.internal.AbstractFormatterSelectionBlock;
import com.aptana.formatter.ui.internal.preferences.ScriptSourcePreviewerUpdater;
import com.aptana.theme.ColorManager;
import com.aptana.ui.ContributionExtensionManager;
import com.aptana.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import com.aptana.ui.preferences.AbstractOptionsBlock;
import com.aptana.ui.util.IStatusChangeListener;

public abstract class AbstractFormatterPreferencePage extends AbstractConfigurationBlockPropertyAndPreferencePage
{
	private static final Job[] NO_BUILD_JOBS = new Job[0];

	protected class FormatterSelectionBlock extends AbstractFormatterSelectionBlock
	{

		private ISharedTextColors fColorManager;

		public FormatterSelectionBlock(IStatusChangeListener context, IProject project,
				IWorkbenchPreferenceContainer container)
		{
			super(context, project, container);
			fColorManager = new ColorManager();
		}

		public void dispose()
		{
			fColorManager.dispose();
			super.dispose();
		}

		protected ContributionExtensionManager getExtensionManager()
		{
			return ScriptFormatterManager.getInstance();
		}

		protected IFormatterModifyDialogOwner createDialogOwner(IScriptFormatterFactory formatter)
		{
			return new FormatterModifyDialogOwner(formatter);
		}

		private class FormatterModifyDialogOwner implements IFormatterModifyDialogOwner
		{

			private final IScriptFormatterFactory formatter;

			public FormatterModifyDialogOwner(IScriptFormatterFactory formatter)
			{
				this.formatter = formatter;

			}

			public ISourceViewer createPreview(Composite composite)
			{
				return FormatterSelectionBlock.this.createSourcePreview(composite, formatter);
			}

			public Shell getShell()
			{
				return AbstractFormatterPreferencePage.this.getShell();
			}

			public IDialogSettings getDialogSettings()
			{
				return AbstractFormatterPreferencePage.this.getDialogSettings();
			}
		}

		/**
		 * @param composite
		 */
		public SourceViewer createSourcePreview(Composite composite, IScriptFormatterFactory factory)
		{
			IPreferenceStore generalTextStore = EditorsUI.getPreferenceStore();
			// TODO - Note that we pass the factory's preferences store and not calling to this.getPrefereceStore.
			// In case we decide to unify the preferences into the this plugin, we might need to change this.
			IPreferenceStore store = new ChainedPreferenceStore(new IPreferenceStore[] { factory.getPreferenceStore(),
					generalTextStore });
			SourceViewer fPreviewViewer = createPreviewViewer(composite, null, null, false, SWT.V_SCROLL | SWT.H_SCROLL
					| SWT.BORDER, store);
			if (fPreviewViewer == null)
			{
				return null;
			}
			SourceViewerConfiguration configuration = (SourceViewerConfiguration) factory.createSimpleSourceViewerConfiguration(
					fColorManager, store, null, false);
			fPreviewViewer.configure(configuration);
			if (fPreviewViewer.getTextWidget().getTabs() == 0)
			{
				fPreviewViewer.getTextWidget().setTabs(4);
			}
			new ScriptSourcePreviewerUpdater(fPreviewViewer, configuration, store);
			fPreviewViewer.setEditable(false);
			IDocument document = new Document();
			fPreviewViewer.setDocument(document);
			return fPreviewViewer;
		}

		/**
		 * @param parent
		 * @param verticalRuler
		 * @param overviewRuler
		 * @param showAnnotationsOverview
		 * @param styles
		 * @param store
		 * @return
		 */
		private ProjectionViewer createPreviewViewer(Composite parent, IVerticalRuler verticalRuler,
				IOverviewRuler overviewRuler, boolean showAnnotationsOverview, int styles, IPreferenceStore store)
		{
			ProjectionViewer viewer = new ProjectionViewer(parent, verticalRuler, overviewRuler,
					showAnnotationsOverview, styles);
			// TODO - Shalom - Attach the Theme colors (see AbstractThemeableEditor)
			return viewer;
		}

		protected String getPreferenceLinkMessage()
		{
			return FormatterMessages.FormatterPreferencePage_settingsLink;
		}

		protected void updatePreview()
		{
			if (fSelectedPreviewViewer != null)
			{
				IScriptFormatterFactory factory = getSelectedFormatter();
				IProfileManager manager = getProfileManager();
				FormatterPreviewUtils.updatePreview(fSelectedPreviewViewer, factory.getPreviewContent(), factory, manager
						.getSelected().getSettings());
			}
		}

		@Override
		protected Job[] createBuildJobs(IProject project)
		{
			return NO_BUILD_JOBS;
		}

	}

	protected AbstractOptionsBlock createOptionsBlock(IStatusChangeListener newStatusChangedListener, IProject project,
			IWorkbenchPreferenceContainer container)
	{
		return new FormatterSelectionBlock(newStatusChangedListener, project, container);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.ui.preferences.PropertyAndPreferencePage#supportsProjectSpecificOptions()
	 */
	@Override
	protected boolean supportsProjectSpecificOptions()
	{
		// For now, we disable any project-specific settings for the code formatter.
		// TODO - Re-enable this by removing this method if we decide to enable project-specific settings.
		return false;
	}

	protected abstract IDialogSettings getDialogSettings();

	protected String getHelpId()
	{
		return null;
	}

	protected void setDescription()
	{
		// empty
	}

	protected String getPreferencePageId()
	{
		return null;
	}

	protected String getProjectHelpId()
	{
		return null;
	}

	protected String getPropertyPageId()
	{
		return null;
	}

}