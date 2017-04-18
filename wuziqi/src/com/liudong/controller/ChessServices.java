package com.liudong.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liudong.model.Chess;
import com.liudong.model.JsonResult;
import com.liudong.model.Key;
import com.liudong.model.Room;
import com.liudong.model.User;
import com.liudong.model.constant.RoomStatus;
import com.liudong.util.ChessboardCode;
import com.liudong.util.SessionUtil;

@Service
public class ChessServices {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 用户连接后处理
	 * 
	 * @param session
	 * @param handler
	 */
	public void connected(WebSocketSession session,
	        SystemWebSocketHandler handler) {
		HttpSession sess = (HttpSession) session.getAttributes().get(
		        Key.WEBSOCKET_HTTP_SESS);
		Room r = (Room) sess.getAttribute(Key.USER_SESSION_ROOM_KEY);
		User u = (User) sess.getAttribute(Key.USER_SESSION_KEY);
		if (null == r || null == u)
			return;
		sess.setAttribute(Key.HTTP_WEBSOCKET_SESSION_KEY, session);
		if (u.getUserName().equals(r.getUser1Name())) {
			if (false == r.isUser1Ready())
				r.setUser1Ready(true);
		}
		if (u.getUserName().equals(r.getUser2Name())) {
			if (false == r.isUser2Ready())
				r.setUser2Ready(true);
		}
		JsonResult<Chess> j = new JsonResult<Chess>();
		Chess c = new Chess();
		c.setNextUserName(r.getUser1Name());
		if (r.getStatus() == RoomStatus.ING
		        && StringUtils.isNotBlank(r.getNextChessUserName())) {
			c.setNextUserName(r.getNextChessUserName());
		}
		j.setStatus("connected-one");
		if (true == r.isUser1Ready() && true == r.isUser2Ready()) {
			j.setStatus("connected");
			c.setOpUserName(r.getUser1Name() + "," + r.getUser2Name());
			if (RoomStatus.ING != r.getStatus()) {
				j.setStatus("connected-first");
				r.reset();
			}
			r.setStatus(RoomStatus.ING);
		}
		j.setContent(c);
		j.setUrl("connected");
		TextMessage message = new TextMessage(JSON.toJSONString(j));
		checkAndSendChessList(session, r);
		handler.sendMessageToRoom(message, r);
	}

	// 游戏进行中 刷新页面 恢复棋盘
	private void checkAndSendChessList(WebSocketSession session, Room r) {
		if (r.getStatus() != RoomStatus.ING) {
			return;
		}
		JsonResult<List<Chess>> res = new JsonResult<List<Chess>>();
		res.setContent(r.getChessedList());
		res.setStatus("recover");
		res.setUrl("recover");
		String json = JSONObject.toJSONString(res);
		TextMessage message = new TextMessage(json);
		if (session.isOpen()) {
			try {
				session.sendMessage(message);
			} catch (IOException e) {
				logger.error("发送恢复棋盘信息失败!", e);
			}
		}
	}

	/**
	 * 落子
	 * 
	 * @param session
	 * @param handler
	 * @param chess
	 */
	public void chess(WebSocketSession session, SystemWebSocketHandler handler,
	        Chess chess) {
		HttpSession sess = (HttpSession) session.getAttributes().get(
		        Key.WEBSOCKET_HTTP_SESS);
		Room r = (Room) sess.getAttribute(Key.USER_SESSION_ROOM_KEY);
		User u = (User) sess.getAttribute(Key.USER_SESSION_KEY);
		if (null == r || null == u)
			return;
		// Chessboard board = ChessboardCache.getBoardByRoom(r);
		chess.setOpUserName(u.getUserName());
		if (false == r.chess(chess)) {
			return;
		}
		r.checkWin();

		JsonResult<Chess> j = new JsonResult<Chess>();
		j.setStatus("chess");
		if (true == r.isOver())
			j.setStatus("over");
		j.setContent(chess);
		TextMessage message = new TextMessage(JSON.toJSONString(j));
		SystemWebSocketHandler.sendMessageToRoom(message, r);
	}

