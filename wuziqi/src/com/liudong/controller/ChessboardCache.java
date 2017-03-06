package com.liudong.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.liudong.model.Chessboard;
import com.liudong.model.Room;

public class ChessboardCache {
	private static Map<String, Chessboard> boards = new HashMap<String, Chessboard>();

	public static Chessboard getBoardByRoom(Room r) {
		return boards.get(r.getRoomeName());
	}

	public static boolean addBoardByRoom(Room r) {
		return addBoardByRoom(r, null);
	}

	public static synchronized boolean addBoardByRoom(Room r, Chessboard c) {
		if (null == r || StringUtils.isBlank(r.getRoomeName()) || boards.get(r.getRoomeName()) != null)
			return false;
		if (null == c)
			c = new Chessboard(r);
		boards.put(r.getRoomeName(), c);
		return true;
	}

	/**
	 * 删除房间棋盘
	 * 
	 * @param r
	 * @return
	 */
	public static boolean removeBoardByRoom(Room r) {
		if (null == r || StringUtils.isBlank(r.getRoomeName()) || boards.get(r.getRoomeName()) == null)
			return false;
		boards.remove(r.getRoomeName());
		return true;
	}

}
