package com.conceptapp.util;

import java.util.HashMap;
import java.util.Map;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

public class MemCache {

	private Cache cache = null;

	public MemCache() {
		Map<Integer, Integer> props = new HashMap<Integer, Integer>();
		props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
		try {
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			cache = cacheFactory.createCache(props);
		} catch (CacheException ex) {
			new Log(MemCache.class.getName()).stackTrace(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public void put(String key, byte[] value) {
		if (cache != null) {
			cache.put(key, value);
		}
	}

	public byte[] get(String key) {
		if (cache == null) {
			return null;
		}
		return (byte[]) cache.get(key);
	}

}