package com.liudong.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liudong.model.JsonResult;
import com.liudong.model.Key;
import com.liudong.model.Room;
import com.liudong.model.User;

@Controller
@RequestMapping("/valid")
public class ValidCtrl {

	/**
	 * 检查并保持session有效性.</br> 返回房间信息,Fail表示出错跳转房间列表页面
	 */
	@RequestMapping("checkandkeep")
	@ResponseBody
	public JsonResult<Room> checkAndKepp(HttpSession httpSession) {
		JsonResult<Room> j = new JsonResult<Room>();
		User u = (User) httpSession.getAttribute(Key.USER_SESSION_KEY);
		Room r = (Room) httpSession.getAttribute(Key.USER_SESSION_ROOM_KEY);
		if (null == r || null == u) {
			j.setStatus(JsonResult.FAIL);
			return j;
		}
		u = Cache.getUserByName(u.getUserName());
		r = Cache.getRoomeByName(r.getRoomeName());
		if (null == r || null == u) {
			j.setStatus(JsonResult.FAIL);
			return j;
		}
		httpSession.setAttribute(Key.USER_SESSION_KEY, u);
		httpSession.setAttribute(Key.USER_SESSION_ROOM_KEY, r);
		j.setStatus("success");
		j.setContent(r);
		return j;
	}
}
