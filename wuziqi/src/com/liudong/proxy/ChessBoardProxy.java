package com.liudong.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONObject;
import com.liudong.annotation.SocketMapping;
import com.liudong.controller.ChessBoardCtrl;
import com.liudong.controller.SystemWebSocketHandler;

@Service
public class ChessBoardProxy {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ChessBoardCtrl chessBoardController;

	/**
	 * 实现基本的代理。 缺少url冲突的处理
	 * 
	 * @param wSession
	 * @param handler
	 * @param mess
	 */
	public void doService(WebSocketSession wSession, SystemWebSocketHandler handler, JSONObject mess) {
		String url = mess.getString("url");
		if (StringUtils.isBlank(url))
			return;
		Method[] methods = chessBoardController.getClass().getMethods();

		for (Method mt : methods) {
			if (mt.isAnnotationPresent(SocketMapping.class)) {
				SocketMapping st = mt.getAnnotation(SocketMapping.class);
				String path = st.path();
				if (url.matches(path)) {
					try {
						invokeMethod(mt, wSession, handler, mess);
					} catch (Exception e) {
						logger.error("反射调用方法出错！", e);
					}
					return;
				}
			}

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void invokeMethod(Method mt, WebSocketSession wSession, SystemWebSocketHandler handler, JSONObject mess)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class[] cls = mt.getParameterTypes();
		if (cls.length < 1) {
			mt.invoke(chessBoardController);
			return;
		}
		Object[] obj = new Object[] { wSession, handler, mess };
		Object[] arg = new Object[cls.length];
		for (int i = 0; i < cls.length; i++) {
			for (Object o : obj) {
				Class c = cls[i];
				if (c.isAssignableFrom(o.getClass())) {
					arg[i] = o;
					break;
				}
			}
		}
		mt.invoke(chessBoardController, arg);
	}

}
