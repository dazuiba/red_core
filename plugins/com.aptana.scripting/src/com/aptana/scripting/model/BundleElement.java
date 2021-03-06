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
package com.aptana.scripting.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jruby.RubyRegexp;

import com.aptana.core.util.StringUtil;
import com.aptana.parsing.io.SourcePrinter;
import com.aptana.scope.ScopeSelector;
import com.aptana.scripting.model.ProjectTemplate.Type;

public class BundleElement extends AbstractElement
{
	private String _author;
	private String _copyright;
	private String _description;
	private String _license;
	private String _licenseUrl;
	private String _repository;

	private File _bundleDirectory;
	private BundlePrecedence _bundlePrecedence;
	private List<MenuElement> _menus;
	private List<CommandElement> _commands;
	private List<EnvironmentElement> _envs;
	private boolean _visible;

	private Object menuLock = new Object();
	private Object commandLock = new Object();
	private Object envLock = new Object();

	private Map<String, String> _fileTypeRegistry;
	private List<String> fileTypes;

	private Map<ScopeSelector, RubyRegexp> _foldingStartMarkers;
	private Map<ScopeSelector, RubyRegexp> _foldingStopMarkers;

	private Map<ScopeSelector, RubyRegexp> _increaseIndentMarkers;
	private Map<ScopeSelector, RubyRegexp> _decreaseIndentMarkers;

	private List<ProjectTemplate> _projectTemplates;

	/**
	 * Bundle
	 * 
	 * @param path
	 */
	public BundleElement(String path)
	{
		super(path);

		if (path != null)
		{
			// calculate bundle's root directory
			File pathFile = new File(path);
			File parentDirectory = (pathFile.isFile()) ? pathFile.getParentFile() : pathFile;
			String parentName = parentDirectory.getName();

			if (BundleManager.COMMANDS_DIRECTORY_NAME.equals(parentName)
					|| BundleManager.SNIPPETS_DIRECTORY_NAME.equals(parentName))
			{
				parentDirectory = parentDirectory.getParentFile();
			}

			this._bundleDirectory = parentDirectory.getAbsoluteFile();
		}

		// calculate the bundle scope
		this._bundlePrecedence = BundleManager.getInstance().getBundlePrecedence(path);
	}

	/**
	 * addCommand
	 * 
	 * @param command
	 */
	public void addCommand(CommandElement command)
	{
		if (command != null)
		{
			synchronized (commandLock)
			{
				if (this._commands == null)
				{
					this._commands = new ArrayList<CommandElement>();
				}

				// NOTE: Should we prevent the same element from being added twice?
				this._commands.add(command);
			}

			command.setOwningBundle(this);

			// fire add event
			BundleManager.getInstance().fireElementAddedEvent(command);
		}
	}

	/**
	 * addEnv
	 * 
	 * @param env
	 */
	public void addEnv(EnvironmentElement env)
	{
		if (env != null)
		{
			synchronized (envLock)
			{
				if (this._envs == null)
				{
					this._envs = new ArrayList<EnvironmentElement>();
				}

				// NOTE: Should we prevent the same element from being added twice?
				this._envs.add(env);
			}

			env.setOwningBundle(this);

			// fire add event
			BundleManager.getInstance().fireElementAddedEvent(env);
		}
	}

	/**
	 * addMenu
	 * 
	 * @param snippet
	 */
	public void addMenu(MenuElement menu)
	{
		if (menu != null)
		{
			synchronized (menuLock)
			{
				if (this._menus == null)
				{
					this._menus = new ArrayList<MenuElement>();
				}

				// NOTE: Should we prevent the same element from being added twice?
				this._menus.add(menu);
			}

			menu.setOwningBundle(this);

			// fire add event
			BundleManager.getInstance().fireElementAddedEvent(menu);
		}
	}

	/**
	 * associateFileType
	 * 
	 * @param fileType
	 */
	public void associateFileType(String fileType)
	{
		if (fileTypes == null)
		{
			fileTypes = new ArrayList<String>();
		}
		fileTypes.add(fileType);
	}

	/**
	 * associateScope
	 * 
	 * @param filePattern
	 * @param scope
	 */
	public void associateScope(String filePattern, String scope)
	{
		if (!StringUtil.isEmpty(filePattern) && !StringUtil.isEmpty(scope))
		{
			// Store the filetype -> scope mapping for later lookup when we need to set up the scope in the editor
			if (this._fileTypeRegistry == null)
			{
				this._fileTypeRegistry = new HashMap<String, String>();
			}

			this._fileTypeRegistry.put(filePattern, scope);
		}
	}

