package com.conceptapp.companies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.conceptapp.util.PMF;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class CompaniesServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setAttribute("title", "Companies");
		request.setAttribute("h1", "Companies");
		// Get user
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
		request.setAttribute("user", user.getEmail());
		// Get records
		List<Company> records = new ArrayList<Company>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Company.class);
		query.setOrdering("id asc");
		try {
			@SuppressWarnings("unchecked")
			List<Company> results = (List<Company>) query.execute();
			if (results.iterator().hasNext()) {
				for (Company v : results) {
					records.add(pm.detachCopy(v));
				}
			}
		} finally {
			query.closeAll();
		}
		pm.close();
		request.setAttribute("records", records);
		RequestDispatcher dispatcher =
			request.getRequestDispatcher("/WEB-INF/companies.jsp");
		dispatcher.forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}

}