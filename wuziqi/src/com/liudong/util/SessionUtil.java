package com.liudong.util;

import javax.servlet.http.HttpSession;

import com.liudong.controller.Cache;
import com.liudong.model.Key;
import com.liudong.model.Room;
import com.liudong.model.User;

public class SessionUtil {

	/**
	 * 因为session中开始保存的是user对象,但是后来cache增加了超时时间<br>
	 * 为了防止每次从session取导致cache中对象失效,之后从该方法中获取user对象
	 * 
	 */
	public static User getUserBySession(HttpSession sess) {
		User u = (User) sess.getAttribute(Key.USER_SESSION_KEY);
		if (null == u)
			return null;
		u = Cache.getUserByName(u.getUserName());
		if (null == u)
			return null;
		sess.setAttribute(Key.USER_SESSION_KEY, u);
		return u;
	}

	/**
	 * 因为session中开始保存的是Room对象,但是后来cache增加了超时时间<br>
	 * 为了防止每次从session取导致cache中对象失效,之后从该方法中获取Room对象
	 * 
	 */
	public static Room getRoomBySession(HttpSession sess) {
		Room r = (Room) sess.getAttribute(Key.USER_SESSION_ROOM_KEY);
		if (null == r)
			return null;
		r = Cache.getRoomeByName(r.getRoomeName());
		if (null == r)
			return null;
		sess.setAttribute(Key.USER_SESSION_ROOM_KEY, r);
		return r;
	}
}
