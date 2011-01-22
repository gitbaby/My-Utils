package com.conceptapp.videos;

import com.conceptapp.util.MyUtils;
import com.conceptapp.util.PMF;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UploadingServlet extends HttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		// Get member
		Member member = Auth.setMember(request, pm);
		if (member == null) {
			response.sendRedirect(Auth.getLoginUrl(request));
			return;
		}
		// Get uploaded files
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
		BlobKey blobKey = blobs.get("video");
		if (blobKey == null) {
			response.sendRedirect("/member/uploaded/error");
		} else {
			String title = MyUtils.escapeHtml(request.getParameter("title"));
			String description = MyUtils.escapeHtml(request.getParameter("description"));
			String keywords = MyUtils.escapeHtml(request.getParameter("keywords"));
			Video video = new Video(title, description, keywords);
			video.setMemberId(member.getId());
			video.setVideoBlobId(blobKey.getKeyString());
			blobKey = blobs.get("attachment");
			if (blobKey != null) {
				video.setAttachmentBlobId(blobKey.getKeyString());
			}
			video.updatePersistent(pm);
			response.sendRedirect("/member/uploaded/success");
		}
	}

}