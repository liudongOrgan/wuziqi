package com.liudong.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.liudong.model.JsonResult;
import com.liudong.model.Key;
import com.liudong.model.Room;
import com.liudong.model.User;

@Controller
@RequestMapping("/")
public class Index {

	@RequestMapping("index")
	public String index() {
		return "index";
	}

	@RequestMapping("wuziqi")
	public ModelAndView index2(HttpSession httpSession) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("wuziqi");
		mv.addObject("user", JSON.toJSONString(httpSession.getAttribute(Key.USER_SESSION_KEY)));
		mv.addObject("room", JSON.toJSONString(httpSession.getAttribute(Key.USER_SESSION_ROOM_KEY)));
		return mv;
	}

	@RequestMapping("createroom")
	@ResponseBody
	public JsonResult<Room> createRoom(String roomName, HttpSession httpSession) {
		JsonResult<Room> j = new JsonResult<Room>();
		if (Cache.getRoomeByName(roomName) != null) {
			j.setStatus("exists");
			return j;
		}
		if (null != httpSession.getAttribute(Key.USER_SESSION_ROOM_KEY)) {
			j.setStatus("created");
			return j;
		}

		Room r = new Room();
		r.setRoomeName(roomName);
		r.setCreateDate(new Date());
		r.setStatus(1);
		Object cur = httpSession.getAttribute(Key.USER_SESSION_KEY);
		r.setUser1Name(null == cur ? "房间名称" : ((User) cur).getUserName());
		if (true == Cache.addRoom(r)) {
			httpSession.setAttribute(Key.USER_SESSION_ROOM_KEY, r);
			j.setStatus("success");
			j.setContent(r);
			return j;
		}

		j.setStatus("error");
		return j;
	}

	@RequestMapping("loadroomlist")
	@ResponseBody
	public JsonResult<List<Room>> loadRoomList() {
		List<Room> list = Cache.getRoomList();
		JsonResult<List<Room>> j = new JsonResult<List<Room>>();
		j.setStatus("success");
		j.setContent(list);
		return j;
	}

	@RequestMapping("getcreatedroom")
	@ResponseBody
	public JsonResult<Room> getCreatedRoom(HttpSession httpSession) {
		JsonResult<Room> j = new JsonResult<Room>();
		j.setStatus("success");
		Object r = httpSession.getAttribute(Key.USER_SESSION_ROOM_KEY);
		if (null != r) {
			Room room = (Room) r;
			if (StringUtils.isBlank(room.getUser1Name()) && StringUtils.isBlank(room.getUser2Name())) {
				httpSession.removeAttribute(Key.USER_SESSION_ROOM_KEY);
				r = httpSession.getAttribute(Key.USER_SESSION_ROOM_KEY);
			}
		}
		j.setContent(null == r ? null : (Room) r);
		return j;
	}

	@RequestMapping("createusername")
	@ResponseBody
	public JsonResult<User> createusername(String username, HttpSession httpSession) {
		JsonResult<User> j = new JsonResult<User>();
		if (null != httpSession.getAttribute(Key.USER_SESSION_KEY)) {
			j.setStatus("created");
			return j;
		}
		if (StringUtils.isBlank(username)) {
			j.setStatus("empty");
			return j;
		}
		username = username.trim();
		if (null != Cache.getUserByName(username)) {
			j.setStatus("exists");
			return j;
		}
		User u = new User();
		u.setUserName(username);
		Cache.addUser(u);
		httpSession.setAttribute(Key.USER_SESSION_KEY, u);
		j.setStatus("success");
		j.setContent(u);
		return j;
	}

	@RequestMapping("getuserinfo")
	@ResponseBody
	public JsonResult<User> getuserinfo(HttpSession httpSession) {
		JsonResult<User> j = new JsonResult<User>();
		Object obj = httpSession.getAttribute(Key.USER_SESSION_KEY);
		j.setStatus("success");
		j.setContent(null == obj ? null : (User) obj);
		return j;
	}

	@RequestMapping("enterroom")
	@ResponseBody
	public JsonResult<Room> enterroom(String roomname, HttpSession httpSession) {
		JsonResult<Room> j = new JsonResult<Room>();
		User u = (User) httpSession.getAttribute(Key.USER_SESSION_KEY);
		Room r = (Room) httpSession.getAttribute(Key.USER_SESSION_ROOM_KEY);
		if (null != r) {
			backroom(roomname, httpSession);
		}
		if (true == Cache.enterRoom(u, roomname)) {
			r = Cache.getRoomeByName(roomname);
			httpSession.setAttribute(Key.USER_SESSION_ROOM_KEY, r);
			j.setStatus("success");
			return j;
		} else {
			j.setStatus("error");
			return j;
		}
	}

	@RequestMapping("backroom")
	@ResponseBody
	public JsonResult<Room> backroom(String roomname, HttpSession httpSession) {
		JsonResult<Room> j = new JsonResult<Room>();
		j.setStatus("success");
		User u = (User) httpSession.getAttribute(Key.USER_SESSION_KEY);
		Room r = Cache.getRoomeByName(roomname);
		if (null == r)
			return null;
		Cache.backRoom(u, r);
		httpSession.removeAttribute(Key.USER_SESSION_ROOM_KEY);
		return j;
	}

	@RequestMapping("checkstart")
	@ResponseBody
	public JsonResult<Room> checkstart(HttpSession httpSession) {
		JsonResult<Room> j = new JsonResult<Room>();
		Room r = (Room) httpSession.getAttribute(Key.USER_SESSION_ROOM_KEY);
		j.setStatus("error");
		if (null == r)
			return j;

		if (StringUtils.isNotBlank(r.getUser1Name()) && StringUtils.isNotBlank(r.getUser2Name()))
			j.setStatus("success");

		return j;
	}

}
