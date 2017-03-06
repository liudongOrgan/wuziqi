package com.liudong.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.liudong.model.Chess;
import com.liudong.model.Chessboard;
import com.liudong.model.JsonResult;
import com.liudong.model.Key;
import com.liudong.model.Room;
import com.liudong.model.User;

@Service
public class ChessServices {
	/**
	 * 用户连接后处理
	 * 
	 * @param session
	 * @param handler
	 */
	public void connected(WebSocketSession session, SystemWebSocketHandler handler) {
		HttpSession sess = (HttpSession) session.getAttributes().get(Key.WEBSOCKET_HTTP_SESS);
		Room r = (Room) sess.getAttribute(Key.USER_SESSION_ROOM_KEY);
		User u = (User) sess.getAttribute(Key.USER_SESSION_KEY);
		if (null == r || null == u)
			return;
		Chessboard board = ChessboardCache.getBoardByRoom(r);
		if (null == board) {
			ChessboardCache.addBoardByRoom(r);
			board = ChessboardCache.getBoardByRoom(r);
		}
		if (u.getUserName().equals(r.getUser1Name())) {
			if (false == board.isUser1Ready())
				board.setUser1Ready(true);
		}
		if (u.getUserName().equals(r.getUser2Name())) {
			if (false == board.isUser2Ready())
				board.setUser2Ready(true);
		}
		JsonResult<Chess> j = new JsonResult<Chess>();
		Chess c = new Chess();
		c.setNextUserName(r.getUser1Name());
		if (board.getStatus() == 2 && StringUtils.isNotBlank(board.getNextChessUserName())) {
			c.setNextUserName(board.getNextChessUserName());
		}
		j.setStatus("connected-one");
		if (true == board.isUser1Ready() && true == board.isUser2Ready()) {
			j.setStatus("connected");
			c.setOpUserName(r.getUser1Name() + "," + r.getUser2Name());
		}
		j.setContent(c);
		TextMessage message = new TextMessage(JSON.toJSONString(j));
		r.setStatus(2);
		handler.sendMessageToRoom(message, r);
	}

	/**
	 * 落子
	 * 
	 * @param session
	 * @param handler
	 * @param chess
	 */
	public void chess(WebSocketSession session, SystemWebSocketHandler handler, Chess chess) {
		HttpSession sess = (HttpSession) session.getAttributes().get(Key.WEBSOCKET_HTTP_SESS);
		Room r = (Room) sess.getAttribute(Key.USER_SESSION_ROOM_KEY);
		User u = (User) sess.getAttribute(Key.USER_SESSION_KEY);
		if (null == r || null == u)
			return;
		Chessboard board = ChessboardCache.getBoardByRoom(r);
		chess.setOpUserName(u.getUserName());
		if (false == board.chess(chess)) {
			return;
		}
		board.checkWin();

		JsonResult<Chess> j = new JsonResult<Chess>();
		j.setStatus("chess");
		if (true == board.isOver())
			j.setStatus("over");
		j.setContent(chess);
		TextMessage message = new TextMessage(JSON.toJSONString(j));
		handler.sendMessageToRoom(message, r);

	}

	// 退出
	public void close(WebSocketSession session, SystemWebSocketHandler hand) {
		HttpSession sess = (HttpSession) session.getAttributes().get(Key.WEBSOCKET_HTTP_SESS);
		if (null == sess)
			return;
		Room r = (Room) sess.getAttribute(Key.USER_SESSION_ROOM_KEY);
		User u = (User) sess.getAttribute(Key.USER_SESSION_KEY);
		if (null == r || null == u)
			return;
		// Cache.backRoom(u, r);
		// sess.removeAttribute(Key.USER_SESSION_ROOM_KEY);
	}

	public void backRoom(Room r) {
		JsonResult<Chess> j = new JsonResult<Chess>();
		j.setStatus("exit");
		TextMessage message = new TextMessage(JSON.toJSONString(j));
		SystemWebSocketHandler.sendMessageToUser(r.getUser1Name(), message);
		SystemWebSocketHandler.sendMessageToUser(r.getUser2Name(), message);
	}

}
