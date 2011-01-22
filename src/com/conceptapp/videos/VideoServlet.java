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
import com.conceptapp.util.NotFoundException;

@SuppressWarnings("serial")
public class VideoServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String path = request.getPathInfo();
		if (path != null) {
			String[] args = path.split("/");
			if (args.length > 1) {
				String videoId = args[1];
				PersistenceManager pm = PMF.get().getPersistenceManager();
				Auth.setMember(request, pm);
				try {
					// Get video
					Video video = DB.getRecord(pm, Video.class, Video.createKey(videoId));
					if (video == null) {
						throw new NotFoundException();
					}
					request.setAttribute("video", video);
					// Get member
					String memberId = video.getMemberId();
					if (memberId != null) {
						Member member = DB.getRecord(pm, Member.class, Member.createKey(memberId));
						request.setAttribute("member", member);
					}
					String title = video.getTitle();
					KeywordSet keywords = (KeywordSet) video.getKeywords();
					request.setAttribute("title", title);
					request.setAttribute("description", title);
					request.setAttribute("keywords", keywords.join(","));
					request.setAttribute("h1", title);
				} catch (NotFoundException e) {
					request.setAttribute("title", "Not found");
					request.setAttribute("h1", "Not Found");
				} finally {
					pm.close();
				}
				response.setContentType("text/html; charset=UTF-8");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/videos/video.jsp");
				dispatcher.forward(request, response);
				return;
			}
		}
		// Invalid args
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

}