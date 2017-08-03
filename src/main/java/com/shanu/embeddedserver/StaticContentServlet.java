package com.shanu.embeddedserver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.util.resource.Resource;

public class StaticContentServlet extends DefaultServlet {
	private String _contextPath;
	private Resource _resourceBase;
	public StaticContentServlet(String contextPath){
		_contextPath = contextPath;
	}
	
	public void addResource(URL url){
		try {
			_resourceBase = Resource.newResource(url.toExternalForm());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Resource getResource(String pathInContext){
		String newPath = pathInContext.substring(_contextPath.length(), pathInContext.length());
		Resource r = null;
		File configFile = null;
		try {
			r = _resourceBase.addPath(newPath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(r.exists()){
			try {
				configFile = new File(r.getURL().getPath());
				r = Resource.newResource(configFile.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;		
	}
}
