package com.conceptapp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Histogram<KeyType> {
	protected Map<KeyType, Integer> map;
	protected List<Map.Entry<KeyType, Integer>> list;

	public Histogram() {
		map = new HashMap<KeyType, Integer>();
	}

	public void add(KeyType key) {
		if (list != null) list = null;
		if (map.containsKey(key)) {
			map.put(key, map.get(key) + 1);
		} else {
			map.put(key, 1);
		}
	}

	public void add(Collection<KeyType> keys) {
		for (KeyType key : keys) add(key);
	}

	public void filter(int count) {
		if (count < 1) return;
		if (list != null) list = null;
		for (Iterator<KeyType> it = map.keySet().iterator(); it.hasNext(); )
			if (map.get(it.next()) <= count)
				it.remove();
	}

	public void limit(int size) {
		sort();
		for (int i = size() - 1; i >= size; i--) map.remove(getKey(i));
	}

	public int size() {
		return map.size();
	}

	public Set<KeyType> getKeys() {
		return map.keySet();
	}

	public KeyType getKey(int number) {
		sort();
		return list.get(number).getKey();
	}

	public int getCount(int number) {
		sort();
		return list.get(number).getValue();
	}

	public int getCount(KeyType key) {
		return map.get(key);
	}

	protected void sort() {
		if (list != null) return;
		list = new ArrayList<Map.Entry<KeyType, Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<KeyType, Integer>>() {
			public int compare(Map.Entry<KeyType, Integer> a, Map.Entry<KeyType, Integer> b) {
				return b.getValue().compareTo(a.getValue());
			}
		});
	}
}
