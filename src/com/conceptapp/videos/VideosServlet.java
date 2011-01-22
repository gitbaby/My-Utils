package com.conceptapp.videos;

import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.conceptapp.util.DB;
import com.conceptapp.util.PMF;

@SuppressWarnings("serial")
public class VideosServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setAttribute("title", "Videos");
		request.setAttribute("h1", "Videos");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Auth.setMember(request, pm);
		request.setAttribute("records", DB.getAllRecords(pm, Video.class));
		pm.close();
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/videos/videos.jsp");
		dispatcher.forward(request, response);
	}

}