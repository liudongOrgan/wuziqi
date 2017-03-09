package com.liudong.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liudong.annotation.SocketMapping;
import com.liudong.model.Chess;
import com.liudong.model.JsonResult;
import com.liudong.model.Key;
import com.liudong.model.Room;
import com.liudong.model.User;

@Controller
public class ChessBoardController {
	@Autowired
	ChessServices chessService;

	@SocketMapping(path = "connected")
	public void connected(SystemWebSocketHandler handler,
			WebSocketSession session) {
		chessService.connected(session, handler);
	}

	@SocketMapping(path = "chess")
	public boolean chess(JSONObject mess, SystemWebSocketHandler handler,
			WebSocketSession session) throws Exception {
		HttpSession sess = (HttpSession) session.getAttributes().get(
				Key.WEBSOCKET_HTTP_SESS);
		Room r = (Room) sess.getAttribute(Key.USER_SESSION_ROOM_KEY);
		User u = (User) sess.getAttribute(Key.USER_SESSION_KEY);
		if (null == r || null == u)
			return false;
		Chess chess = new Chess();
		int x = mess.getIntValue("x");
		int y = mess.getIntValue("y");
		boolean validate = x > -1 && x < 16;
		validate = validate && y > -1 && y < 16;
		if (false == validate) {
			throw new Exception("接收到坐标错误!");
		}
		chess.setX(x);
		chess.setY(y);
		chess.setOpUserName(u.getUserName());
		if (false == r.chess(chess)) {
			return false;
		}
		r.checkWin();

		JsonResult<Chess> j = new JsonResult<Chess>();
		j.setUrl("chess");
		j.setStatus("chess");
		if (true == r.isOver()){
			j.setStatus("over");
			j.setUrl("over");
		}
		j.setContent(chess);
		TextMessage message = new TextMessage(JSON.toJSONString(j));
		handler.sendMessageToRoom(message, r);
		return true;
	}
}