	/**
	 * clear - note that this only clears the bundle properties and not the bundle elements
	 */
	public void clearMetadata()
	{
		this._author = null;
		this._copyright = null;
		this._description = null;
		this._license = null;
		this._licenseUrl = null;
		if (_projectTemplates != null)
		{
			_projectTemplates.clear();
			_projectTemplates = null;
		}
	}

	/**
	 * getAuthor
	 * 
	 * @return
	 */
	public String getAuthor()
	{
		return this._author;
	}

	/**
	 * getBundleDirectory
	 * 
	 * @return
	 */
	public File getBundleDirectory()
	{
		return this._bundleDirectory;
	}

	/**
	 * getBundlePrecedence
	 * 
	 * @return
	 */
	public BundlePrecedence getBundlePrecedence()
	{
		return this._bundlePrecedence;
	}

	/**
	 * getCommandByName
	 * 
	 * @return
	 */
	public CommandElement getCommandByName(String name)
	{
		CommandElement result = null;

		synchronized (commandLock)
		{
			if (!StringUtil.isEmpty(name) && this._commands != null && this._commands.size() > 0)
			{
				for (CommandElement command : this._commands)
				{
					if (name.equals(command.getDisplayName()))
					{
						result = command;
						break;
					}
				}
			}
		}

		return result;
	}

	/**
	 * getCommands
	 * 
	 * @return
	 */
	public CommandElement[] getCommands()
	{
		CommandElement[] result = BundleManager.NO_COMMANDS;

		synchronized (commandLock)
		{
			if (this._commands != null && this._commands.size() > 0)
			{
				result = this._commands.toArray(new CommandElement[this._commands.size()]);
			}
		}

		return result;
	}

	/**
	 * getEnvs
	 * 
	 * @return
	 */
	public EnvironmentElement[] getEnvs()
	{
		EnvironmentElement[] result = new EnvironmentElement[0];

		synchronized (envLock)
		{
			if (this._envs != null && this._envs.size() > 0)
			{
				result = this._envs.toArray(new EnvironmentElement[this._envs.size()]);
			}
		}

		return result;
	}

	/**
	 * getCopyright
	 * 
	 * @return
	 */
	public String getCopyright()
	{
		return this._copyright;
	}

	/**
	 * getDefaultName
	 * 
	 * @return
	 */
	protected String getDefaultName()
	{
		return BundleUtils.getDefaultBundleName(this.getPath());
	}

	/**
	 * getDescription
	 * 
	 * @return
	 */
	public String getDescription()
	{
		return this._description;
	}

	/**
	 * getDisplayName
	 * 
	 * @return
	 */
	public String getDisplayName()
	{
		String result = super.getDisplayName();

		if (StringUtil.isEmpty(result))
		{
			result = this.getDefaultName();
		}

		return result;
	}

	/**
	 * getElementName
	 */
	protected String getElementName()
	{
		return "bundle"; //$NON-NLS-1$
	}

	/**
	 * getFileTypeRegistry
	 * 
	 * @return
	 */
	public Map<String, String> getFileTypeRegistry()
	{
		return this._fileTypeRegistry;
	}

	/**
	 * getFoldingStartMarkers
	 * 
	 * @return
	 */
	public Map<ScopeSelector, RubyRegexp> getFoldingStartMarkers()
	{
		if (this._foldingStartMarkers == null)
		{
			return Collections.emptyMap();
		}

		return Collections.unmodifiableMap(this._foldingStartMarkers);
	}

	/**
	 * getFoldingStopMarkers
	 * 
	 * @return
	 */
	public Map<ScopeSelector, RubyRegexp> getFoldingStopMarkers()
	{
		if (this._foldingStopMarkers == null)
		{
			return Collections.emptyMap();
		}

		return Collections.unmodifiableMap(this._foldingStopMarkers);
	}

	/**
	 * getFoldingStartMarkers
	 * 
	 * @return
	 */
	public Map<ScopeSelector, RubyRegexp> getIncreaseIndentMarkers()
	{
		if (this._increaseIndentMarkers == null)
		{
			return Collections.emptyMap();
		}

		return Collections.unmodifiableMap(this._increaseIndentMarkers);
	}

	/**
	 * getDecreaseIndentMarkers
	 * 
	 * @return
	 */
	public Map<ScopeSelector, RubyRegexp> getDecreaseIndentMarkers()
	{
		if (this._decreaseIndentMarkers == null)
		{
			return Collections.emptyMap();
		}

		return Collections.unmodifiableMap(this._decreaseIndentMarkers);
	}

	/**
	 * getLicense
	 * 
	 * @return
	 */
	public String getLicense()
	{
		return this._license;
	}

