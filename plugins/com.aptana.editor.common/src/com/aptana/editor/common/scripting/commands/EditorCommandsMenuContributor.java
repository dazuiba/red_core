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
package com.aptana.editor.common.scripting.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;
import org.eclipse.ui.texteditor.ITextEditor;

import com.aptana.core.IScopeReference;
import com.aptana.core.util.CollectionsUtil;
import com.aptana.editor.common.CommonEditorPlugin;
import com.aptana.editor.common.ITopContentTypesProvider;
import com.aptana.editor.common.scripting.IContentTypeTranslator;
import com.aptana.editor.common.scripting.QualifiedContentType;
import com.aptana.scripting.model.AbstractElement;
import com.aptana.scripting.model.BundleElement;
import com.aptana.scripting.model.BundleManager;
import com.aptana.scripting.model.CommandElement;
import com.aptana.scripting.model.CommandResult;
import com.aptana.scripting.model.InvocationType;
import com.aptana.scripting.model.MenuElement;
import com.aptana.scripting.model.SnippetElement;
import com.aptana.scripting.model.filters.IModelFilter;
import com.aptana.scripting.model.filters.NotFilter;
import com.aptana.scripting.model.filters.ScopeFilter;

/**
 * This contributes the menus for editor scope to the Commands menu.
 * 
 * @author schitale
 */
public class EditorCommandsMenuContributor extends ContributionItem
{

	private static final String TAB = "\u00bb"; //$NON-NLS-1$

	public EditorCommandsMenuContributor()
	{
	}

	public EditorCommandsMenuContributor(String id)
	{
		super(id);
	}

	@Override
	public void fill(Menu menu, int index)
	{
		super.fill(menu, index);
		IEvaluationService evaluationService = (IEvaluationService) PlatformUI.getWorkbench().getService(
				IEvaluationService.class);
		if (evaluationService != null)
		{
			IEvaluationContext currentState = evaluationService.getCurrentState();
			Object activePart = currentState.getVariable(ISources.ACTIVE_PART_NAME);
			if (activePart instanceof ITextEditor)
			{
				fill(menu, (ITextEditor) activePart, this);
			}
			else
			{
				fill(menu, null, this);
			}
		}
	}

	public static void fill(Menu menu, ITextEditor textEditor)
	{
		fill(menu, textEditor, null);
	}

