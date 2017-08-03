package com.shanu.embeddedserver;

import java.net.URL;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Hello world!
 *
 */
public class App 
{
	private static MyServer _server;
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        _server = new MyServer();
        _server.getContext().setContextPath("/");
        _server.getContext().addServlet(new ServletHolder(new FirstServlet()), "/abc");
        URL url = _server.getClass().getResource("/resources");
        String contextPath = "/resources";
        registerStaticContent(url,contextPath);
        _server.startServer();
        
    }
    
    public static void registerStaticContent(URL resourceUrl, String contextPath){
    	String pathSpec = contextPath;
    	if(!pathSpec.endsWith("/")){
    		pathSpec += "/";
    	}
    	pathSpec += "*";
    	StaticContentServlet contentServlet = new StaticContentServlet(contextPath);
    	_server.getContext().addServlet(new ServletHolder(contentServlet), pathSpec);
    	contentServlet.addResource(resourceUrl);
    }
}
