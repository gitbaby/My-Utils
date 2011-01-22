package com.conceptapp.videos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.conceptapp.util.NotFoundException;
import com.conceptapp.util.PMF;

@SuppressWarnings("serial")
public class SearchServlet extends HttpServlet {

	private static final int perPage = 1000;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		// Get arguments
		KeywordSet keywords = null;
		String basePaging = "";
		String baseSort = "";
		String title = "";
		String order = "";
		int page = 1;
		String path = request.getPathInfo();
		if (path != null) {
			String[] args = path.split("/");
			if (args.length > 1) {
				// Keywords
				title = args[1];
				basePaging = baseSort = "/videos/" + title;
				title = title.replaceAll("[-\"<>\\s]+", " ").trim();
				keywords = new KeywordSet(title);
				// Page
				if (args.length > 3) {
					order = args[2];
					basePaging += "/" + order;
					try {
						page = Integer.parseInt(args[3]);
					} catch(NumberFormatException e) {
					}
				} else if (args.length > 2) {
					try {
						page = Integer.parseInt(args[2]);
					} catch(NumberFormatException e) {
						order = args[2];
						basePaging += "/" + order;
					}
				}
				if (page < 1) {
					page = 1;
				}
			}
		}
		// Search videos
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Auth.setMember(request, pm);
		try {
			if (title.length() > 0) {
				Search search = new Search(pm);
				List<Video> videos = search.getVideos(keywords, order, (page - 1) * perPage, page * perPage);
				request.setAttribute("videos", videos);
				request.setAttribute("basePaging", basePaging);
				request.setAttribute("pages", (int) Math.ceil((double) search.getTotal() / perPage));
				request.setAttribute("page", page);
				request.setAttribute("baseSort", baseSort);
				request.setAttribute("order", order);
				title = capitalize(title);
				String p = "";
				if (order.equals("p")) p = " by popular music";
				else if (order.equals("d")) p = " by new music";
				else if (order.equals("t")) p = " by title";
				if (page > 1) p += " page " + page;
				request.setAttribute("title", title);
				request.setAttribute("description", title);
				request.setAttribute("keywords", title.replaceAll(" ", ",").toLowerCase());
				request.setAttribute("h1", title);
				request.setAttribute("h2", title);
			} else {
				// Invalid arguments
				request.setAttribute("error", "Please enter at least one keyword.");
			}
		} catch (NotFoundException e) {
			if (page > 1) {
				// Invalid page number
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			// Not found
			title = capitalize(title);
			request.setAttribute("title", title);
			request.setAttribute("h1", title);
			request.setAttribute("error", "Sorry, I can't find nothing like \"" + title + "\".");
		}
		finally {
			pm.close();
		}
		response.setContentType("text/html; charset=UTF-8");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/videos/search.jsp");
		dispatcher.forward(request, response);
	}

	public static String capitalizeWord(String s) {
		if (s.length() == 0) return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	public static String capitalize(String s) {
		String[] words = s.split("\\s");
		List<String> capitalized = new ArrayList<String>();
		for (String w : words) {
			capitalized.add(capitalizeWord(w));
		}
		Iterator<String> iter = capitalized.iterator();
		StringBuffer buffer = new StringBuffer(iter.next());
		while (iter.hasNext()) buffer.append(" ").append(iter.next());
		return buffer.toString();
	}

}