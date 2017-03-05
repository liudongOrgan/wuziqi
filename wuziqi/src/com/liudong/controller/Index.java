package com.liudong.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liudong.model.JsonResult;
import com.liudong.model.Key;
import com.liudong.model.Room;

@Controller
@RequestMapping("/")
public class Index {

	@RequestMapping("index")
	public String index() {
		return "index";
	}

	@RequestMapping("wuziqi")
	public String index2() {
		return "wuziqi";
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
			// return j;
		}

		Room r = new Room();
		r.setRoomeName(roomName);
		r.setCreateDate(new Date());
		Object cur = httpSession.getAttribute(Key.USER_SESSION_KEY);
		r.setUser1Name(null == cur ? "房间名称" : cur.toString());
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
		j.setContent(null == r ? null : (Room) r);
		return j;
	}

}
