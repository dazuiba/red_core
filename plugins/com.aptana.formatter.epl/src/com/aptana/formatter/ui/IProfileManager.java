/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package com.aptana.formatter.ui;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.util.IPropertyChangeListener;

import com.aptana.ui.preferences.PreferenceKey;

/**
 * The model for the set of profiles which are available in the workbench.
 */
public interface IProfileManager
{

	/**
	 * A property name for a change in the profile's selection.
	 */
	public static final String PROFILE_SELECTED = "profile.selected"; //$NON-NLS-1$

	/**
	 * Returns true iff the profile is dirty.
	 */
	public boolean isDirty();

	/**
	 * Get an immutable list as view on all profiles, sorted alphabetically. Unless the set of profiles has been
	 * modified between the two calls, the sequence is guaranteed to correspond to the one returned by
	 * <code>getSortedNames</code>.
	 * 
	 * @return a list of elements of type <code>IProfile</code>
	 * @see #getSortedDisplayNames()
	 */
	public List<IProfile> getSortedProfiles();

	/**
	 * Get the names of all profiles stored in this profile manager, sorted alphabetically. Unless the set of profiles
	 * has been modified between the two calls, the sequence is guaranteed to correspond to the one returned by
	 * <code>getSortedProfiles</code>.
	 * 
	 * @return All names, sorted alphabetically
	 * @see #getSortedProfiles()
	 */
	public String[] getSortedDisplayNames();

	/**
	 * Check whether a user-defined profile in this profile manager already has this name.
	 * 
	 * @param name
	 *            The name to test for
	 * @return Returns <code>true</code> if a profile with the given name exists
	 */
	public boolean containsName(String name);

	public IProfile findProfile(String profileId);

	public void addPropertyChangeListener(IPropertyChangeListener listener);
	
	public void removePropertyChangeListener(IPropertyChangeListener listener);

	/**
	 * Get the currently selected profile.
	 * 
	 * @return The currently selected profile.
	 */
	public IProfile getSelected();

	/**
	 * Set the selected profile. The profile must already be contained in this profile manager.
	 * 
	 * @param profile
	 *            The profile to select
	 */
	public void setSelected(IProfile profile);

	/**
	 * Add a new custom profile to this profile manager.
	 * 
	 * @param profile
	 *            The profile to add
	 */
	public void addProfile(IProfile profile);

	/**
	 * Delete passed profile from this profile manager. The next profile in the list is selected.
	 * 
	 * @return true if the profile has been successfully removed, false otherwise.
	 */
	public boolean deleteProfile(IProfile profile);

	/**
	 * Rename profile to specified newName and return result profile
	 * 
	 * @param profile
	 *            profile to rename
	 * @param newName
	 *            new profile name
	 * @return profile with specified name
	 */
	public IProfile rename(IProfile profile, String newName);

	/**
	 * @param profileName
	 * @param settings
	 * @param version
	 * @return
	 */
	public IProfile create(ProfileKind kind, String profileName, Map<String, String> settings, int version);

	public void markDirty();

	/**
	 * 
	 */
	public void clearDirty();

	/**
	 * Returns the active profile key.
	 * 
	 * @return
	 */
	public PreferenceKey getActiveProfileKey();

	public IProfileVersioner getProfileVersioner();

	public IProfileStore getProfileStore();

	public List<IProfile> getBuiltInProfiles();

	public List<IProfile> getCustomProfiles();

	public String getDefaultProfileID();

	public String getDefaultProfileName();

	/**
	 * Returns the preferences key that will eventually hold the custom formatters settings.
	 * 
	 * @return The preferences key that will hold the custom formatter settings.
	 */
	public PreferenceKey getProfilesKey();
}