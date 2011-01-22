package com.conceptapp;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class WelcomeServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
//		if (Auth.isUser())
			response.sendRedirect("/app/main");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		doGet(request, response);
	}

}
