package com.liudong.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liudong.model.Room;

public class Cache {
	private static Map<String, Room> rooms = new HashMap<String, Room>();

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
		for(Map.Entry<String, Room> ent : rooms.entrySet()){
			list.add(ent.getValue());
		}
		return list;
	}
}
