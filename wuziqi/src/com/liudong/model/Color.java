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

	public static Color getByVal(int v) {
		switch (v) {
		case 0:
			return NONE;
		case 1:
			return BLACK;
		case 2:
			return WHITE;
		}
		return null;
	}
}
