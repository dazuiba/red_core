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
package com.aptana.git.ui.internal.history;

/**
 * @author cwilliams
 */
class GitLane
{

	/**
	 * Global incrementing index. This is so that a new lane gets a new index.
	 */
	private static int fgIndex = 0;

	/**
	 * SHA representing the parent that this lane is tracking.
	 */
	private String fSha;

	/**
	 * Index of the current lane.
	 */
	private int fIndex;

	GitLane(String sha)
	{
		fIndex = fgIndex++;
		fSha = sha;
	}

	/**
	 * To determine if this lane is the one where the passed in commit's SHA is attached to.
	 * 
	 * @param sha
	 * @return
	 */
	boolean isCommit(String sha)
	{
		return fSha.equals(sha);
	}

	/**
	 * The SHA of the commit/parent this lane is tracking.
	 * 
	 * @return
	 */
	String sha()
	{
		return fSha;
	}

	/**
	 * Index of the lane. Used to identify the lane for coloring and tracking.
	 * 
	 * @return
	 */
	int index()
	{
		return fIndex;
	}

	/**
	 * Set the new commit/parent SHA to track.
	 * 
	 * @param sha
	 */
	void setSha(String sha)
	{
		fSha = sha;
	}

	/**
	 * Used to reset our global incrementer.
	 */
	static void resetColors()
	{
		fgIndex = 0;
	}

}
