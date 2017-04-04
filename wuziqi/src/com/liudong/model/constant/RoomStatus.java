package com.liudong.model.constant;

public enum RoomStatus {
	/**
	 * 默认状态
	 */
	DEFAULT(0), // 默认状态
	/**
	 * 玩家进入房间 等待其他玩家进入
	 */
	WAIT(1), // 玩家进入房间 等待其他玩家进入
	/** 正在游戏中 */
	ING(2), // 正在游戏中
	/**
	 * 房间游戏结束
	 */
	OVER(3);// 房间游戏结束
	private int value;

	RoomStatus(int val) {
		this.value = val;
	}

	public int getVlaue() {
		return this.value;
	}
}
