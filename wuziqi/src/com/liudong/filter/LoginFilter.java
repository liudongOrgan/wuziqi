package com.liudong.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.liudong.listener.SessionCache;
import com.liudong.model.Key;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) arg0;
		HttpServletResponse resp = (HttpServletResponse) arg1;
		HttpSession sess = req.getSession();

		if (false == checkLogin(sess)) {
			String uri = req.getRequestURI();
			if (uri.startsWith("/login") || uri.endsWith(".js")) {
				arg2.doFilter(arg0, arg1);
			} else {
				resp.sendRedirect("/login/page");
			}
			return;
		}

		sessionValidate(req, sess);
		arg2.doFilter(arg0, arg1);

	}

	private boolean checkLogin(HttpSession sess) {
		Object obj = sess.getAttribute(Key.USER_SESSION_KEY);
		if (null == obj || StringUtils.isBlank(obj.toString()))
			return false;
		return true;
	}

	private void sessionValidate(HttpServletRequest req, HttpSession sess) {
		String uri = req.getRequestURI();
		String str = req.getParameter("jsessionid");
		if (uri.contains("ws") && sess.getAttributeNames().hasMoreElements() == false && StringUtils.isNotBlank(str)) {
			// 某些手机浏览器 websocket握手http请求中没有带cookie
			HttpSession sessBefore = SessionCache.getSessionById(str);
			Enumeration<String> names = sessBefore.getAttributeNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				sess.setAttribute(name, sessBefore.getAttribute(name));
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
