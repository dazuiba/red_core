package com.aptana.ui;

import org.eclipse.core.resources.IProject;

/**
 * Selects a contributed extension implementation based upon the
 * <code>priority</code> it was registered with when the plugin containing its
 * extension definition was loaded.
 */
public class PriorityContributionSelector implements
		IContributionSelector {

	/*
	 * @see org.eclipse.dltk.core.ContributionSelector#select(org.eclipse.dltk.core.ContributedExtension[],
	 *      org.eclipse.core.resources.IProject)
	 */
	public IContributedExtension select(
			IContributedExtension[] contributions, IProject project) {
		int maxPriority = Integer.MIN_VALUE;
		IContributedExtension selected = null;

		for (int i = 0; i < contributions.length; i++) {
			IContributedExtension contrib = contributions[i];

			/*
			 * if more then one contribution has the same priority, the first
			 * one found in the array wins
			 */
			if (contrib.getPriority() > maxPriority) {
				selected = contrib;
				maxPriority = contrib.getPriority();
			}
		}

		return selected;
	}
}