package com.liudong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class Index {
	@RequestMapping("index")
	public String index() {
		return "index";
	}

	@RequestMapping("wuziqi/*.do")
	public String index2() {
		return "index";
	}
}