	/**
	 * getLicenseUrl
	 * 
	 * @return
	 */
	public String getLicenseUrl()
	{
		return this._licenseUrl;
	}

	/**
	 * getLoadPaths
	 * 
	 * @return
	 */
	public List<String> getLoadPaths()
	{
		List<String> result = new LinkedList<String>();

		result.add(BundleUtils.getBundleLibDirectory(this.getBundleDirectory()));

		return result;
	}

	/**
	 * getMenus
	 * 
	 * @return
	 */
	public MenuElement[] getMenus()
	{
		MenuElement[] result = BundleManager.NO_MENUS;

		synchronized (menuLock)
		{
			if (this._menus != null && this._menus.size() > 0)
			{
				result = this._menus.toArray(new MenuElement[this._menus.size()]);
			}
		}

		return result;
	}

	/**
	 * getGitRepo
	 * 
	 * @return
	 */
	public String getRepository()
	{
		return this._repository;
	}

	/**
	 * hasCommands
	 * 
	 * @return
	 */
	public boolean hasCommands()
	{
		boolean result = false;

		synchronized (commandLock)
		{
			if (this._commands != null)
			{
				result = this._commands.size() > 0;
			}
		}

		return result;
	}

	/**
	 * hasMenus
	 * 
	 * @return
	 */
	public boolean hasMenus()
	{
		boolean result = false;

		synchronized (menuLock)
		{
			if (this._menus != null)
			{
				result = this._menus.size() > 0;
			}
		}

		return result;
	}

	/**
	 * hasMetadata
	 * 
	 * @return
	 */
	public boolean hasMetadata()
	{
		return (this._author != null || this._copyright != null || this._description != null || this._license != null || this._licenseUrl != null);
	}

	/**
	 * isEmpty
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return this.hasMetadata() == false && this.hasCommands() == false && this.hasMenus() == false;
	}

	/**
	 * isReference
	 * 
	 * @return
	 */
	public boolean isReference()
	{
		// NOTE: we need to check the actual display name field and not the
		// calculated display name we generate in this class
		String displayName = super.getDisplayName();

		return !StringUtil.isEmpty(displayName) && StringUtil.areNotEqual(displayName, this.getDefaultName());
	}

	/**
	 * isVisible
	 * 
	 * @return
	 */
	public boolean isVisible()
	{
		return this._visible;
	}

	/**
	 * printBody
	 * 
	 * @return
	 */
	public void printBody(SourcePrinter printer)
	{
		printer.printWithIndent("bundle_precedence: ").println(this._bundlePrecedence.toString()); //$NON-NLS-1$
		printer.printWithIndent("path: ").println(this.getPath()); //$NON-NLS-1$
		printer.printWithIndent("author: ").println(this._author); //$NON-NLS-1$
		printer.printWithIndent("copyright: ").println(this._copyright); //$NON-NLS-1$
		printer.printWithIndent("description: ").println(this._description); //$NON-NLS-1$
		printer.printWithIndent("repository: ").println(this._repository); //$NON-NLS-1$

		// output commands
		synchronized (commandLock)
		{
			if (this._commands != null)
			{
				for (CommandElement command : this._commands)
				{
					command.toSource(printer);
				}
			}
		}

		// output menus
		synchronized (menuLock)
		{
			if (this._menus != null)
			{
				for (MenuElement menu : this._menus)
				{
					menu.toSource(printer);
				}
			}
		}
	}

	/**
	 * removeCommand
	 * 
	 * @param command
	 */
	public void removeCommand(CommandElement command)
	{
		boolean removed = false;

		synchronized (commandLock)
		{
			if (this._commands != null && (removed = this._commands.remove(command)))
			{
				AbstractElement.unregisterElement(command);
			}
		}

		if (removed)
		{
			// fire delete event
			BundleManager.getInstance().fireElementDeletedEvent(command);
		}
	}

	/**
	 * removeElement
	 * 
	 * @param element
	 */
	public void removeElement(AbstractBundleElement element)
	{
		if (element instanceof CommandElement)
		{
			this.removeCommand((CommandElement) element);
		}
		else if (element instanceof MenuElement)
		{
			this.removeMenu((MenuElement) element);
		}
	}

	/**
	 * removeMenu
	 * 
	 * @param command
	 */
	public void removeMenu(MenuElement menu)
	{
		boolean removed = false;

		synchronized (menuLock)
		{
			if (this._menus != null && (removed = this._menus.remove(menu)))
			{
				AbstractElement.unregisterElement(menu);

				menu.removeChildren();
			}
		}

		if (removed)
		{
			// fire delete event
			BundleManager.getInstance().fireElementDeletedEvent(menu);
		}
	}

