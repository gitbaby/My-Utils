package com.conceptapp.companies;

import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.conceptapp.util.MyUtils;
import com.conceptapp.util.PMF;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class EditCompanyServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setAttribute("title", "Edit Company");
		request.setAttribute("h1", "Edit Company");
		// Get user
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
		request.setAttribute("user", user.getEmail());
		// Get company
		String id = request.getParameter("id");
		if (id != null) {
			try {
				Long companyId = new Long(id);
				PersistenceManager pm = PMF.get().getPersistenceManager();
				Company rec = Company.getRecord(pm, companyId);
				if (rec != null) {
					request.setAttribute("id", id);
					request.setAttribute("name", MyUtils.escapeHtml(rec.getName()));
					request.setAttribute("addressLine1", MyUtils.escapeHtml(rec.getAddressLine1()));
					request.setAttribute("addressLine2", MyUtils.escapeHtml(rec.getAddressLine2()));
					request.setAttribute("city", MyUtils.escapeHtml(rec.getCity()));
					request.setAttribute("country", MyUtils.escapeHtml(rec.getCountry()));
					request.setAttribute("state", MyUtils.escapeHtml(rec.getState()));
					request.setAttribute("postCode", MyUtils.escapeHtml(rec.getPostCode()));
				}
			} catch(NumberFormatException e) {
			}
		}
		RequestDispatcher dispatcher =
			request.getRequestDispatcher("/WEB-INF/editcompany.jsp");
		dispatcher.forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		// Get company
		Company rec = null;
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			try {
				rec = Company.getRecord(pm, new Long(id));
			} catch(NumberFormatException e) {
			}
		}
		if (rec == null) {
			Long lastId = 0l;
			Company lastRec = Company.getLastRecord(pm);
			if (lastRec != null) lastId = lastRec.getId();
			rec = new Company(lastId + 1);
		}
		// Update data
		rec.setName(request.getParameter("name"));
		rec.setAddressLine1(request.getParameter("addressline1"));
		rec.setAddressLine2(request.getParameter("addressline2"));
		rec.setCity(request.getParameter("city"));
		rec.setState(request.getParameter("state"));
		rec.setPostCode(request.getParameter("postcode"));
		rec.setCountry(request.getParameter("country"));
		// Save changes
		rec.updatePersistent(pm);
		// Return to the list
		response.sendRedirect("/app/main");
	}

}