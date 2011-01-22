package com.conceptapp.videos;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import com.conceptapp.util.DB;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Auth {

	private static final UserService userService = UserServiceFactory.getUserService();

	private Auth() {}

	public static String getLoginUrl(HttpServletRequest request) {
		return userService.createLoginURL(request.getRequestURI());
	}

	public static String getLogoutUrl(HttpServletRequest request) {
		return userService.createLogoutURL("/");
	}

	public static void setLoginUrls(HttpServletRequest request) {
		request.setAttribute("signin", getLoginUrl(request));
		request.setAttribute("signout", getLogoutUrl(request));
	}

	public static Member getMember(PersistenceManager pm) {
		User user = userService.getCurrentUser();
		if (user == null) return null;
		String email = user.getEmail();
		String nickname = user.getNickname();
		Member member = DB.getRecord(pm, Member.class, Member.createKey(Member.emailToId(email)));
		if (member == null) {
			member = new Member(email, nickname);
			if (!member.updatePersistent(pm)) return null;
		} else if (!member.getNickname().equals(nickname)) {
			member.setNickname(nickname);
			member.updatePersistent(pm);
		}
		return member;
	}

	public static Member setMember(HttpServletRequest request, PersistenceManager pm) {
		Member member = getMember(pm);
		if (member != null) request.setAttribute("nickname", member.getNick());
		setLoginUrls(request);
		return member;
	}

}