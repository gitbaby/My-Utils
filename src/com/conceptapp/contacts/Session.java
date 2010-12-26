package com.conceptapp.contacts;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.conceptapp.util.DBRecord;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Session extends DBRecord {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String id;

	@Persistent
	private String token;

	public Session(String id) {
		setKey(createKey(id));
		setId(id);
	}

	public static Key createKey(String id) {
		return KeyFactory.createKey(Session.class.getSimpleName(), "session(" + id + ")");
	}

	private void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

}