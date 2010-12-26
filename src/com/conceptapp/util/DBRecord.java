package com.conceptapp.util;

import java.io.Serializable;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
public class DBRecord implements Serializable, DBObject {

	@Override
	public Key getKey() {
		return null;
	}

	/**
	 * Creates new or updates existing record. 
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
	 * Deletes record.
	 */
	public boolean deletePersistent(PersistenceManager pm) {
		Transaction tx = pm.currentTransaction();
		boolean success = false;
		try {
			tx.begin();
			pm.deletePersistent(this);
			tx.commit();
			success = true;
		} finally {
			if (tx.isActive()) tx.rollback();
		}
		return success;
	}

}