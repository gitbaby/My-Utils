package com.conceptapp.videos;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ServeServlet extends HttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		String path = request.getPathInfo();
		if (path != null) {
			String[] args = path.split("/");
			if (args.length > 1) {
				BlobKey blobKey = new BlobKey(args[1]);
				blobstoreService.serve(blobKey, response);
			}
		}
	}

}