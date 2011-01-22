package com.conceptapp.videos;

import com.conceptapp.util.PMF;
import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UploadedServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		// Get member
		Member member = Auth.setMember(request, pm);
		if (member == null) {
			response.sendRedirect(Auth.getLoginUrl(request));
			return;
		}
		String path = request.getPathInfo();
		if (path != null) {
			String[] args = path.split("/");
			if (args.length > 1) {
				String status = args[1];
				if (status.equals("success")) {
					request.setAttribute("message", "Success");
				} else if (status.equals("error")) {
					request.setAttribute("message", "Could not upload video");
				}
			}
		}
		// Show result
		request.setAttribute("title", "Upload Video");
		request.setAttribute("h1", "Upload Video");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/videos/uploaded.jsp");
		dispatcher.forward(request, response);
	}

}