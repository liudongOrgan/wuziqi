package com.liudong.model;

import java.util.Date;

public class Room {
	private Integer status = 0;// 0默认 待加入 1 等待开始 2 进行中 3结束待销毁
	private String user1Name;
	private String user2Name;
	private String roomeName;
	private Date createDate;
	private volatile Chessboard chessBoard = new Chessboard(this);
	private volatile boolean user1Ready = false;
	private volatile boolean user2Ready = false;

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

	public Chessboard getChessBoard() {
		return chessBoard;
	}

	public boolean isUser1Ready() {
		return user1Ready;
	}

	public void setUser1Ready(boolean user1Ready) {
		this.user1Ready = user1Ready;
	}

	public boolean isUser2Ready() {
		return user2Ready;
	}

	public void setUser2Ready(boolean user2Ready) {
		this.user2Ready = user2Ready;
	}

	public Integer getStatus() {
		return status;
	}

	public String getNextChessUserName() {
		return chessBoard.getNextChessUserName();
	}

	public boolean chess(Chess chess) {
		return chessBoard.chess(chess);
	}

	public Color checkWin() {
		return chessBoard.checkWin();
	}

	public boolean isOver() {
		return chessBoard.isOver();
	}

}