	public static void fill(final Menu menu, ITextEditor textEditor, IContributionItem contributionItem)
	{
		String scope = null;
		List<MenuElement> menusFromScopeList = new LinkedList<MenuElement>();
		MenuElement[] menusFromScope;
		MenuElement[] menusFromOtherScopes = null;
		if (textEditor == null)
		{
			IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
			if (part != null)
			{
				IScopeReference scopeRef = (IScopeReference) part.getAdapter(IScopeReference.class);
				if (scopeRef != null)
				{
					scope = scopeRef.getScopeId();
				}
			}
			menusFromScope = BundleManager.getInstance().getMenus(new IModelFilter()
			{

				public boolean include(AbstractElement element)
				{
					return true;
				}
			});
			if (menusFromScope.length > 0)
			{
				menusFromScopeList.addAll(Arrays.asList(menusFromScope));
			}
		}
		else
		{
			try
			{
				IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
				int caretOffset = TextEditorUtils.getCaretOffset(textEditor);
				// Get the scope at caret offset
				scope = CommonEditorPlugin.getDefault().getDocumentScopeManager()
						.getScopeAtOffset(document, caretOffset);
			}
			catch (BadLocationException e)
			{
				CommonEditorPlugin.logError(e);
			}

			// First pull all possible menus from the current caret position's scopes
			if (scope != null)
			{
				ScopeFilter filter = new ScopeFilter(scope);
				menusFromScope = BundleManager.getInstance().getMenus(filter);
				if (menusFromScope.length > 0)
				{
					menusFromScopeList.addAll(Arrays.asList(menusFromScope));
				}
			}

			// Next we get all possible scopes from the top level content type provider
			SourceViewerConfiguration sourceViewerConfiguration = (SourceViewerConfiguration) textEditor
					.getAdapter(SourceViewerConfiguration.class);
			if (sourceViewerConfiguration instanceof ITopContentTypesProvider)
			{
				String[][] topContentTypes = ((ITopContentTypesProvider) sourceViewerConfiguration)
						.getTopContentTypes();
				List<String> topLevelContentTypesList = new LinkedList<String>();
				for (String[] topContentType : topContentTypes)
				{
					QualifiedContentType qualifiedContentType = new QualifiedContentType(topContentType);
					String contentType = getContentTypeTranslator().translate(qualifiedContentType).toString();
					topLevelContentTypesList.add(contentType);
				}
				if (topLevelContentTypesList.size() > 0)
				{
					String[] topLevelContentTypes = new String[topLevelContentTypesList.size()];
					topLevelContentTypesList.toArray(topLevelContentTypes);

					// Get menus
					ScopeFilter topLevelContentTypesFilter = new ScopeFilter(topLevelContentTypes);
					menusFromScope = BundleManager.getInstance().getMenus(topLevelContentTypesFilter);
					if (menusFromScope.length > 0)
					{
						// Collect
						menusFromScopeList.addAll(Arrays.asList(menusFromScope));
					}

					// Next we use a negative filter to get menus that belong to scopes
					// that do not match the top level scopes. We will use this
					// later to build the "Other" menu.
					NotFilter notFilter = new NotFilter(topLevelContentTypesFilter);
					menusFromOtherScopes = BundleManager.getInstance().getMenus(notFilter);
				}
			}
		}

		// Do we have some menus?
		if (menusFromScopeList.size() > 0)
		{
			// Remove duplicates and sort
			CollectionsUtil.removeDuplicates(menusFromScopeList);
			Collections.sort(menusFromScopeList, new Comparator<MenuElement>()
			{
				public int compare(MenuElement menuElement1, MenuElement menuElement2)
				{
					return menuElement1.getDisplayName().compareTo(menuElement2.getDisplayName());
				}
			});
			menusFromScope = new MenuElement[menusFromScopeList.size()];
			menusFromScopeList.toArray(menusFromScope);

			// Now build the menu
			buildMenu(menu, menusFromScope, textEditor, scope, contributionItem);
		}

		// Are there any menus that belong to scopes other than top level scopes
		if (menusFromOtherScopes != null && menusFromOtherScopes.length > 0)
		{
			// Build the "Other" menu
			MenuItem separatorMenuItem = new MenuItem(menu, SWT.SEPARATOR);
			separatorMenuItem.setData(contributionItem);

			MenuItem menuItemForOtherScopes = new MenuItem(menu, SWT.CASCADE);
			menuItemForOtherScopes.setData(contributionItem);
			menuItemForOtherScopes.setText(Messages.EditorCommandsMenuContributor_CommandsForOtherScopes);

			Menu menuForOtherScopes = new Menu(menu);
			menuItemForOtherScopes.setMenu(menuForOtherScopes);
			buildMenu(menuForOtherScopes, menusFromOtherScopes, textEditor, scope, contributionItem);
		}
	}

	protected static IContentTypeTranslator getContentTypeTranslator()
	{
		return CommonEditorPlugin.getDefault().getContentTypeTranslator();
	}

