package com.liudong.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.liudong.model.Key;
import com.liudong.model.Room;

public class SystemWebSocketHandler implements WebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(SystemWebSocketHandler.class);

	private static final Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();
	@Autowired
	ChessServices chessService;

	// 连接建立后处理
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws IOException {
		logger.debug("connect to the websocket success......");
		String userName = (String) session.getAttributes().get(Key.WEBSOCKET_USERNAME);
		if (StringUtils.isNotBlank(userName)) {
			WebSocketSession sessionold = sessions.get(userName);
			if (null != sessionold) {
				try {
					sessionold.close();
				} catch (IOException e) {
					logger.error("关闭旧连接出错！", e);
				}
			}
			sessions.put(userName, session);
			chessService.connected(sessionold, this);
		}
	}

	// 接收文本消息，并发送出去
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

		// sendMessageToUsers();
	}

	// 抛出异常时处理
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		logger.debug("websocket connection closed......");
		String userName = (String) session.getAttributes().get(Key.WEBSOCKET_USERNAME);
		if (StringUtils.isNotBlank(userName)) {
			sessions.remove(userName);
		}
	}

	// 连接关闭后处理
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		logger.debug("websocket connection closed......");
		String userName = (String) session.getAttributes().get(Key.WEBSOCKET_USERNAME);
		if (StringUtils.isNotBlank(userName)) {
			sessions.remove(userName);
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给房间用户发送消息
	 *
	 * @param message
	 */
	public void sendMessageToRoom(TextMessage message, Room r) {
		sendMessageToUser(r.getUser1Name(), message);
		sendMessageToUser(r.getUser2Name(), message);
	}

	/**
	 * 给某个用户发送消息
	 *
	 * @param userName
	 * @param message
	 */
	public void sendMessageToUser(String userName, TextMessage message) {
		WebSocketSession user = sessions.get(userName);
		if (user.getAttributes().get(Key.WEBSOCKET_USERNAME).equals(userName)) {
			try {
				if (user.isOpen()) {
					user.sendMessage(message);
				}
			} catch (IOException e) {
				logger.error("推送消息出错！", e);
			}
		}
	}
}