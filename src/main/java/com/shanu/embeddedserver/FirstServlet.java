package com.shanu.embeddedserver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FirstServlet extends javax.servlet.http.HttpServlet{
	static int num = 0;
	
	public FirstServlet(){
		System.out.println("First Servlet is called by " + ++num + " times");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
			response.getWriter().println("<h1>Hello from HelloServlet</h1>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
			response.getWriter().println("<h1>Hello from HelloServlet</h1>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
