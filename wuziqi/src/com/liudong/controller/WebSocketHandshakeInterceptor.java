package com.liudong.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.liudong.model.Key;
import com.liudong.model.User;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

	private static Logger logger = LoggerFactory
	        .getLogger(HandshakeInterceptor.class);

	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
	        ServerHttpResponse response, WebSocketHandler wsHandler,
	        Map<String, Object> attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest()
			        .getSession(false);
			if (session != null) {
				User u = (User) session.getAttribute(Key.USER_SESSION_KEY);
				if (null == u) {
					logger.info("用户未设置昵称进入！" + session.getId());
					return false;
				}
				attributes.put(Key.WEBSOCKET_USERNAME, u.getUserName());
				attributes.put(Key.WEBSOCKET_HTTP_SESS, session);
			}
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request,
	        ServerHttpResponse response, WebSocketHandler wsHandler,
	        Exception exception) {

	}
}
