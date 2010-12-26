package com.conceptapp.companies;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Restaurant implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Company company;

	@SuppressWarnings("unused")
	@Persistent
	private Key companyKey;

	@Persistent
	private Long id;

	@Persistent
	private String name;

	@Persistent
	private String addressLine1;

	@Persistent
	private String addressLine2;

	@Persistent
	private String city;

	@Persistent
	private String state;

	@Persistent
	private String postCode;

	@Persistent
	private String country;

	@Persistent
	private String lastIp;

	@Persistent
	private Date lastDate;

	@Persistent
	private String version;

	@Persistent
	private Long errors;

	@Persistent
	private String status;

	public Restaurant(Company company, Long id) {
		setKey(createKey(company, id));
		setCompany(company);
		setId(id);
	}

	public static Key createKey(Company company, Long id) {
		KeyFactory.Builder kb = new KeyFactory.Builder(Company.class.getSimpleName(), company.getKey().getName());
		kb.addChild(Restaurant.class.getSimpleName(), "restaurant(" + id + ")");
		return kb.getKey();
	}

	private void setKey(Key v) {
		key = v;
	}

	public Key getKey() {
		return key;
	}

	private void setCompany(Company v) {
		company = v;
		companyKey = v.getKey();
	}

	public Company getCompany() {
		return company;
	}

	public void setId(Long v) {
		id = v;
	}

	public Long getId() {
		return id;
	}

	public void setName(String v) {
		name = v;
	}

	public String getName() {
		return name;
	}

	public void setAddressLine1(String v) {
		addressLine1 = v;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine2(String v) {
		addressLine2 = v;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setCity(String v) {
		city = v;
	}

	public String getCity() {
		return city;
	}

	public void setState(String v) {
		state = v;
	}

	public String getState() {
		return state;
	}

	public void setPostCode(String v) {
		postCode = v;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setCountry(String v) {
		country = v;
	}

	public String getCountry() {
		return country;
	}

	public void setLastIp(String v) {
		lastIp = v;
	}

	public String getLastIp() {
		return lastIp;
	}

	public void setLastDate() {
		lastDate = new Date();
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setVersion(String v) {
		version = v;
	}

	public String getVersion() {
		return version;
	}

	public void setErrors(Long v) {
		errors = v;
	}

	public Long getErrors() {
		return errors;
	}

	public void setStatus(String v) {
		status = v;
	}

	public String getStatus() {
		return status;
	}
	
	/**
	 * Updates record. 
	 * @param pm PersistenceManager
	 * @return success
	 */
	public boolean updatePersistent(PersistenceManager pm) {
		Transaction tx = pm.currentTransaction();
		boolean success = false;
		try {
			tx.begin();
			pm.makePersistent(this);
			tx.commit();
			success = true;
		} finally {
			if (tx.isActive()) tx.rollback();
		}
		return success;
	}

	/**
	 * Finds record by key. 
	 * @param pm PersistenceManager
	 * @param key Key
	 * @return Restaurant
	 */
	public static Restaurant getRecord(PersistenceManager pm, Key key) {
		Restaurant rec = null;
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			try {
				rec = pm.getObjectById(Restaurant.class, key);
			} catch (JDOObjectNotFoundException e) {
				// Does not exist
			}
			tx.commit();
		} finally {
			if (tx.isActive()) tx.rollback();
		}
		return rec;
	}

	/**
	 * Finds record by ID. 
	 * @param pm PersistenceManager
	 * @param company Company
	 * @param id String
	 * @return Restaurant
	 */
	public static Restaurant getRecord(PersistenceManager pm, Company company, Long id) {
		return getRecord(pm, createKey(company, id));
	}

	/**
	 * Returns last record. 
	 * @param pm PersistenceManager
	 * @param company Company
	 * @return Restaurant
	 */
	@SuppressWarnings("unchecked")
	public static Restaurant getLastRecord(PersistenceManager pm, Company company) {
		Restaurant rec = null;
		Query q = pm.newQuery(Restaurant.class);
		q.setFilter("companyKey == keyParam");
		q.setOrdering("id desc");
		q.setRange(0, 1);
		q.declareParameters("com.google.appengine.api.datastore.Key keyParam");
		try {
			List<Restaurant> results = (List<Restaurant>) q.execute(company.getKey());
	        if (results.iterator().hasNext()) rec = pm.detachCopy(results.get(0));
		} finally {
			q.closeAll();
		}
		return rec;
	}

}