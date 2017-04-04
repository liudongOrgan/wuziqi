package com.liudong.listener;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import com.liudong.controller.Cache;
import com.liudong.model.Key;
import com.liudong.model.Room;
import com.liudong.model.User;

public class SessionListener implements HttpSessionListener {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		SessionCache.addSession(arg0.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession sess = event.getSession();
		Room r = (Room) sess.getAttribute(Key.USER_SESSION_ROOM_KEY);
		User u = (User) sess.getAttribute(Key.USER_SESSION_KEY);
		logger.info("session destroyed! " + (null != u ? u.getUserName() : ""));
		if (null != u)
			Cache.removeUser(u);
		if (null != r || null != u)
			Cache.backRoom(u, r);
		WebSocketSession wSess = (WebSocketSession) sess.getAttribute(Key.HTTP_WEBSOCKET_SESSION_KEY);
		if (null != wSess && wSess.isOpen()) {
			try {
				wSess.close();
			} catch (IOException e) {
				logger.error("session invalidated, delete websocket exception!", e);
			}
		}
		SessionCache.delSession(sess.getId());
	}

}
