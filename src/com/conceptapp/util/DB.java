package com.conceptapp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

public class DB {

	private DB() {}

	/**
	 * Finds record by key.
	 */
	public static <T extends Object> T getRecord(PersistenceManager pm, Class<T> cl, Key key) {
		T rec = null;
		try {
			rec = pm.getObjectById(cl, key);
		} catch (JDOObjectNotFoundException e) {
			// Does not exist
		}
		return rec;
	}

    /**
     * Deletes record by key.
     */
    public static <T extends Object> boolean deleteRecord(PersistenceManager pm, Class<T> cl, Key key) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.deletePersistent(pm.getObjectById(cl, key));
            tx.commit();
        } catch (JDOObjectNotFoundException e) {
            // Does not exist
        } finally {
            if (tx.isActive()) tx.rollback();
        }
        return true;
    }

	/**
	 * Finds a record for each key. Order is preserved. Returns detached copies.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends DBObject> List<T> getRecords(PersistenceManager pm, Class<T> cl, List<Key> keys) {
		int keysSize = keys.size();
		if (keysSize == 0) {
			return new ArrayList<T>();
		}
		Vector<Key> vector = new Vector<Key>(keys);
		Object[] records = new Object[keysSize];
		Query q = pm.newQuery("select from " + cl.getName() + " where :keys.contains(key)");
		try {
			List<T> results = (List<T>) q.execute(keys);
			for (T r : results) {
				r = pm.detachCopy(r);
				Key key = r.getKey();
				int i = 0;
				while ((i = vector.indexOf(key, i)) >= 0) {
					records[i] = r;
					i++;
				}
			}
		} finally {
			q.closeAll();
		}
		return (List<T>) Arrays.asList(records);
	}

	/**
	 * Returns detached copy of all records. 
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> List<T> getAllRecords(PersistenceManager pm, Class<T> cl) {
		List<T> records = null;
		Query q = pm.newQuery(cl);
		try {
			records = (List<T>) pm.detachCopyAll((Collection<?>) q.execute());
		} finally {
			q.closeAll();
		}
		return records;
	}

	/**
	 * Returns detached copy of the first record with specified ordering. 
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T getFirstRecordOrdering(PersistenceManager pm, Class<T> cl, String ordering) {
		T rec = null;
		Query q = pm.newQuery(cl);
		q.setOrdering(ordering);
		q.setRange(0, 1);
		try {
			List<?> results = (List<?>) q.execute();
	        if (results.iterator().hasNext()) {
	        	rec = (T) pm.detachCopy(results.get(0));
	        }
		} finally {
			q.closeAll();
		}
		return rec;
	}

	/**
	 * Returns detached copy of the last record. 
	 */
	public static <T extends Object> T getLastRecord(PersistenceManager pm, Class<T> cl) {
		return getFirstRecordOrdering(pm, cl, "id desc");
	}

}