package com.liudong.model;

import com.alibaba.fastjson.JSONObject;

public class Chess {
	private String opUserName;
	private int x;
	private int y;
	private Color color;
	private String nextUserName;

	public String getOpUserName() {
		return opUserName;
	}

	public void setOpUserName(String opUserName) {
		this.opUserName = opUserName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getNextUserName() {
		return nextUserName;
	}

	public void setNextUserName(String nextUserName) {
		this.nextUserName = nextUserName;
	}

	public Chess copy() {
		String json = JSONObject.toJSONString(this);
		Chess tmp = JSONObject.parseObject(json, Chess.class);
		return tmp;
	}

}
