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
package com.aptana.explorer.internal.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.WorkbenchJob;

import com.aptana.explorer.ExplorerPlugin;
import com.aptana.theme.Theme;
import com.aptana.theme.ThemePlugin;

/**
 * Adds focus filtering and a free form text filter to the Project view.
 * 
 * @author cwilliams
 */
public class FilteringProjectView extends GitProjectView
{
	/**
	 * Memento names for saving state of view and restoring it across launches.
	 */
	private static final String TAG_SELECTION = "selection"; //$NON-NLS-1$
	private static final String TAG_EXPANDED = "expanded"; //$NON-NLS-1$
	private static final String TAG_ELEMENT = "element"; //$NON-NLS-1$
	private static final String TAG_PATH = "path"; //$NON-NLS-1$
	private static final String TAG_PROJECT = "project"; //$NON-NLS-1$
	private static final String TAG_FILTER = "filter"; //$NON-NLS-1$
	private static final String KEY_NAME = "name"; //$NON-NLS-1$

	/**
	 * Maximum time spent expanding the tree after the filter text has been updated (this is only used if we were able
	 * to at least expand the visible nodes)
	 */
	private static final long SOFT_MAX_EXPAND_TIME = 200;

	private IResource currentFilter = null;

	private PathFilter patternFilter;
	private WorkbenchJob refreshJob;

	/**
	 * Fields for the focus/eyeball filtering
	 */
	protected TreeItem hoveredItem;
	protected int lastDrawnX;
	protected Object[] fExpandedElements;
	private Image eyeball;

	private Composite customComposite;
	private IResourceChangeListener fResourceListener;

	// Since the IMemento model does not fit our 'live' project switching,
	// we maintain the states of all the projects in these data structures and
	// flush them into the IMemento when needed (in the saveState call).
	private Map<IProject, List<String>> projectExpansions;
	private Map<IProject, List<String>> projectSelections;
	private Map<IProject, String> projectFilters;

	/**
	 * Constructs a new FilteringProjectView.
	 */
	public FilteringProjectView()
	{
		projectExpansions = new HashMap<IProject, List<String>>();
		projectSelections = new HashMap<IProject, List<String>>();
		projectFilters = new HashMap<IProject, String>();
	}

