package com.shanu.embeddedserver;

import java.util.List;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;

public class MyServer {
	private Server _server;
	private ServerConnector _connector;
	private List<ServletHolder> _servletHolders;
	private List<ServletMapping> _servletMappings;
	private ServletContextHandler context;
	
	MyServer(){
		try{
			_server = new Server();
		}catch(Exception e){
			e.printStackTrace();
		}
		_connector = new ServerConnector(_server);
		_connector.setHost("127.0.0.1");
		_connector.setPort(9876);
		_server.setConnectors(new Connector[]{_connector});
		context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		_server.setHandler(context);
	}
	
	public ServletContextHandler getContext (){
		return context;
	}
	
	public Server getServer(){
		return _server;
	}
	
	public void startServer(){
		try {
			_server.start();
			_server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopServer(){
		try {
			_server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
