package com.conceptapp.videos;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.conceptapp.util.DBRecord;
import com.conceptapp.util.Hash;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Member extends DBRecord {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String id;

	@Persistent
	private String email;

	@Persistent
	private String nickname;

	@Persistent
	private Date addedTime;
	
	public Member(String email, String nickname) {
		setEmail(email);
		this.nickname = nickname;
		this.addedTime = new Date();
	}

	public static Key createKey(String id) {
		return KeyFactory.createKey(Member.class.getSimpleName(), "member(" + id + ")");
	}

	public static String emailToId(String email) {
		return new Hash().getHashUrlSafe(email);
	}

	private void setKey(Key key) {
		this.key = key;
	}

	@Override
	public Key getKey() {
		return key;
	}

	private void setId(String id) {
		setKey(createKey(id));
		this.id = id;
	}

	public String getId() {
		return id;
	}

	private void setEmail(String email) {
		setId(emailToId(email));
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public String getNick() {
		int endIndex = nickname.indexOf("@");
		if (endIndex > 0) {
			return nickname.substring(0, endIndex);
		}
		return nickname;
	}

	public void setAddedTime(Date addedTime) {
		this.addedTime = addedTime;
	}

	public Date getAddedTime() {
		return addedTime;
	}

}