	@Override
	public void createPartControl(Composite aParent)
	{
		customComposite = new Composite(aParent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		customComposite.setLayout(gridLayout);

		super.createPartControl(customComposite);
		patternFilter = new PathFilter();
		createRefreshJob();

		// Add eyeball hover
		addFocusHover();
		addResourceListener();
	}

	@Override
	public void init(IViewSite aSite, IMemento aMemento) throws PartInitException
	{
		super.init(aSite, aMemento);
		loadMementoCache();

		eyeball = ExplorerPlugin.getImage("icons/full/obj16/eye.png"); //$NON-NLS-1$
	}

	/**
	 * Load the memento's relevant data into this view's internal memento data structure.
	 */
	protected void loadMementoCache()
	{
		if (this.memento == null)
		{
			return;
		}
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IMemento[] projectMementoes = memento.getChildren(TAG_PROJECT);
		for (IMemento projMemento : projectMementoes)
		{
			String projectName = projMemento.getString(KEY_NAME);
			IProject project = workspaceRoot.getProject(projectName);
			// Load only projects that exist and open in the workspace.
			if (project != null && project.isAccessible())
			{
				List<String> expanded = new ArrayList<String>();
				IMemento childMem = projMemento.getChild(TAG_EXPANDED);
				if (childMem != null)
				{
					IMemento[] elementMem = childMem.getChildren(TAG_ELEMENT);
					for (int i = 0; i < elementMem.length; i++)
					{
						expanded.add(elementMem[i].getString(TAG_PATH));
					}
				}
				List<String> selected = new ArrayList<String>();
				childMem = projMemento.getChild(TAG_SELECTION);
				if (childMem != null)
				{
					IMemento[] elementMem = childMem.getChildren(TAG_ELEMENT);
					for (int i = 0; i < elementMem.length; i++)
					{
						selected.add(elementMem[i].getString(TAG_PATH));
					}
				}
				// Cache the loaded mementoes
				projectExpansions.put(project, expanded);
				projectSelections.put(project, selected);

				childMem = projMemento.getChild(TAG_FILTER);
				if (childMem != null)
				{
					projectFilters.put(project, childMem.getString(TAG_PATH));
				}
			}
		}
	}

	/**
	 * Update the in-memory memento for a given project. This update method should be called before saving the memento
	 * and before every project switch.
	 * 
	 * @param project
	 */
	protected void updateProjectMementoCache(IProject project)
	{
		TreeViewer viewer = getCommonViewer();
		if (viewer == null || project == null)
		{
			return;
		}
		List<String> expanded = new ArrayList<String>();
		List<String> selected = new ArrayList<String>();

		// Save the expansion state
		Object expandedElements[] = viewer.getVisibleExpandedElements();
		if (expandedElements.length > 0)
		{
			for (int i = 0; i < expandedElements.length; i++)
			{
				if (expandedElements[i] instanceof IResource)
				{
					expanded.add(((IResource) expandedElements[i]).getFullPath().toString());
				}
			}
		}
		// Save the selection state
		Object selectedElements[] = ((IStructuredSelection) viewer.getSelection()).toArray();
		if (selectedElements.length > 0)
		{
			for (int i = 0; i < selectedElements.length; i++)
			{
				if (selectedElements[i] instanceof IResource)
				{
					selected.add(((IResource) selectedElements[i]).getFullPath().toString());
				}
			}
		}
		projectExpansions.put(project, expanded);
		projectSelections.put(project, selected);

		IResource filter = getFilterResource();
		if (filter != null)
		{
			projectFilters.put(project, filter.getLocation().toPortableString());
		}
	}

	private void addFocusHover()
	{
		final int IMAGE_MARGIN = 2;
		final Tree tree = getCommonViewer().getTree();

		/*
		 * NOTE: MeasureItem and PaintItem are called repeatedly. Therefore it is critical for performance that these
		 * methods be as efficient as possible.
		 */
		// Do our hover bg coloring
		tree.addListener(SWT.EraseItem, createHoverBGColorer());
		// Paint Eyeball
		tree.addListener(SWT.Paint, createEyeballPainter(eyeball, IMAGE_MARGIN, tree));
		// Track hovered item and force it's coloring
		getCommonViewer().getControl().addMouseMoveListener(createHoverTracker());
		// Remove hover on exit of tree
		getCommonViewer().getControl().addMouseTrackListener(createTreeExitHoverRemover());
		// handle Eyeball Focus action
		getCommonViewer().getTree().addMouseListener(createEyeballFocusClickHandler(eyeball));
	}

	protected MouseListener createEyeballFocusClickHandler(final Image eyeball)
	{
		return new MouseListener()
		{
			public void mouseUp(MouseEvent e)
			{
			}

			public void mouseDown(MouseEvent e)
			{
				if (hoveredItem == null)
					return;
				// If user clicks on the eyeball, we need to turn on the focus filter!
				Tree tree = (Tree) e.widget;
				TreeItem t = tree.getItem(new Point(e.x, e.y));
				if (t == null)
					return;
				if (!t.equals(hoveredItem))
					return;
				if (e.x >= lastDrawnX && e.x <= lastDrawnX + eyeball.getBounds().width)
				{
					// Ok, now we need to turn on the filter!
					IResource text = getResourceToFilterBy();
					if (text != null)
					{
						fExpandedElements = getCommonViewer().getExpandedElements();
						hoveredItem = null;
						setFilter(text);
					}
				}
			}

			public void mouseDoubleClick(MouseEvent e)
			{
			}
		};
	}

	protected MouseTrackAdapter createTreeExitHoverRemover()
	{
		return new MouseTrackAdapter()
		{
			@Override
			public void mouseExit(MouseEvent e)
			{
				super.mouseExit(e);
				if (hoveredItem == null)
					return;
				removeHoveredItem();
			}
		};
	}

	protected MouseMoveListener createHoverTracker()
	{
		return new MouseMoveListener()
		{

			public void mouseMove(MouseEvent e)
			{
				// If the filter is already on, we shouldn't do this stuff
				if (filterOn())
					return;
				final TreeItem t = getCommonViewer().getTree().getItem(new Point(e.x, e.y));
				// hovered item didn't change
				if (hoveredItem != null && hoveredItem.equals(t))
					return;
				// remove old hover
				removeHoveredItem();

				if (t == null)
					return;
				IResource data = getResource(t);
				if (data != null && (data.getType() == IResource.FILE))
				{
					hoveredItem = t;
					hoveredItem.setBackground(getHoverBackgroundColor());
					Display.getDefault().asyncExec(new Runnable()
					{
						public void run()
						{
							if (hoveredItem == null || getCommonViewer() == null || getCommonViewer().getTree() == null
									|| hoveredItem.getBounds() == null)
								return;
							getCommonViewer().getTree().redraw(hoveredItem.getBounds().x, hoveredItem.getBounds().y,
									hoveredItem.getBounds().width, hoveredItem.getBounds().height, true);
						}
					});
				}
			}
		};
	}

	protected Listener createEyeballPainter(final Image eyeball, final int IMAGE_MARGIN, final Tree tree)
	{
		return new Listener()
		{
			public void handleEvent(Event event)
			{
				if (hoveredItem == null || hoveredItem.isDisposed())
					return;
				if (eyeball != null)
				{
					int endOfClientAreaX = tree.getClientArea().width + tree.getClientArea().x;
					int endOfItemX = hoveredItem.getBounds().width + hoveredItem.getBounds().x;
					lastDrawnX = Math.max(endOfClientAreaX, endOfItemX) - (IMAGE_MARGIN + eyeball.getBounds().width);
					int itemHeight = tree.getItemHeight();
					int imageHeight = eyeball.getBounds().height;
					int y = hoveredItem.getBounds().y + (itemHeight - imageHeight) / 2;
					event.gc.drawImage(eyeball, lastDrawnX, y);
				}
			}
		};
	}

	protected Listener createHoverBGColorer()
	{
		return new Listener()
		{
			public void handleEvent(Event event)
			{
				if ((event.detail & SWT.BACKGROUND) != 0)
				{
					TreeItem item = (TreeItem) event.item;
					if (hoveredItem == null || !hoveredItem.equals(item))
						return;
					Tree tree = (Tree) event.widget;
					int clientWidth = tree.getClientArea().width;

					GC gc = event.gc;
					Color oldBackground = gc.getBackground();
					gc.setBackground(getHoverBackgroundColor());

					gc.fillRectangle(0, event.y, clientWidth, event.height);
					gc.setBackground(oldBackground);

					event.detail &= ~SWT.BACKGROUND;
				}
			}
		};
	}

	@Override
	protected void removeFilter()
	{
		clearFilter();
		super.removeFilter();
	}

	@Override
	public void saveState(IMemento memento)
	{
		super.saveState(memento);
		TreeViewer viewer = getCommonViewer();
		if (viewer == null)
		{
			if (this.memento != null)
			{
				memento.putMemento(this.memento);
			}
			return;
		}

		// Make sure we are up-to-date
		updateProjectMementoCache(selectedProject);
		// Collect all the projects in the cache
		Set<IProject> projects = new TreeSet<IProject>(new Comparator<IProject>()
		{
			public int compare(IProject o1, IProject o2)
			{
				return o1 == o2 ? 0 : o1.getName().compareTo(o2.getName());
			}
		});
		projects.addAll(projectExpansions.keySet());
		projects.addAll(projectSelections.keySet());

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		// Create a memento for every accessible project that has expanded/selected paths.
		for (IProject project : projects)
		{
			if (project.isAccessible()
					&& !(projectExpansions.get(project).isEmpty() && projectSelections.get(project).isEmpty() && projectFilters
							.get(project) == null))
			{
				IMemento projectMemento = memento.createChild(TAG_PROJECT);
				projectMemento.putString(KEY_NAME, project.getName());
				// Create the expansions memento
				List<String> expanded = projectExpansions.get(project);
				if (!expanded.isEmpty())
				{
					IMemento expansionMem = projectMemento.createChild(TAG_EXPANDED);
					for (String expandedPath : expanded)
					{
						if (workspaceRoot.findMember(expandedPath) != null)
						{
							IMemento elementMem = expansionMem.createChild(TAG_ELEMENT);
							elementMem.putString(TAG_PATH, expandedPath);
						}
					}
				}
				// Create the selection memento
				List<String> selected = projectSelections.get(project);
				if (!selected.isEmpty())
				{
					IMemento selectionMem = projectMemento.createChild(TAG_SELECTION);
					for (String selectedPath : selected)
					{
						if (workspaceRoot.findMember(selectedPath) != null)
						{
							IMemento elementMem = selectionMem.createChild(TAG_ELEMENT);
							elementMem.putString(TAG_PATH, selectedPath);
						}
					}
				}

				String filter = projectFilters.get(project);
				if (filter != null)
				{
					IMemento filterMem = projectMemento.createChild(TAG_FILTER);
					filterMem.putString(TAG_PATH, filter);
				}
			}
		}
	}

	/**
	 * Restore the expansion and selection state in a job.
	 * 
	 * @param project
	 */
	protected void restoreStateJob(final IProject project)
	{
		Job job = new WorkbenchJob("Restoring State") {//$NON-NLS-1$
			public IStatus runInUIThread(IProgressMonitor monitor)
			{
				restoreState(project);
				return Status.OK_STATUS;
			}
		};
		job.setSystem(true);
		// We have to delay it a bit, otherwise, the tree collapse back due
		// to other jobs.
		job.schedule(getRefreshJobDelay() * 2);
	}

	/**
	 * Restores the expansion and selection state of given project.
	 * 
	 * @param project
	 *            the {@link IProject}
	 * @see #restoreStateJob(IProject)
	 * @see #saveState(IMemento)
	 * @since 2.0
	 */
	protected void restoreState(IProject project)
	{
		TreeViewer viewer = getCommonViewer();

		IContainer container = ResourcesPlugin.getWorkspace().getRoot();
		List<String> expansions = projectExpansions.get(project);
		List<String> selections = projectSelections.get(project);
		viewer.getControl().setRedraw(false);
		// FIXME Reconstruct filter into IResource
		String filter = projectFilters.get(project);
		if (filter == null || filter.length() == 0)
		{
			if (currentFilter != null)
			{
				clearFilter();
			}
		}
		else
		{
			IResource filterResource = project.getWorkspace().getRoot()
					.getFileForLocation(Path.fromPortableString(filter));
			setFilter(filterResource);
		}
		if (selections != null)
		{
			List<IResource> elements = new ArrayList<IResource>();
			for (String selectionPath : selections)
			{
				IResource element = container.findMember(selectionPath);
				if (element != null)
				{
					elements.add(element);
				}
			}
			viewer.setSelection(new StructuredSelection(elements), true);
		}
		if (expansions != null)
		{
			List<IResource> elements = new ArrayList<IResource>();
			for (String expansionPath : expansions)
			{
				IResource element = container.findMember(expansionPath);
				if (element != null)
				{
					elements.add(element);
				}
			}
			viewer.setExpandedElements(elements.toArray());
		}
		viewer.getControl().setRedraw(true);
	}

	@Override
	protected void projectChanged(IProject oldProject, IProject newProject)
	{
		// Update the memento cache when the project is changed.
		updateProjectMementoCache(oldProject);
		super.projectChanged(oldProject, newProject);
		// Restore the displayed project state.
		restoreStateJob(newProject);
	}

	@Override
	public void dispose()
	{
		removeResourceListener();
		super.dispose();
	}

	private void addResourceListener()
	{
		// Add a listener for add/remove/edits of files in this project!
		fResourceListener = new IResourceChangeListener()
		{

			public void resourceChanged(IResourceChangeEvent event)
			{
				if (selectedProject == null || !selectedProject.exists())
					return;
				IResourceDelta delta = event.getDelta();
				IResourceDelta[] children = delta.getAffectedChildren();
				for (IResourceDelta iResourceDelta : children)
				{
					IResource resource = iResourceDelta.getResource();
					if (resource == null)
						continue;
					if (resource.getProject().equals(selectedProject))
					{
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable()
						{

							public void run()
							{
								if (getCommonViewer() != null && getCommonViewer().getTree() != null
										&& getCommonViewer().getTree().isDisposed())
								{
									getCommonViewer().refresh();
								}
							}
						});
						return;
					}
				}
			}
		};
		ResourcesPlugin.getWorkspace().addResourceChangeListener(fResourceListener, IResourceChangeEvent.POST_CHANGE);
	}