	/**
	 * This recursively builds the menu contribution.
	 * 
	 * @param menu
	 * @param menusFromScope
	 * @param textEditor
	 * @param scope
	 */
	private static void buildMenu(Menu menu, MenuElement[] menusFromScope, final ITextEditor textEditor, String scope,
			IContributionItem contributionItem)
	{
		for (MenuElement menuForScope : menusFromScope)
		{
			if (menuForScope.isHierarchicalMenu())
			{
				MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
				menuItem.setData(contributionItem);
				menuItem.setText(menuForScope.getDisplayName());

				Menu menuForMenuForScope = new Menu(menu);
				menuItem.setMenu(menuForMenuForScope);

				// Recursive
				buildMenu(menuForMenuForScope, menuForScope.getChildren(), textEditor, scope, contributionItem);
			}
			else if (menuForScope.isSeparator())
			{
				MenuItem separatorMenuItem = new MenuItem(menu, SWT.SEPARATOR);
				separatorMenuItem.setData(contributionItem);
			}
			else
			{
				final CommandElement command = menuForScope.getCommand();

				// A command attached to the menu item may not be executable on
				// the current platform in which case we skip the menu item
				if (command != null && (!command.isExecutable()))
				{
					continue;
				}
				final MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
				menuItem.setData(contributionItem);

				String displayName = menuForScope.getDisplayName();
				String acceleratorText = ""; //$NON-NLS-1$
				if (command != null)
				{
					KeySequence[] keySequences = command.getKeySequences();
					if (keySequences != null && keySequences.length > 0)
					{
						KeySequence keySequence = keySequences[0];
						acceleratorText = "\t" + keySequence.format(); //$NON-NLS-1$
					}
					if (command instanceof SnippetElement || keySequences == null || keySequences.length == 0)
					{
						String[] triggers = command.getTriggers();
						if (triggers != null && triggers.length > 0)
						{
							// Use first trigger
							displayName += " " + triggers[0] + getTabChar(); //$NON-NLS-1$
						}
					}
				}
				menuItem.setText(displayName + acceleratorText);
				menuItem.addSelectionListener(new SelectionListener()
				{
					public void widgetSelected(SelectionEvent e)
					{
						if (command == null)
						{
							// There is no associated command. Show a message to the user.
							MessageDialog.openError(menuItem.getParent().getShell(),
									Messages.EditorCommandsMenuContributor_TITLE_CommandNotDefined, Messages.bind(
											Messages.EditorCommandsMenuContributor_MSG_CommandNotDefined,
											menuItem.getText()));
						}
						else
						{
							CommandResult commandResult = CommandExecutionUtils.executeCommand(command,
									InvocationType.MENU, textEditor);
							CommandExecutionUtils.processCommandResult(command, commandResult, textEditor);
						}
					}

					public void widgetDefaultSelected(SelectionEvent e)
					{
					}
				});
				// Enable the menu item if:
				// 1. There is no associated command so that we can show a message to the user when they invoke the menu
				// item
				// 2. The command did not specify the scope
				// 3. The command specified the scope and it matches the current scope
				menuItem.setEnabled(command == null || command.getScope() == null
						|| command.getScopeSelector().matches(scope));
			}
		}
		if (menusFromScope.length > 0)
		{
			MenuElement menuForScope = menusFromScope[0];
			// if we're inside a bundle's main menu
			if (menuForScope.getParent() != null && menuForScope.getParent().getParent() == null)
			{
				MenuItem separatorMenuItem = new MenuItem(menu, SWT.SEPARATOR);
				separatorMenuItem.setData(contributionItem);

				final MenuItem editBundleItem = new MenuItem(menu, SWT.PUSH);
				editBundleItem.setData(contributionItem);
				editBundleItem.setText(Messages.EditorCommandsMenuContributor_LBL_EditBundle);
				final BundleElement bundleElement = menuForScope.getOwningBundle();
				editBundleItem.addSelectionListener(new SelectionListener()
				{
					public void widgetSelected(SelectionEvent e)
					{
						editBundle(bundleElement);
					}

					public void widgetDefaultSelected(SelectionEvent e)
					{
					}
				});
			}
		}
	}

	private static String getTabChar()
	{
		if (Platform.getOS().equals(Platform.OS_MACOSX))
		{
			return "\u21E5"; // char which looks like: ->| //$NON-NLS-1$ 
		}
		return TAB;
	}

	protected static void editBundle(final BundleElement owningBundle)
	{
		Job job = new EditBundleJob(owningBundle);
		job.schedule();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.action.ContributionItem#isDynamic()
	 */
	@Override
	public boolean isDynamic()
	{
		return true;
	}
}
