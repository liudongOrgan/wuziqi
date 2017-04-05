package com.liudong.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.liudong.model.JsonResult;
import com.liudong.model.Key;
import com.liudong.model.User;

@Controller
@RequestMapping("/login")
public class LoginCtrl {
	@RequestMapping("jq")
	public ModelAndView jqm(HttpSession httpSession) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("roomlist");
		return mv;
	}
	
	@RequestMapping("page")
	public ModelAndView loginPage(HttpSession httpSession) {
		if (null != httpSession.getAttribute(Key.USER_SESSION_KEY)) {
			return new ModelAndView("redirect:/index");
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		return mv;
	}

	@RequestMapping("data")
	@ResponseBody
	public JsonResult<User> login(String name, HttpSession httpSession) {

		JsonResult<User> j = new JsonResult<User>();
		if (null != httpSession.getAttribute(Key.USER_SESSION_KEY)) {
			j.setStatus("created");
			return j;
		}
		if (StringUtils.isBlank(name)) {
			j.setStatus("empty");
			return j;
		}
		name = name.trim();
		if (name.length() > 10) {
			j.setStatus("long");
			return j;
		}
		if (null != Cache.getUserByName(name)) {
			j.setStatus("exists");
			return j;
		}
		User u = new User();
		u.setUserName(name);
		Cache.addUser(u);
		httpSession.setAttribute(Key.USER_SESSION_KEY, u);
		j.setStatus("success");
		j.setContent(u);
		return j;
	}

}
