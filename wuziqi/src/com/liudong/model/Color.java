package com.liudong.model;

public enum Color {
	NONE(0), WHITE(2), BLACK(1);
	private int val;

	Color(int v) {
		this.val = v;
	}

	public int getVal() {
		return val;
	}
}
