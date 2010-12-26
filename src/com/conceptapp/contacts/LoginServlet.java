package com.conceptapp.contacts;

import com.google.gdata.client.http.AuthSubUtil;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		String authSubLogin = AuthSubUtil.getRequestUrl(
				"http://conceptapp.appspot.com/app/contacts/authhandler",
				"https://www.google.com/m8/feeds/",
				false,
				true
		);
		response.sendRedirect(authSubLogin);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		doGet(request, response);
	}

}