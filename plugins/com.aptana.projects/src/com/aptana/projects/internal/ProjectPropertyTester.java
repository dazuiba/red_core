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
package com.aptana.projects.internal;

import java.text.MessageFormat;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.aptana.projects.ProjectsPlugin;

/**
 * A property tester that will test the existence of the nature ID in the given {@link IProject} receiver.
 * 
 * @author Shalom Gibly
 */
public class ProjectPropertyTester extends PropertyTester
{

	public static final String APTANA_NATURE_PREFIX = "com.aptana."; //$NON-NLS-1$

	private static final String NATURE = "nature"; //$NON-NLS-1$
	private static final String APTANA_NATURE = "aptana"; //$NON-NLS-1$

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
	{
		if (NATURE.equals(property) && receiver instanceof IProject)
		{
			IProject project = (IProject) receiver;
			if (!project.isAccessible())
			{
				return false;
			}
			try
			{
				if (args != null && args.length > 0)
				{
					if (APTANA_NATURE.equals(args[0]))
					{
						// generic check against all Aptana project natures
						String[] natureIds = project.getDescription().getNatureIds();
						for (String id : natureIds)
						{
							if (id.startsWith(APTANA_NATURE_PREFIX))
							{
								return true;
							}
						}
						return false;
					}
					// checks against the specific nature
					return project.hasNature(args[0].toString());
				}
				else
				{
					// we have not args, so check if the project does not contain any nature
					return project.getDescription().getNatureIds().length == 0;
				}
			}
			catch (CoreException e)
			{
				ProjectsPlugin.logError(
						MessageFormat.format(Messages.ProjectPropertyTester_ERR_ProjectNature, project.getName()), e);
			}
		}
		return false;
	}
}
