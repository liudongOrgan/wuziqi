package com.liudong.model;

import org.apache.commons.lang3.StringUtils;

public class Chessboard {
	private static final int BOARD_SIZE = 17;
	private Room room = null;
	private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
	private volatile boolean user1Ready = false;
	private volatile boolean user2Ready = false;
	private short back = 0;// 1表示 玩家1 退出 ，10表示 玩家2退出 ，11表示玩家1 和 玩家2 都退出
	private int[][] winPath = new int[5][2]; // 赢棋路径
	private boolean over = false; // 是否结束
	private Color winColor = null; // 黑色表示玩家1 白色玩家2
	private String nextChessUserName = "";
	private short status = 0; // 0初始状态 1等待玩家状态 2 游戏中状态 3游戏结束状态

	public Chessboard(Room r) {
		this.room = r;
	}

	public short getStatus() {
		return status;
	}

	/**
	 * 下子
	 * 
	 * @param c
	 */
	public synchronized boolean chess(Chess c) {
		if (true == over)
			return false;
		if (board[c.getX()][c.getY()] != Color.NONE.getVal()) {
			return false;
		}
		if (false == user1Ready || false == user2Ready) {
			return false;
		}
		if (StringUtils.isNotBlank(nextChessUserName) && !c.getOpUserName().equals(nextChessUserName)) {
			return false;
		}
		if (c.getOpUserName().equals(room.getUser1Name())) {
			board[c.getX()][c.getY()] = Color.BLACK.getVal();
			c.setColor(Color.BLACK);
			c.setNextUserName(room.getUser2Name());
			this.nextChessUserName = room.getUser2Name();
		}
		if (c.getOpUserName().equals(room.getUser2Name())) {
			board[c.getX()][c.getY()] = Color.WHITE.getVal();
			c.setColor(Color.WHITE);
			c.setNextUserName(room.getUser1Name());
			this.nextChessUserName = room.getUser1Name();
		}
		return true;
	}

	// 用户1 是否准备好
	public boolean isUser1Ready() {
		return user1Ready;
	}

	// 设置用户1 准备好了
	public synchronized void setUser1Ready(boolean user1Ready) {
		this.user1Ready = user1Ready;
		if (user1Ready == user2Ready && true == user2Ready) {
			status = 2;
		}
	}

	// 用户2 是否准备好了
	public boolean isUser2Ready() {
		return user2Ready;
	}

	// 设置用户2 准备好了
	public synchronized void setUser2Ready(boolean user2Ready) {
		this.user2Ready = user2Ready;
		if (user1Ready == user2Ready && true == user2Ready) {
			status = 2;
		}
	}

	// 设置用户1 退出游戏
	public void setUser1Over(String userName) {
		if (userName.equals(room.getUser1Name()) && 0 == back % 10) {
			back += 1;
		}
		if (back > 10)
			over = true;
	}

	// 设置用户2 退出游戏
	public void setUser2Over(String userName) {
		if (userName.equals(room.getUser2Name()) && back < 10) {
			back += 10;
		}
		if (back > 10)
			over = true;
	}

	// 判断是否有人赢了
	public Color checkWin() {
		if (true == over)
			return null;
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
					if (board[i][j] != Color.NONE.getVal()) {
						status[i][j][k] = 1;
					}
					if (board[i][j] != Color.NONE.getVal() && board[i][j] == board[x][y]) {
						status[i][j][k] = status[x][y][k] + 1;
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
					over = true;
					this.status = 3;
					winColor = Color.getByVal(board[i][j]);
					return winColor;
				}
			}
		}

		return Color.NONE;
	}

	public boolean isOver() {
		return over;
	}

	public Result getResult() {
		Result res = new Result();
		res.setWinPath(winPath);
		res.setWinUser(winColor.getVal() == Color.BLACK.getVal() ? room.getUser1Name() : room.getUser2Name());
		return res;
	}

	public String getNextChessUserName() {
		return nextChessUserName;
	}

}
