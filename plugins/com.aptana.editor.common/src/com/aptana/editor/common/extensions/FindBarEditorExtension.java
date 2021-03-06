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
package com.aptana.editor.common.extensions;

import java.lang.ref.WeakReference;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.ITextEditor;

import com.aptana.editor.findbar.api.FindBarDecoratorFactory;
import com.aptana.editor.findbar.api.IFindBarDecorated;
import com.aptana.editor.findbar.api.IFindBarDecorator;

/**
 * Decorates an editor with a find bar (so, ctrl+F won't bring the default dialog, but a bar that appears similar to the
 * firefox search). The default usage for an editor that wants the find bar is: public MyEditor() { super();
 * fThemeableEditorFindBarExtension = new FindBarEditorExtension(this); } protected void createActions() {
 * super.createActions(); this.fThemeableEditorFindBarExtension.createFindBarActions(); } public void
 * createPartControl(Composite parent) { Composite findBarComposite = (Composite)
 * this.fThemeableEditorFindBarExtension.createFindBarComposite(parent); super.createPartControl(findBarComposite); }
 * public Object getAdapter(Class adapter) { Object adaptable =
 * this.fThemeableEditorFindBarExtension.getFindBarDecoratorAdapter(adapter); if(adaptable != null){ return adaptable; }
 * return super.getAdapter(adapter); }
 */
public class FindBarEditorExtension
{

	/**
	 * Keep a weak-reference to the editor which will have the find bar added.
	 */
	private WeakReference<ITextEditor> fEditor;

	private IFindBarDecorated fFindBarDecorated;
	private IFindBarDecorator fFindBarDecorator;

	/**
	 * @param editor
	 *            must either be an ITextEditor or provide adaptation to ITextEditor.class.
	 */
	public FindBarEditorExtension(IAdaptable editor)
	{
		ITextEditor textEditor;
		if (editor instanceof ITextEditor)
		{
			textEditor = (ITextEditor) editor;
		}
		else
		{
			textEditor = (ITextEditor) editor.getAdapter(ITextEditor.class);
			if (textEditor == null)
			{
				throw new AssertionError("The editor passed must either be an ITextEditor or provide adaptation to it.");
			}
		}
		this.fEditor = new WeakReference<ITextEditor>(textEditor);
	}

	/**
	 * Add a composite that will hold the find bar and the actual editor. When called, the return must be the new
	 * Composite parent to be used for creating the editor.
	 */
	public Composite createFindBarComposite(Composite parent)
	{
		Composite findBarComposite = getFindBarDecorator().createFindBarComposite(parent);
		return findBarComposite;
	}

	/**
	 * Create the find bar
	 */
	public void createFindBar(ISourceViewer sourceViewer)
	{
		getFindBarDecorator().createFindBar(sourceViewer);
	}

	/**
	 * Create the actions for the find bar in the original editor.
	 */
	public void createFindBarActions()
	{
		getFindBarDecorator().installActions();
	}

	/**
	 * Provide an adapter to IFindBarDecorator.
	 */
	@SuppressWarnings("rawtypes")
	public Object getFindBarDecoratorAdapter(Class adapter)
	{
		if (IFindBarDecorated.class.equals(adapter))
		{
			return getFindBarDecorated();
		}

		return null;
	}

	IFindBarDecorated getFindBarDecorated()
	{
		if (fFindBarDecorated == null)
		{
			fFindBarDecorated = new IFindBarDecorated()
			{
				public IFindBarDecorator getFindBarDecorator()
				{
					return FindBarEditorExtension.this.getFindBarDecorator();
				}
			};
		}
		return fFindBarDecorated;
	}

	private IFindBarDecorator getFindBarDecorator()
	{
		if (fFindBarDecorator == null)
		{
			fFindBarDecorator = FindBarDecoratorFactory.createFindBarDecorator(fEditor.get());
		}
		return fFindBarDecorator;
	}

	public void dispose()
	{
		this.fFindBarDecorated = null;
		this.fFindBarDecorator = null;
		if (this.fEditor != null)
		{
			this.fEditor.clear();
			this.fEditor = null;
		}
	}

}
