package com.liudong.model;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.liudong.model.constant.RoomStatus;

public class Room extends ReentrantLock {
	private static final long serialVersionUID = 3960279532871689856L;
	private RoomStatus status = RoomStatus.DEFAULT;// 0默认 待加入 ,1 等待开始, 2 进行中,
													// 3结束
	private String user1Name;// 黑棋用户
	private String user2Name;// 白棋用户
	private String roomeName;// 房间名
	private Date createDate;// 创建时间
	private volatile Chessboard chessBoard = new Chessboard(this);
	private volatile boolean user1Ready = false;
	private volatile boolean user2Ready = false;

	public void reset() {
		chessBoard = new Chessboard(this);
	}

	public void setStatus(RoomStatus status) {
		this.status = status;
	}

	/**
	 * 黑棋用户
	 */
	public String getUser1Name() {
		return user1Name;
	}

	public void setUser1Name(String user1Name) {
		this.user1Name = user1Name;
	}

	/**
	 * 白棋用户
	 */
	public String getUser2Name() {
		return user2Name;
	}

	public void setUser2Name(String user2Name) {
		this.user2Name = user2Name;
	}

	/**
	 * 房间名称
	 */
	public String getRoomeName() {
		return roomeName;
	}

	public void setRoomeName(String roomeName) {
		this.roomeName = roomeName;
	}

	/**
	 * 房间创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 棋盘
	 */
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

	/**
	 * 当前房间状态
	 */
	public RoomStatus getStatus() {
		return status;
	}

	/**
	 * 获得当前下子用户名
	 */
	public String getNextChessUserName() {
		return chessBoard.getNextChessUserName();
	}

	/**
	 * 落子
	 */
	public boolean chess(Chess chess) {
		if (RoomStatus.ING != this.status)
			return false;
		return chessBoard.chess(chess);
	}

	/**
	 * 判断是否有人获胜
	 */
	public Color checkWin() {
		Color isWin = chessBoard.checkWin();
		if (Color.NONE != isWin) {
			this.status = RoomStatus.OVER;
			this.user1Ready = false;
			this.user2Ready = false;
		}

		return isWin;
	}

	/**
	 * 是否游戏结束,有人获胜及结束
	 */
	public boolean isOver() {
		return chessBoard.isOver();
	}

	/**
	 * 获得落子列表
	 */
	public List<Chess> getChessedList() {
		return chessBoard.getChessedList();
	}

}
