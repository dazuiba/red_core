package com.aptana.ide.syncing.ui.navigator.actions;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.BaseSelectionListenerAction;

import com.aptana.ide.syncing.core.SiteConnectionUtils;

public class NavigatorBaseSyncAction extends BaseSelectionListenerAction
{

	private IWorkbenchPart fActivePart;

	public NavigatorBaseSyncAction(String text, IWorkbenchPart activePart)
	{
		super(text);
		fActivePart = activePart;
	}

	@Override
	protected boolean updateSelection(IStructuredSelection selection)
	{
		// checks if any of the selection belongs to a sync connection and enables the action accordingly
		Object[] elements = ((IStructuredSelection) selection).toArray();
		for (Object element : elements)
		{
			if (element instanceof IAdaptable
					&& SiteConnectionUtils.findSitesForSource((IAdaptable) element).length > 0)
			{
				return true;
			}
		}
		return false;
	}

	protected IWorkbenchPart getActivePart()
	{
		return fActivePart;
	}
}