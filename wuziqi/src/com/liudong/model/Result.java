package com.liudong.model;

public class Result {
	private int[][] winPath = new int[5][2]; // 赢棋路径
	private String winUser;

	public int[][] getWinPath() {
		return winPath;
	}

	public void setWinPath(int[][] winPath) {
		this.winPath = winPath;
	}

	public String getWinUser() {
		return winUser;
	}

	public void setWinUser(String winUser) {
		this.winUser = winUser;
	}

}
