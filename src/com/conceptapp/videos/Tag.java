package com.conceptapp.videos;

import java.util.HashSet;
import java.util.Set;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.conceptapp.util.DBRecord;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Tag extends DBRecord {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String tag;

	@Persistent
	private Set<String> videoIds;

	@Persistent
	private Boolean banned;

	public Tag(String tag) {
		setTag(tag);
		videoIds = new HashSet<String>();
		banned = false;
	}

	public static Key createKey(String tag) {
		return KeyFactory.createKey(Tag.class.getSimpleName(), "tag(" + tag + ")");
	}

	private void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	private void setTag(String tag) {
		setKey(createKey(tag));
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setVideoIds(Set<String> videoIds) {
		this.videoIds = videoIds;
	}

	public Set<String> getVideoIds() {
		return videoIds;
	}

	public void addVideoId(String videoId) {
		if (banned) {
			return;
		}
		if (videoIds.size() < 1000) {
			videoIds.add(videoId);
		} else {
			setBanned(true);
		}
	}

	public void removeVideoId(String videoId) {
		videoIds.remove(videoId);
	}

	public int size() {
		return videoIds.size();
	}

	public void setBanned(Boolean banned) {
		if (banned) {
			videoIds.clear();
		}
		this.banned = banned;
	}

	public Boolean getBanned() {
		return banned;
	}

}