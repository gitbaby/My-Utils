package com.conceptapp.videos;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.conceptapp.util.DBRecord;
import com.conceptapp.util.Hash;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Video extends DBRecord {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String id;

	@Persistent
	private String title;

	@Persistent
	private String description;

	@Persistent
	private String userKeywords;

	@Persistent
	private String videoBlobId;

	@Persistent
	private String attachmentBlobId;

	@Persistent
	private String memberId;

	@Persistent
	private Date addedDate;

	@Persistent
	private Date lastViewDate;

	@Persistent
	private Long views;

	@Persistent
	private Double popularity;


	@NotPersistent
	private Integer relevance;

	@NotPersistent
	private String titleUrl;


	public Video(String title, String description, String userKeywords) {
		setId(new Hash().getHashUrlSafe(UUID.randomUUID().toString()));
		this.title = title;
		this.description = description;
		this.userKeywords = userKeywords;
		this.addedDate = new Date();
	}

	public static Key createKey(String id) {
		return KeyFactory.createKey(Video.class.getSimpleName(), "video(" + id + ")");
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

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String getTitleUrl() {
		if (titleUrl == null) {
			titleUrl = (new KeywordSet(title)).join("-");
		}
		return titleUrl;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Set<String> getKeywords() {
		return new KeywordSet(title + " " + description + " " + userKeywords);
	}

	public void setUserKeywords(String userKeywords) {
		this.userKeywords = userKeywords;
	}

	public String getUserKeywords() {
		return userKeywords;
	}

	public void setVideoBlobId(String videoBlobId) {
		this.videoBlobId = videoBlobId;
	}

	public String getVideoBlobId() {
		return videoBlobId;
	}

	public void setAttachmentBlobId(String attachmentBlobId) {
		this.attachmentBlobId = attachmentBlobId;
	}

	public String getAttachmentBlobId() {
		return attachmentBlobId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setLastViewDate(Date lastViewDate) {
		this.lastViewDate = lastViewDate;
	}

	public Date getLastViewDate() {
		return lastViewDate;
	}

	public void setViews(Long views) {
		this.views = views;
		popularity = views / (36500 - Math.floor(addedDate.getTime() / 86400000));
	}

	public Long getViews() {
		return views;
	}

	public void addView() {
		setViews(views + 1);
		setLastViewDate(new Date());
	}

	public void setPopularity(Double popularity) {
		this.popularity = popularity;
	}

	public Double getPopularity() {
		return popularity;
	}

	public void setRelevance(Integer relevance) {
		this.relevance = getKeywords().size() * relevance;
	}

	public Integer getRelevance() {
		return relevance;
	}

	/**
	 * Creates tags for search.
	 */
	public void createTags(PersistenceManager pm) {
		Set<String> keywords = getKeywords();
		String id = getId();
		Class<Tag> tagClass = Tag.class;
		Transaction tx = pm.currentTransaction();
		for (String keyword : keywords) {
			try {
				tx.begin();
				Tag tag;
				try {
					tag = pm.getObjectById(tagClass, Tag.createKey(keyword));
				} catch (JDOObjectNotFoundException e) {
					tag = new Tag(keyword);
				}
				if (tag.getBanned()) {
					tx.rollback();
				} else {
					if (!tag.getVideoIds().contains(id)) {
						tag.addVideoId(id);
						pm.makePersistent(tag);
					}
					tx.commit();
				}
			} finally {
				if (tx.isActive()) tx.rollback();
			}
		}
	}

	/**
	 * Deletes tags for search.
	 */
	public void deleteTags(PersistenceManager pm) {
		Set<String> keywords = getKeywords();
		String id = getId();
		Class<Tag> tagClass = Tag.class;
		Transaction tx = pm.currentTransaction();
		for (String keyword : keywords) {
			try {
				tx.begin();
				Tag tag = pm.getObjectById(tagClass, Tag.createKey(keyword));
				if (tag.getBanned()) {
					tx.rollback();
				} else {
					if (tag.getVideoIds().contains(id)) {
						tag.removeVideoId(id);
						if (tag.size() > 0) {
							pm.makePersistent(tag);
						} else {
							pm.deletePersistent(tag);
						}
					}
					tx.commit();
				}
			} catch (JDOObjectNotFoundException e) {
			} finally {
				if (tx.isActive()) tx.rollback();
			}
		}
	}

	/**
	 * Creates new or updates existing record. 
	 */
	@Override
	public boolean updatePersistent(PersistenceManager pm) {
		boolean success = super.updatePersistent(pm);
		if (success) {
			createTags(pm);
		}
		return success;
	}

	/**
	 * Deletes record.
	 */
	@Override
	public boolean deletePersistent(PersistenceManager pm) {
		boolean success = super.deletePersistent(pm);
		if (success) {
			deleteTags(pm);
		}
		return success;
	}

}