	private void removeResourceListener()
	{
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(fResourceListener);
	}

	/**
	 * Clears the filter.
	 */
	protected void clearFilter()
	{
		currentFilter = null;
		filterChanged();
	}

	/**
	 * Set the filter.
	 * 
	 * @param resource
	 */
	protected void setFilter(IResource resource)
	{
		currentFilter = resource;
		filterChanged();
	}

	/**
	 * Create the refresh job for the receiver.
	 */
	private void createRefreshJob()
	{
		refreshJob = doCreateRefreshJob();
		refreshJob.setSystem(true);
	}

	/**
	 * Update the receiver after the filter has changed.
	 */
	protected void filterChanged()
	{
		// cancel currently running job first, to prevent unnecessary redraw
		if (refreshJob != null)
		{
			refreshJob.cancel();
			refreshJob.schedule(getRefreshJobDelay());
		}
	}

	/**
	 * Return the time delay that should be used when scheduling the filter refresh job. Subclasses may override.
	 * 
	 * @return a time delay in milliseconds before the job should run
	 * @since 3.5
	 */
	protected long getRefreshJobDelay()
	{
		return 100;
	}

	protected WorkbenchJob doCreateRefreshJob()
	{
		return new WorkbenchJob("Refresh Filter") {//$NON-NLS-1$
			public IStatus runInUIThread(IProgressMonitor monitor)
			{
				if (getCommonViewer().getControl().isDisposed())
				{
					return Status.CANCEL_STATUS;
				}

				Control redrawFalseControl = getCommonViewer().getControl();
				try
				{
					// don't want the user to see updates that will be made to
					// the tree
					// we are setting redraw(false) on the composite to avoid
					// dancing scrollbar
					redrawFalseControl.setRedraw(false);
					// collapse all
					getCommonViewer().collapseAll();
					// Now apply/remove teh filter. This will trigger a refresh!
					IResource filterResource = getFilterResource();
					try
					{
						if (filterResource == null)
						{
							patternFilter.setResourceToFilterOn(null);

							getCommonViewer().removeFilter(patternFilter);
						}
						else
						{
							getCommonViewer().removeFilter(patternFilter);
							patternFilter.setResourceToFilterOn(filterResource);
							showFilterLabel(
									eyeball,
									NLS.bind(Messages.FilteringProjectView_LBL_FilteringFor,
											new Object[] { patternFilter.getPattern() }));
							getCommonViewer().addFilter(patternFilter);
						}
					}
					catch (Exception e)
					{
						// ignore. This seems to just happen on windows and appears to be benign
					}
					
					// Now set up expansion of elements
					if (filterResource != null)
					{
						/*
						 * Expand elements one at a time. After each is expanded, check to see if the filter text has
						 * been modified. If it has, then cancel the refresh job so the user doesn't have to endure
						 * expansion of all the nodes.
						 */
						TreeItem[] items = getCommonViewer().getTree().getItems();
						int treeHeight = getCommonViewer().getTree().getBounds().height;
						int numVisibleItems = treeHeight / getCommonViewer().getTree().getItemHeight();
						long stopTime = SOFT_MAX_EXPAND_TIME + System.currentTimeMillis();
						boolean cancel = false;
						if (items.length > 0
								&& recursiveExpand(items, monitor, stopTime, new int[] { numVisibleItems }))
						{
							cancel = true;
						}
						if (cancel)
						{
							return Status.CANCEL_STATUS;
						}
					}
					else
					{
						// to reset our expansion state back to what it was before user used the eyeball/focus filter!
						if (fExpandedElements != null)
						{
							getCommonViewer().collapseAll();
							getCommonViewer().setExpandedElements(fExpandedElements);
							fExpandedElements = null;
						}
					}
				}
				finally
				{
					// done updating the tree - set redraw back to true
					TreeItem[] items = getCommonViewer().getTree().getItems();
					if (items.length > 0 && getCommonViewer().getTree().getSelectionCount() == 0)
					{
						getCommonViewer().getTree().setTopItem(items[0]);
					}
					redrawFalseControl.setRedraw(true);
				}
				return Status.OK_STATUS;
			}

			/**
			 * Returns true if the job should be canceled (because of timeout or actual cancellation).
			 * 
			 * @param items
			 * @param monitor
			 * @param cancelTime
			 * @param numItemsLeft
			 * @return true if canceled
			 */
			private boolean recursiveExpand(TreeItem[] items, IProgressMonitor monitor, long cancelTime,
					int[] numItemsLeft)
			{
				boolean canceled = false;
				for (int i = 0; !canceled && i < items.length; i++)
				{
					TreeItem item = items[i];
					boolean visible = numItemsLeft[0]-- >= 0;
					if (monitor.isCanceled() || (!visible && System.currentTimeMillis() > cancelTime))
					{
						canceled = true;
					}
					else
					{
						Object itemData = item.getData();
						if (itemData != null)
						{
							if (!item.getExpanded())
							{
								// do the expansion through the viewer so that
								// it can refresh children appropriately.
								getCommonViewer().setExpandedState(itemData, true);
							}
							TreeItem[] children = item.getItems();
							if (items.length > 0)
							{
								canceled = recursiveExpand(children, monitor, cancelTime, numItemsLeft);
							}
						}
					}
				}
				return canceled;
			}

		};
	}

