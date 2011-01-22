package com.conceptapp.videos;

import com.conceptapp.util.PMF;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		// Get member
		Member member = Auth.setMember(request, pm);
		if (member == null) {
			response.sendRedirect(Auth.getLoginUrl(request));
			return;
		}
		// Show form
		request.setAttribute("title", "Upload Video");
		request.setAttribute("h1", "Upload Video");
		request.setAttribute("uploadUrl", blobstoreService.createUploadUrl("/member/uploading"));
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/videos/upload.jsp");
		dispatcher.forward(request, response);
	}

}