	// 退出
	public void close(WebSocketSession session, SystemWebSocketHandler hand) {
	}

	/**
	 * 玩家认输
	 */
	public boolean defeated(WebSocketSession session,
	        SystemWebSocketHandler hand, HttpSession sess) {

		Room r = SessionUtil.getRoomBySession(sess);
		User u = SessionUtil.getUserBySession(sess);
		if (null == r || null == u)
			return false;
		r.setStatus(RoomStatus.OVER);
		r.setUser1Ready(false);
		r.setUser2Ready(false);
		r.reset();
		JsonResult<String> j = new JsonResult<String>();
		j.setStatus(ChessboardCode.DEFEATED_REQUEST);
		j.setUrl(ChessboardCode.DEFEATED_REQUEST);
		j.setContent(u.getUserName());
		TextMessage message = new TextMessage(JSON.toJSONString(j));
		SystemWebSocketHandler.sendMessageToRoom(message, r);
		return true;
	}

	/**
	 * 重新开始<br>
	 * 通知对方玩家,都准备之后交换棋子
	 */
	public boolean restart(WebSocketSession session,
	        SystemWebSocketHandler hand, HttpSession sess) {

		Room r = SessionUtil.getRoomBySession(sess);
		User u = SessionUtil.getUserBySession(sess);
		if (null == r || null == u)
			return false;
		if (StringUtils.isBlank(r.getUser1Name())
		        || StringUtils.isBlank(r.getUser2Name())) {
			return false;
		}
		if (null != r.getUser1Name()
		        && r.getUser1Name().equals(u.getUserName())
		        && r.isUser1Ready() == false) {// 玩家1准备好了
			r.setUser1Ready(true);
		}
		if (null != r.getUser2Name()
		        && r.getUser2Name().equals(u.getUserName())
		        && r.isUser2Ready() == false) {// 玩家2准备好了
			r.setUser2Ready(true);
		}
		if (StringUtils.isNotBlank(r.getUser1Name())
		        && StringUtils.isNotBlank(r.getUser2Name()) && r.isUser1Ready()
		        && r.isUser2Ready()) { // 交换棋子,交换room的user1name,user2name 即可.
			String u1n = r.getUser1Name();
			String u2n = r.getUser2Name();
			r.setUser1Name(u2n);
			r.setUser2Name(u1n);
		}
		JsonResult<Chess> j = new JsonResult<Chess>();
		j.setStatus("exit");
		if (r.isUser1Ready() && r.isUser2Ready()) { // 游戏重新开始,刷新页面
			r.reset();
			r.setStatus(RoomStatus.ING);
			j.setStatus(ChessboardCode.RESTART_RELOAD_PAGE);
			j.setUrl(ChessboardCode.RESTART_RELOAD_PAGE);
			TextMessage message = new TextMessage(JSON.toJSONString(j));
			SystemWebSocketHandler.sendMessageToUser(r.getUser1Name(), message);
			SystemWebSocketHandler.sendMessageToUser(r.getUser2Name(), message);
		} else { // 向对方发送重新开始请求
			j.setStatus(ChessboardCode.RESTART_REQUEST);
			j.setUrl(ChessboardCode.RESTART_REQUEST);
			String toUserName = r.getUser1Name().equals(u.getUserName()) ? r
			        .getUser2Name() : r.getUser1Name();
			TextMessage message = new TextMessage(JSON.toJSONString(j));
			SystemWebSocketHandler.sendMessageToUser(toUserName, message);
		}
		return true;
	}

	public void backRoom(Room r) {
		JsonResult<Chess> j = new JsonResult<Chess>();
		j.setStatus("exit");
		TextMessage message = new TextMessage(JSON.toJSONString(j));
		SystemWebSocketHandler.sendMessageToUser(r.getUser1Name(), message);
		SystemWebSocketHandler.sendMessageToUser(r.getUser2Name(), message);
	}

}
