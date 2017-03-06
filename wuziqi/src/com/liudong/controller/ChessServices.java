package com.liudong.controller;

import javax.servlet.http.HttpSession;

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
			board.setUser1Ready(true);
		}
		if (u.getUserName().equals(r.getUser2Name())) {
			board.setUser2Ready(true);
		}
		if (true == board.isUser1Ready() && true == board.isUser2Ready()) {
			JsonResult<Chess> j = new JsonResult<Chess>();
			Chess c = new Chess();
			c.setNextUserName(r.getUser1Name());
			j.setStatus("chess");
			j.setContent(c);
			TextMessage message = new TextMessage(JSON.toJSONString(j));
			handler.sendMessageToRoom(message, r);
		}
	}

	public void chess(WebSocketSession session, SystemWebSocketHandler hand) {

	}

}