	protected IResource getFilterResource()
	{
		return currentFilter;
	}

	private IResource getResourceToFilterBy()
	{
		return getResource(hoveredItem);
	}

	private void removeHoveredItem()
	{
		if (hoveredItem == null)
			return;
		if (hoveredItem.isDisposed())
		{
			hoveredItem = null;
			return;
		}
		final Rectangle bounds = hoveredItem.getBounds();
		hoveredItem.setBackground(null);
		hoveredItem = null;
		Display.getDefault().asyncExec(new Runnable()
		{

			public void run()
			{
				getCommonViewer().getTree().redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
			}
		});
	}

	protected Color getHoverBackgroundColor()
	{
		return ThemePlugin.getDefault().getColorManager().getColor(getActiveTheme().getLineHighlightAgainstBG());
	}

	protected Theme getActiveTheme()
	{
		return getThemeManager().getCurrentTheme();
	}

	private boolean filterOn()
	{
		return getFilterResource() != null;
	}

	protected IResource getResource(final TreeItem t)
	{
		Object data = t.getData();
		if (data instanceof IResource)
			return (IResource) t.getData();
		if (data instanceof IAdaptable)
		{
			IAdaptable adapt = (IAdaptable) data;
			return (IResource) adapt.getAdapter(IResource.class);
		}
		return null;
	}

}
