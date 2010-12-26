package com.conceptapp.util;

import com.google.appengine.api.datastore.Key;

public interface DBObject {
	Key getKey();
}