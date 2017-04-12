package com.liudong.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 容器中超过15分钟的没有被访问的内容将会被删除
 * 
 * @author Administrator
 */
public class MyHashMap<K, V> implements Map<K, V> {
	Logger log = LoggerFactory.getLogger(this.getClass());

	private HashMap<K, MyEntry<K, V>> map = new HashMap<K, MyEntry<K, V>>();
	PriorityQueue<MyEntry<K, V>> pq = new PriorityQueue<MyEntry<K, V>>(11,
	        new Comparator<MyEntry<K, V>>() {
		        @Override
		        public int compare(MyEntry<K, V> o1, MyEntry<K, V> o2) {
			        return o1.getDate().getTime() > o2.getDate().getTime() ? 1
			                : -1;
		        }
	        });
	Set<java.util.Map.Entry<K, V>> set = new HashSet<java.util.Map.Entry<K, V>>();

	public V get(Object key) {
		removeInvalidEntry();
		MyEntry<K, V> me = map.get(key);
		return null == me ? null : me.getValue();
	}

	public V put(K key, V value) {
		removeInvalidEntry();
		MyEntry<K, V> me = new MyEntry<K, V>(key, value);
		map.put(key, me);
		return null;
	}

	private void removeInvalidEntry() {
		if (pq.size() < 1)
			return;
		MyEntry<K, V> mye;
		while (System.currentTimeMillis()
		        - (mye = pq.peek()).getDate().getTime() > 15 * 60 * 1000) {
			map.remove(mye.getKey());
			pq.poll();
		}
	}

	static class MyEntry<K, V> implements Map.Entry<K, V> {
		K key;
		V value;
		Date date = new Date();

		public MyEntry(K k, V v) {
			super();
			this.key = k;
			this.value = v;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public V setValue(V value) {
			this.value = value;
			return value;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		// return map.containsValue(value);
		try {
			throw new Exception("MyHashMap containsValue!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("containsValue", e);
		}
		return false;
	}

	@Override
	public V remove(Object key) {
		MyEntry<K, V> e = map.remove(key);
		return (e == null ? null : e.getValue());
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		try {
			throw new Exception("MyHashMap putAll!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("putAll", e);
		}
		// map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		try {
			throw new Exception("MyHashMap values!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("values", e);
		}
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<Entry<K, MyEntry<K, V>>> set = map.entrySet();
		Set<java.util.Map.Entry<K, V>> s = new HashSet<>();
		for (Entry<K, MyEntry<K, V>> e : set) {
			s.add(e.getValue());
		}
		return s;
	}
}
