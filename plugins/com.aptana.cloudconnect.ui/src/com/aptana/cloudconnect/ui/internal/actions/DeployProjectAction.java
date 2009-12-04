package com.aptana.cloudconnect.ui.internal.actions;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class DeployProjectAction implements IObjectActionDelegate {
	private static final String CLOUDCONNECT_URL =
		System.getProperty("CLOUDCONNECT_URL", "http://ec2-67-202-41-115.compute-1.amazonaws.com:81/user_session/new");
	private URL cloudconnectURL;
	
	private String to;
	private String projectName;

	public DeployProjectAction() {
		try {
			cloudconnectURL = new URL(CLOUDCONNECT_URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	public void run(IAction action) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (cloudconnectURL == null) {
			workbench.getDisplay().beep();
			return;
		}
		IWorkbenchBrowserSupport support = workbench.getBrowserSupport();
		try {
			if (support.isInternalWebBrowserAvailable()) {
				support.createBrowser(
						IWorkbenchBrowserSupport.AS_EDITOR,
						"Cloudconnect",
						"Cloudconnect" + (projectName == null ? "" : " - Deploying " + projectName + " to " + to),
						"Cloudconnect Management Console").openURL(cloudconnectURL);
			} else {
				support.getExternalBrowser().openURL(cloudconnectURL);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (to == null) {
			to = action.getText();
		}
		action.setEnabled(false);
		action.setText("Deploy...");
		projectName = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				Object firstElement = structuredSelection.getFirstElement();
				if (firstElement instanceof IResource) {
					IResource resource = (IResource) firstElement;
					action.setEnabled(true);
					projectName = resource.getName();
					action.setText("To " + to);
				}
			}
		}
	}

}
