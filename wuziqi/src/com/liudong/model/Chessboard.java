package com.liudong.model;

public class Chessboard {
	private Room room = null;
	private int[][] board = new int[17][17];
	private boolean user1Ready = false;
	private boolean user2Ready = false;
	private short over = 0;

	public Chessboard(Room r) {
		this.room = r;
	}

	public synchronized boolean chess(Chess c) {
		if (board[c.getX()][c.getY()] != Color.NONE.getVal()) {
			return false;
		}
		if (c.getOpUserName().equals(room.getUser1Name())) {
			board[c.getX()][c.getY()] = Color.BLACK.getVal();
			c.setColor(Color.BLACK);
		}
		if (c.getOpUserName().equals(room.getUser2Name())) {
			board[c.getX()][c.getY()] = Color.WHITE.getVal();
			c.setColor(Color.WHITE);
		}
		return true;
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

	public void setUser1Over(String userName) {
		if (userName.equals(room.getUser1Name()) && 0 == over % 10) {
			over += 1;
		}
	}

	public void setUser2Over(String userName) {
		if (userName.equals(room.getUser2Name()) && over < 10) {
			over += 10;
		}
	}

}