	/**
	 * setAuthor
	 * 
	 * @param author
	 */
	public void setAuthor(String author)
	{
		this._author = author;
	}

	/**
	 * setCopyright
	 * 
	 * @param copyright
	 */
	public void setCopyright(String copyright)
	{
		this._copyright = copyright;
	}

	/**
	 * setDescription
	 * 
	 * @param description
	 */
	public void setDescription(String description)
	{
		this._description = description;
	}

	/**
	 * setFoldingMarkers
	 * 
	 * @param scope
	 * @param startRegexp
	 * @param endRegexp
	 */
	public void setFoldingMarkers(String scope, RubyRegexp startRegexp, RubyRegexp endRegexp)
	{
		if (!StringUtil.isEmpty(scope) && startRegexp != null && startRegexp.isNil() == false && endRegexp != null
				&& endRegexp.isNil() == false)
		{
			// store starting regular expression
			if (this._foldingStartMarkers == null)
			{
				this._foldingStartMarkers = new HashMap<ScopeSelector, RubyRegexp>();
			}

			this._foldingStartMarkers.put(new ScopeSelector(scope), startRegexp);

			// store ending regular expression
			if (this._foldingStopMarkers == null)
			{
				this._foldingStopMarkers = new HashMap<ScopeSelector, RubyRegexp>();
			}

			this._foldingStopMarkers.put(new ScopeSelector(scope), endRegexp);
		}
	}

	/**
	 * setIndentMarkers
	 * 
	 * @param scope
	 * @param startRegexp
	 * @param endRegexp
	 */
	public void setIndentMarkers(String scope, RubyRegexp startRegexp, RubyRegexp endRegexp)
	{
		if (!StringUtil.isEmpty(scope) && startRegexp != null && startRegexp.isNil() == false && endRegexp != null
				&& endRegexp.isNil() == false)
		{
			// store increasing regular expression
			if (this._increaseIndentMarkers == null)
			{
				this._increaseIndentMarkers = new HashMap<ScopeSelector, RubyRegexp>();
			}

			this._increaseIndentMarkers.put(new ScopeSelector(scope), startRegexp);

			// store decreasing regular expression
			if (this._decreaseIndentMarkers == null)
			{
				this._decreaseIndentMarkers = new HashMap<ScopeSelector, RubyRegexp>();
			}

			this._decreaseIndentMarkers.put(new ScopeSelector(scope), endRegexp);
		}
	}

	/**
	 * setLicense
	 * 
	 * @param license
	 */
	public void setLicense(String license)
	{
		this._license = license;
	}

	/**
	 * setLicenseUrl
	 * 
	 * @param licenseUrl
	 */
	public void setLicenseUrl(String licenseUrl)
	{
		this._licenseUrl = licenseUrl;
	}

	/**
	 * setGitRepo
	 * 
	 * @param gitRepo
	 */
	public void setRepository(String gitRepo)
	{
		this._repository = gitRepo;
	}

	/**
	 * setVisible
	 * 
	 * @param flag
	 */
	public void setVisible(boolean flag)
	{
		this._visible = flag;
	}

	/**
	 * getFileTypes
	 * 
	 * @return
	 */
	List<String> getFileTypes()
	{
		return fileTypes;
	}

	public void addProjectTemplate(String type, String name, String location, String description)
	{
		if (!StringUtil.isEmpty(type) && !StringUtil.isEmpty(name) && !StringUtil.isEmpty(location))
		{
			if (_projectTemplates == null)
			{
				_projectTemplates = new ArrayList<ProjectTemplate>();
			}
			_projectTemplates.add(new ProjectTemplate(type, name, location, description, _bundleDirectory));
		}
	}

	public ProjectTemplate[] getProjectTemplates()
	{
		if (_projectTemplates == null)
		{
			return BundleManager.NO_PROJECT_TEMPLATES;
		}
		return _projectTemplates.toArray(new ProjectTemplate[_projectTemplates.size()]);
	}

	public ProjectTemplate[] getProjectTemplatesByType(Type type)
	{
		if (_projectTemplates == null)
		{
			return BundleManager.NO_PROJECT_TEMPLATES;
		}
		List<ProjectTemplate> list = new ArrayList<ProjectTemplate>();
		for (ProjectTemplate template : _projectTemplates)
		{
			// type "all" is always included
			if (template.getType() == type || template.getType() == Type.ALL)
			{
				list.add(template);
			}
		}
		return list.toArray(new ProjectTemplate[list.size()]);
	}
}
