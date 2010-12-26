package com.conceptapp.contacts;

import com.conceptapp.util.DB;
import com.conceptapp.util.PMF;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.Organization;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.data.extensions.PostalAddress;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ContactsServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		// Get user
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
        if (user == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No user specified.");
			return;
        }
		// Get all tokens
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Session> sessions = (List<Session>) DB.getAllRecords(pm, Session.class);
		pm.close();
		// Get all contacts
		ContactsService contactsService = new ContactsService("ConceptApp");
		List<FieldList> records = new ArrayList<FieldList>();
		for (Session session : sessions) {
			FieldList fl = new FieldList();
			fl.setUser(session.getId());
			String sessionToken = session.getToken();
			try {
				AuthSubUtil.getTokenInfo(sessionToken, null);
				contactsService.setAuthSubToken(sessionToken);
				try {
					fl.setFields(getUserFields(contactsService));
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			} catch (AuthenticationException e) {
			} catch (GeneralSecurityException e) {
			}
			records.add(fl);
		}
		// Output result
		request.setAttribute("user", user.getEmail());
		request.setAttribute("logout", userService.createLogoutURL(request.getRequestURI()));
		request.setAttribute("title", "Contacts");
		request.setAttribute("h1", "Contacts");
		request.setAttribute("records", records);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/contacts.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}

	public static class FieldList {
		private String user;
		private List<Field> fields;

		public void setUser(String user) {
			this.user = user;
		}

		public String getUser() {
			return user;
		}

		public void setFields(List<Field> fields) {
			this.fields = fields;
		}

		public List<Field> getFields() {
			return fields;
		}
	}

	public static class Field {
		private String name;
		private String field;
		private String value;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getField() {
			return field;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static List<Field> getUserFields(ContactsService myService)
	throws ServiceException, IOException {
		List<Field> fields = new ArrayList<Field>();
		// Request the feed
		URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
		ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
		for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			ContactEntry entry = resultFeed.getEntries().get(i);
			String contactName;
			if (entry.hasName()) {
				Name name = entry.getName();
				if (name.hasFullName()) {
					contactName = name.getFullName().getValue();
				} else {
					contactName = "";
				}
			} else {
				contactName = "";
			}
			for (Email email : entry.getEmailAddresses()) {
				Field field = new Field();
				field.setName(contactName);
				field.setField("E-mail");
				field.setValue(email.getAddress());
				fields.add(field);
			}
			for (Im im : entry.getImAddresses()) {
				if (im.hasAddress()) {
					Field field = new Field();
					field.setName(contactName);
					field.setField("IM");
					field.setValue(im.getAddress());
					fields.add(field);
				}
			}
			for (Organization org : entry.getOrganizations()) {
				if (org.hasOrgName()) {
					Field field = new Field();
					field.setName(contactName);
					field.setField("Organization");
					field.setValue(org.getOrgName().getValue());
					fields.add(field);
				}
			}
			for (PhoneNumber phone : entry.getPhoneNumbers()) {
				Field field = new Field();
				field.setName(contactName);
				field.setField("Phone");
				field.setValue(phone.getPhoneNumber());
				fields.add(field);
			}
			for (PostalAddress addr : entry.getPostalAddresses()) {
				Field field = new Field();
				field.setName(contactName);
				field.setField("Address");
				field.setValue(addr.getValue());
				fields.add(field);
			}
		}
		return fields;
	}

}