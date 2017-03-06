package com.liudong.model;

import java.util.Date;

public class Room {
	private Integer status = 0;// 0默认 待加入 1 等待开始 2 进行中 3结束待销毁
	private String user1Name;
	private String user2Name;
	private String roomeName;
	private Date createDate;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUser1Name() {
		return user1Name;
	}

	public void setUser1Name(String user1Name) {
		this.user1Name = user1Name;
	}

	public String getUser2Name() {
		return user2Name;
	}

	public void setUser2Name(String user2Name) {
		this.user2Name = user2Name;
	}

	public String getRoomeName() {
		return roomeName;
	}

	public void setRoomeName(String roomeName) {
		this.roomeName = roomeName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
