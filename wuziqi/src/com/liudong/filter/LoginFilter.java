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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.liudong.listener.SessionCache;
import com.liudong.model.Key;
import com.liudong.model.User;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) arg0;
		HttpSession sess = req.getSession();
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
		System.out.println(uri + "    login filter " + sess.getId());
		User u = (User) sess.getAttribute(Key.USER_SESSION_KEY);
		System.out.println(req.getRequestURI());
		arg2.doFilter(arg0, arg1);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
