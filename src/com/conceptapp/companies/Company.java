package com.conceptapp.companies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Company implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

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
	private Long errors;

	@Persistent
	private Long restaurantNum;

	@Persistent(mappedBy = "company")
	@Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="id asc"))
	private List<Restaurant> restaurants;

	public Company(Long id) {
		setKey(createKey(id));
		setId(id);
		setErrors(0l);
		restaurantNum = 0l;
		restaurants = new ArrayList<Restaurant>();
	}

	public static Key createKey(Long id) {
		return KeyFactory.createKey(Company.class.getSimpleName(), "company(" + id + ")");
	}

	private void setKey(Key v) {
		key = v;
	}

	public Key getKey() {
		return key;
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

	public void setErrors(Long v) {
		errors = v;
	}

	public Long getErrors() {
		return errors;
	}

	public void addRestaurant(Restaurant v) {
		restaurants.add(v);
		restaurantNum++;
	}

	public void removeRestaurant(Restaurant v) {
		if (restaurants.remove(v)) restaurantNum--;
	}
	
	public Long getRestaurantNum() {
		return restaurantNum;
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
	 * Finds record by ID. 
	 * @param pm PersistenceManager
	 * @param id Long
	 * @return Company
	 */
	public static Company getRecord(PersistenceManager pm, Long id) {
		Key key = Company.createKey(id);
		Company rec = null;
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			try {
				rec = pm.getObjectById(Company.class, key);
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
	 * Returns last record. 
	 * @param pm PersistenceManager
	 * @return Company
	 */
	@SuppressWarnings("unchecked")
	public static Company getLastRecord(PersistenceManager pm) {
		Company rec = null;
		Query q = pm.newQuery(Company.class);
		q.setOrdering("id desc");
		q.setRange(0, 1);
		try {
			List<Company> results = (List<Company>) q.execute();
	        if (results.iterator().hasNext()) rec = pm.detachCopy(results.get(0));
		} finally {
			q.closeAll();
		}
		return rec;
	}

}