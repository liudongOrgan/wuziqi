package com.liudong.model;

public class Chessboard {
	private static final int BOARD_SIZE = 17;
	private Room room = null;
	private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
	private boolean user1Ready = false;
	private boolean user2Ready = false;
	private short over = 0;// 1表示 玩家1 退出 ，10表示 玩家2退出 ，11表示玩家1 和 玩家2 都退出
	private int[][] winPath = new int[5][2]; // 赢棋路径

	public Chessboard(Room r) {
		this.room = r;
	}

	/**
	 * 下子
	 * 
	 * @param c
	 */
	public synchronized boolean chess(Chess c) {
		if (board[c.getX()][c.getY()] != Color.NONE.getVal()) {
			return false;
		}
		if (c.getOpUserName().equals(room.getUser1Name())) {
			board[c.getX()][c.getY()] = Color.BLACK.getVal();
			c.setColor(Color.BLACK);
			c.setNextUserName(room.getUser2Name());
		}
		if (c.getOpUserName().equals(room.getUser2Name())) {
			board[c.getX()][c.getY()] = Color.WHITE.getVal();
			c.setColor(Color.WHITE);
			c.setNextUserName(room.getUser1Name());
		}
		return true;
	}

	// 用户1 是否准备好
	public boolean isUser1Ready() {
		return user1Ready;
	}

	// 设置用户1 准备好了
	public void setUser1Ready(boolean user1Ready) {
		this.user1Ready = user1Ready;
	}

	// 用户2 是否准备好了
	public boolean isUser2Ready() {
		return user2Ready;
	}

	// 设置用户2 准备好了
	public void setUser2Ready(boolean user2Ready) {
		this.user2Ready = user2Ready;
	}

	// 设置用户1 退出游戏
	public void setUser1Over(String userName) {
		if (userName.equals(room.getUser1Name()) && 0 == over % 10) {
			over += 1;
		}
	}

	// 设置用户2 退出游戏
	public void setUser2Over(String userName) {
		if (userName.equals(room.getUser2Name()) && over < 10) {
			over += 10;
		}
	}

	// 判断是否有人赢了
	public Color checkWin() {
		int[][][] status = new int[17][17][4];// 左，左上，上，右上
		for (int i = 2; i < BOARD_SIZE; i++) {
			if (board[1][i] != Color.NONE.getVal() && board[1][i] == board[1][i - 1]) {
				status[1][i][0] = status[1][i - 1][0] + 1;
			} else {
				status[1][i][0] = 0;
			}
			if (board[i][1] != Color.NONE.getVal() && board[i][1] == board[i - 1][1]) {
				status[i][1][2] = status[i - 1][1][2] + 1;
			} else {
				status[i][1][2] = 0;
			}
		}
		int[][] index = { { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 } };// 左，左上，上，右上
		int indexLen = index.length;
		for (int i = 2; i < BOARD_SIZE; i++) { // 行
			for (int j = 2; j < BOARD_SIZE; j++) { // 列
				for (int k = 0; k < indexLen; k++) {
					int x = i + index[k][0];
					int y = j + index[k][1];
					if (board[i][j] != Color.NONE.getVal() && board[i][j] == board[x][y]) {
						status[i][j][k] = status[x][y][k] + 1;
					} else {
						status[i][j][k] = 0;
					}
				}
			}
		}
		for (int i = 1; i < BOARD_SIZE; i++) {
			for (int j = 1; j < BOARD_SIZE; j++) {
				for (int k = 0; k < indexLen; k++) {
					if (status[i][j][k] < 5) { // 连子不到5颗
						continue;
					}
					int x = i;
					int y = j;
					for (int l = 0; l < 5; l++) {
						winPath[l][0] = x;
						winPath[l][1] = y;
						x += index[k][0];
						y += index[k][1];
					}
					return Color.getByVal(board[i][j]);
				}
			}
		}

		return Color.NONE;
	}

}
