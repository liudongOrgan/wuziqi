package com.liudong.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

public class SessionCache {
	private static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<String, HttpSession>();

	public static void addSession(HttpSession sess) {
		if (null == sess) {
			return;
		}
		sessionMap.put(sess.getId(), sess);
	}

	public static void delSession(String sessId) {
		if (StringUtils.isBlank(sessId)) {
			return;
		}
		sessionMap.remove(sessId);
	}

	public static HttpSession getSessionById(String sessId) {
		if (StringUtils.isBlank(sessId))
			return null;
		return sessionMap.get(sessId);
	}
}
