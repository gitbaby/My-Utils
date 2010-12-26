package com.conceptapp.contacts;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.util.AuthenticationException;
import com.conceptapp.util.PMF;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class HandleTokenServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		// Get user
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No user specified.");
			return;
        }
		// Retrieve the AuthSub token assigned by Google
		String token = AuthSubUtil.getTokenFromReply(request.getQueryString());
		if (token == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No token specified.");
			return;
		}
		// Exchange the token for a session token
		String sessionToken;
		try {
			sessionToken = AuthSubUtil.exchangeForSessionToken(token, null);
		} catch (IOException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			"Exception retrieving session token.");
			return;
		} catch (GeneralSecurityException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			"Security error while retrieving session token.");
			return;
		} catch (AuthenticationException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			"Server rejected one time use token.");
			return;
		}
		// Store the token
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Session session = new Session(user.getEmail());
		session.setToken(sessionToken);
		session.updatePersistent(pm);
		pm.close();
		// Output susscess
		PrintWriter out = response.getWriter();
		out.println("<html><body>Success <a href=\"" +
				userService.createLogoutURL("http://conceptapp.appspot.com/") +
				"\">Sign Out</a></body></html>");
	}

}
