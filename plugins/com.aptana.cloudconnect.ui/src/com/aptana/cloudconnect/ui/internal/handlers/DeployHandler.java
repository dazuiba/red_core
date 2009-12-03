package com.aptana.cloudconnect.ui.internal.handlers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class DeployHandler extends AbstractHandler {
	
	private static final String CLOUDCONNECT_URL = 
		System.getProperty("CLOUDCONNECT_URL", "http://ec2-67-202-41-115.compute-1.amazonaws.com:81/user_session/new");
	private URL cloudconnectURL;
	
	public DeployHandler() {
		try {
			cloudconnectURL = new URL(CLOUDCONNECT_URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (cloudconnectURL == null) {
			workbench.getDisplay().beep();
			return null;
		}
		String projectName = null;
		Object context = event.getApplicationContext();
		if (context instanceof IEvaluationContext) {
			IEvaluationContext evaluationContext = (IEvaluationContext) context;
			Object defaultVariable = evaluationContext.getDefaultVariable();
			if (defaultVariable instanceof Collection && ((Collection)defaultVariable).iterator().hasNext()) {
				Object firstElement = ((Collection)defaultVariable).iterator().next();
				if (firstElement instanceof IResource) {
					IResource resource = (IResource) firstElement;
					projectName = resource.getName();
				}
			}
		}
		IWorkbenchBrowserSupport support = workbench.getBrowserSupport();
		try {
			if (support.isInternalWebBrowserAvailable()) {
				support.createBrowser(
						IWorkbenchBrowserSupport.AS_EDITOR,
						"Cloudconnect",
						"Cloudconnect" + (projectName == null ? "" : " - Deploying " + projectName ),
						"Cloudconnect Management Console").openURL(cloudconnectURL);
			} else {
				support.getExternalBrowser().openURL(cloudconnectURL);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}
}
