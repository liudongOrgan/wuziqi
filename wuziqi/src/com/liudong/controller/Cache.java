package com.liudong.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.liudong.model.Room;
import com.liudong.model.User;
import com.liudong.model.constant.RoomStatus;
import com.liudong.util.MyHashMap;

public class Cache {
	private static Map<String, Room> rooms = new MyHashMap<String, Room>();

	private static Map<String, User> users = new MyHashMap<String, User>();

	public static Room getRoomeByName(String roomeName) {
		return rooms.get(roomeName);
	}

	public static boolean addRoom(Room r) {
		if (rooms.get(r.getRoomeName()) != null) {
			return false;
		}
		rooms.put(r.getRoomeName(), r);
		return true;
	}

	public static List<Room> getRoomList() {
		List<Room> list = new ArrayList<Room>(rooms.size());
		for (Map.Entry<String, Room> ent : rooms.entrySet()) {
			list.add(ent.getValue());
		}
		return list;
	}

	public static User getUserByName(String userName) {
		return users.get(userName);
	}

	public static boolean addUser(User u) {
		if (users.get(u.getUserName()) != null)
			return false;
		users.put(u.getUserName(), u);
		return true;
	}

	public static boolean removeUser(User u) {
		users.remove(u.getUserName());
		return true;
	}

	public static boolean enterRoom(User u, String roomName) {
		if (null == u || StringUtils.isBlank(u.getUserName()))
			return false;
		if (StringUtils.isBlank(roomName))
			return false;
		Room r = rooms.get(roomName);
		if (null == r)
			return false;
		r.lock();
		try {
			if (null == r || RoomStatus.ING == r.getStatus())
				return false;
			if (StringUtils.isNotBlank(r.getUser1Name())
			        && StringUtils.isBlank(r.getUser2Name())) {
				r.setUser2Name(u.getUserName());
				return true;
			}
			if (StringUtils.isNotBlank(r.getUser2Name())
			        && StringUtils.isBlank(r.getUser1Name())) {
				r.setUser1Name(u.getUserName());
				return true;
			}
		} finally {
			r.unlock();
		}
		return false;
	}

	/**
	 * 离开房间
	 * 
	 * @param u
	 * @param r
	 * @return
	 */
	public static boolean backRoom(User u, Room r) {
		r = rooms.get(r.getRoomeName());
		if (u.getUserName().equals(r.getUser1Name())) {
			r.setUser1Name(null);
			r.setStatus(RoomStatus.OVER);
			r.reset();
			r.setUser1Ready(false);
		}
		if (u.getUserName().equals(r.getUser2Name())) {
			r.setUser2Name(null);
			r.setStatus(RoomStatus.OVER);
			r.reset();
			r.setUser2Ready(false);
		}
		if (StringUtils.isBlank(r.getUser1Name())
		        && StringUtils.isBlank(r.getUser2Name())) {
			rooms.remove(r.getRoomeName());
		}
		return true;
	}

	public static boolean exitSys(User u, Room r) {
		if (null != r) {
			backRoom(u, r);
		}
		if (null != u) {
			removeUser(u);
		}

		return true;
	}

}
