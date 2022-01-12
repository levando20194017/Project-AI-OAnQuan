package game.model;

import java.util.Iterator;

import game.lib.AI.AlphaBetaPruningComputer;
import game.lib.AI.IAutoSearching;
import game.lib.AI.ComputerDecisionResult;
import game.lib.AI.MiniMaxComputer;

public class GameBoard implements IGameModel {
	private int squareIndex = 0;
	private int level = 1; // related (easy, medium, hard, super hard)
	private GameSquare[] gameBoard; // contains the info of 12 squares
	private LinkNode[] linkedNodeSquare; // link a square with 2 adjacent squares
	private LinkNode latestLoopSquare;
	private Direction loopDirection; // is the direction the player chooses
	private Direction lastestDirectionLooped = null;
	private IAutoSearching computer;

	class LinkNode {
		LinkNode fw;
		LinkNode bw;
		GameSquare square;

		LinkNode(GameSquare s) {
			this.square = s;
		}
	}

	public GameBoard() {
		linkedNodeSquare = new LinkNode[12];
		initGameSquares();
	}

	private void initGameSquares() {
		// generate data for each square
		GameSquare BOSS_1 = new GameSquare(0, Player.PLAYER_2, 10, true); // one boss corresponds to ten militaries
		GameSquare M1_1 = new GameSquare(1, Player.PLAYER_2, 5, false);
		GameSquare M2_1 = new GameSquare(2, Player.PLAYER_2, 5, false);
		GameSquare M3_1 = new GameSquare(3, Player.PLAYER_2, 5, false);
		GameSquare M4_1 = new GameSquare(4, Player.PLAYER_2, 5, false);
		GameSquare M5_1 = new GameSquare(5, Player.PLAYER_2, 5, false);

		GameSquare BOSS_2 = new GameSquare(6, Player.PLAYER_1, 10, true); // one boss corresponds to ten militaries
		GameSquare M1_2 = new GameSquare(7, Player.PLAYER_1, 5, false);
		GameSquare M2_2 = new GameSquare(8, Player.PLAYER_1, 5, false);
		GameSquare M3_2 = new GameSquare(9, Player.PLAYER_1, 5, false);
		GameSquare M4_2 = new GameSquare(10, Player.PLAYER_1, 5, false);
		GameSquare M5_2 = new GameSquare(11, Player.PLAYER_1, 5, false);

		// create an array to store the information of all the squares
		gameBoard = new GameSquare[12];
		gameBoard[0] = BOSS_1;
		gameBoard[1] = M1_1;
		gameBoard[2] = M2_1;
		gameBoard[3] = M3_1;
		gameBoard[4] = M4_1;
		gameBoard[5] = M5_1;
		gameBoard[6] = BOSS_2;
		gameBoard[7] = M5_2;
		gameBoard[8] = M4_2;
		gameBoard[9] = M3_2;
		gameBoard[10] = M2_2;
		gameBoard[11] = M1_2;

		initLinkedGameBoard();
	}

	private void initLinkedGameBoard() {

		linkedNodeSquare[0] = new LinkNode(gameBoard[0]); // boss1
		linkedNodeSquare[1] = new LinkNode(gameBoard[1]); // m1-1
		linkedNodeSquare[2] = new LinkNode(gameBoard[2]); // m2-1
		linkedNodeSquare[3] = new LinkNode(gameBoard[3]); // m3-1
		linkedNodeSquare[4] = new LinkNode(gameBoard[4]); // m4-1
		linkedNodeSquare[5] = new LinkNode(gameBoard[5]); // m5-1
		linkedNodeSquare[6] = new LinkNode(gameBoard[6]); // boss2
		linkedNodeSquare[7] = new LinkNode(gameBoard[11]); // m1-2
		linkedNodeSquare[8] = new LinkNode(gameBoard[10]); // m2-2
		linkedNodeSquare[9] = new LinkNode(gameBoard[9]); // m3-2
		linkedNodeSquare[10] = new LinkNode(gameBoard[8]);// m4-2
		linkedNodeSquare[11] = new LinkNode(gameBoard[7]);// m5-2

		// square 0 & 6
		linkedNodeSquare[0].fw = linkedNodeSquare[1];
		linkedNodeSquare[0].bw = linkedNodeSquare[7];
		linkedNodeSquare[6].fw = linkedNodeSquare[11];
		linkedNodeSquare[6].bw = linkedNodeSquare[5];
		// square 1
		linkedNodeSquare[1].fw = linkedNodeSquare[2];
		linkedNodeSquare[1].bw = linkedNodeSquare[0];
		// square 2
		linkedNodeSquare[2].fw = linkedNodeSquare[3];
		linkedNodeSquare[2].bw = linkedNodeSquare[1];
		// square 3
		linkedNodeSquare[3].fw = linkedNodeSquare[4];
		linkedNodeSquare[3].bw = linkedNodeSquare[2];
		// square 4
		linkedNodeSquare[4].fw = linkedNodeSquare[5];
		linkedNodeSquare[4].bw = linkedNodeSquare[3];
		// square 5
		linkedNodeSquare[5].fw = linkedNodeSquare[6];
		linkedNodeSquare[5].bw = linkedNodeSquare[4];
		// square 7
		linkedNodeSquare[7].fw = linkedNodeSquare[0];
		linkedNodeSquare[7].bw = linkedNodeSquare[8];
		// square 8
		linkedNodeSquare[8].fw = linkedNodeSquare[7];
		linkedNodeSquare[8].bw = linkedNodeSquare[9];
		// square 9
		linkedNodeSquare[9].fw = linkedNodeSquare[8];
		linkedNodeSquare[9].bw = linkedNodeSquare[10];
		// square 10
		linkedNodeSquare[10].fw = linkedNodeSquare[9];
		linkedNodeSquare[10].bw = linkedNodeSquare[11];
		// square 11
		linkedNodeSquare[11].fw = linkedNodeSquare[10];
		linkedNodeSquare[11].bw = linkedNodeSquare[6];
	}

	// return player winner from militaries
	@Override
	public Player winner(Player player1, Player player2) {
		int militariesP1 = getAllMilitaryOfPlayer(player1);
		int militariesP2 = getAllMilitaryOfPlayer(player2);
		return (militariesP1 > militariesP2 ? player1 : player2);
	}

	// return boolean if squareindex have Mili
	@Override
	public boolean stillHaveMilitaryOnBoard(Player player) {
		int militaries = 0;
		for (GameSquare square : gameBoard) {
			// make sure this square isn't boss
			if (square.getSquareIndex() != 0 && square.getSquareIndex() != 6 && square.getPlayer() == player)
				militaries += square.getMilitaries();
		}
		return militaries > 0;
	}

	// trạng thái kết thúc nếu số quân ở cả 2 ô quan đều bằng 0
	@Override
	public boolean isEndGame() {
		return (linkedNodeSquare[0].square.getMilitaries() + linkedNodeSquare[6].square.getMilitaries()) == 0;
	}

	@Override
	public GameState getGameState() {
		// ván đấu chưa kết thúc
		if (!isEndGame()) {
			return GameState.NORMAL;
		}
		// trạng thái hòa
		if (isDraw())
			return GameState.DRAW;
		// trả về người chơi chiến thắng
		return (winner(Player.PLAYER_1, Player.PLAYER_2) == Player.PLAYER_1 ? GameState.PLAYER1_WIN
				: GameState.PLAYER2_WIN);
	}

	// tổng quân ở ô bên mình (ko tính ô quan) và số quân ăn được
	private int getAllMilitaryOfPlayer(Player p) {
		int totalmilitariesP = 0;
		for (GameSquare square : gameBoard) {
			// make sure this square isn't boss
			if (square.getSquareIndex() != 0 && square.getSquareIndex() != 6 && square.getPlayer() == p)
				totalmilitariesP += square.getMilitaries();
		}
		// includes reward military
		totalmilitariesP += p.militaries;
		return totalmilitariesP;
	}

	// trả về kết quả có hòa hay ko
	private boolean isDraw() {
		int totalmilitariesP1 = getAllMilitaryOfPlayer(Player.PLAYER_1);
		int totalmilitariesP2 = getAllMilitaryOfPlayer(Player.PLAYER_2);
		return totalmilitariesP1 == totalmilitariesP2;
	}

	// phần thưởng
	@Override
	public void getRewardInSquare(Player player, int index) {
		player.militaries += linkedNodeSquare[index].square.getMilitaries();
		// linkedNodeSquare[index].square.getMilitaries() số quân trông ô ở vị trí index
		// sau khi nhận được phần thưởng thì set lại số quân của ô đấy về 0
		linkedNodeSquare[index].square.setMilitaries(0);
	}

	// số quân ở vị trí index
	@Override
	public int getMilitaryAt(int index) {
		return linkedNodeSquare[index].square.getMilitaries();
	}

	// set số lượng quân của ô ở vị trí index về 0
	@Override
	public void removeMiltaryAt(int index) {
		if (index < 0 || index >= gameBoard.length)
			return;
		linkedNodeSquare[index].square.setMilitaries(0);
	}

	// thêm quân vào vị trí index với số lượng numberMiltary
	@Override
	public void addMiltaries(int index, int numberMiltary) {
		linkedNodeSquare[index].square.setMilitaries(gameBoard[index].getMilitaries() + numberMiltary);
	}

	// trả về hướng đi cuối cùng được lặp lại
	@Override
	public Direction getLatestLoopDirection() {
		return lastestDirectionLooped;
	}

	// thiết lập hướng đi ban đầu và kế tiếp
	@Override
	public void setLoopDirection(Direction direction) {
		this.loopDirection = direction;
		this.lastestDirectionLooped = direction;
	}

	@Override
	public Iterator<GameSquare> iterator() {
		return new Iterator<GameSquare>() {

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public GameSquare next() {
				GameSquare ret = null;
				if (latestLoopSquare == null) {
					latestLoopSquare = linkedNodeSquare[squareIndex];
				}
				if (squareIndex > 0 && squareIndex < 6) {
					if (loopDirection == Direction.LEFT) {
						ret = latestLoopSquare.bw.square;
						latestLoopSquare = latestLoopSquare.bw;
					}
					if (loopDirection == Direction.RIGHT) {
						ret = latestLoopSquare.fw.square;
						latestLoopSquare = latestLoopSquare.fw;
					}
				} else {
					if (loopDirection == Direction.LEFT) {
						ret = latestLoopSquare.fw.square;
						latestLoopSquare = latestLoopSquare.fw;
					}
					if (loopDirection == Direction.RIGHT) {
						ret = latestLoopSquare.bw.square;
						latestLoopSquare = latestLoopSquare.bw;

					}
				}
				if (latestLoopSquare.square.getSquareIndex() == 0 || latestLoopSquare.square.getSquareIndex() == 6) {
					// System.out.println("before " + lastestDirectionLooped + " after "
					// + lastestDirectionLooped.getOppositeDirection());
					lastestDirectionLooped = lastestDirectionLooped.getOppositeDirection();
				}

				return ret;
			}

		};

	}

	// set first index of the loop
	@Override
	public void setFirstIndexOfLoop(int index) {
		latestLoopSquare = null;
		this.squareIndex = index;
	}

	// mảng chứa các phần tử
	@Override
	public GameSquare[] getGameSquares() {
		return gameBoard;
	}

	@Override
	public boolean isValidMove(int c, int r, Player curPlayer) {
		// column row
		if (r == 0 && curPlayer == Player.PLAYER_2)// nếu row = 0, tức là hàng dân phía người chơi thứ 2
			return true;
		if (r == 1 && curPlayer == Player.PLAYER_1) // nếu row = 1, tức là hàng dân phía người chơi thứ 2
			return true;
		return false;
	}

	// thiết lập lại game mới
	@Override
	public void reAssign() {
		initGameSquares();
		Player.PLAYER_1.militaries = 0;
		Player.PLAYER_2.militaries = 0;
	}

	// thiết lập lại game mới
	public GameBoard cpy() {
		GameBoard res = new GameBoard();
		res.gameBoard[0] = new GameSquare(gameBoard[0]);
		res.gameBoard[1] = new GameSquare(gameBoard[1]);
		res.gameBoard[2] = new GameSquare(gameBoard[2]);
		res.gameBoard[3] = new GameSquare(gameBoard[3]);
		res.gameBoard[4] = new GameSquare(gameBoard[4]);
		res.gameBoard[5] = new GameSquare(gameBoard[5]);
		res.gameBoard[6] = new GameSquare(gameBoard[6]);
		res.gameBoard[7] = new GameSquare(gameBoard[7]);
		res.gameBoard[8] = new GameSquare(gameBoard[8]);
		res.gameBoard[9] = new GameSquare(gameBoard[9]);
		res.gameBoard[10] = new GameSquare(gameBoard[10]);
		res.gameBoard[11] = new GameSquare(gameBoard[11]);
		res.initLinkedGameBoard();
		res.squareIndex = this.squareIndex;
		return res;
	}

	@Override
	public String toString() {
		String s = "";
		for (GameSquare sq : gameBoard) {
			s += sq.toString() + "|";
		}
		return s;
	}

	@Override
	public ComputerDecisionResult autoSearch() {
		ComputerDecisionResult rs = null;
		if (level <= 5)
			if (computer == null)
				computer = new MiniMaxComputer();
		if (level > 5)
			if (computer == null || computer instanceof MiniMaxComputer)
				computer = new AlphaBetaPruningComputer();

		rs = computer.search(this, Player.PLAYER_1.isComputer() ? Player.PLAYER_1 : Player.PLAYER_2,
				Player.PLAYER_1.isComputer() ? Player.PLAYER_2 : Player.PLAYER_1, level);
		return rs;
	}

	// thiết lập cấp độ cho game
	@Override
	public void setGameLevel(int inputGameLevel) {
		this.level = inputGameLevel;
	}

	// nếu đến lượt chơi của người nào, mà ko còn quân xuất hiện ở phía người đấy
	// thì người đấy phải lấy quân của mình để rải ra mỗi ô
	// trường hợp nếu số quân < 5 thì trò chơi kết thúc
	@Override
	public void outMilitaries(Player curPlayer) {
		curPlayer.militaries -= 5;
		if (curPlayer == Player.PLAYER_1) {
			for (int i = 7; i < 7 + 5; i++) {
				addMiltaries(i, 1);
			}
		}

		if (curPlayer == Player.PLAYER_2) {
			for (int i = 1; i < 1 + 5; i++) {
				addMiltaries(i, 1);
			}
		}
	}

	@Override
	public void outMilitariesIf(Player curPlayer) {
		int markNumber = curPlayer.militaries;
		curPlayer.militaries = 0;
		if (curPlayer == Player.PLAYER_1) {
			Player.PLAYER_2.militaries += 5 - markNumber;
			for (int i = 7; i < 7 + 5; i++) {
				addMiltaries(i, 1);
			}
		}

		if (curPlayer == Player.PLAYER_2) {
			Player.PLAYER_1.militaries += 5 - markNumber;
			for (int i = 1; i < 1 + 5; i++) {
				addMiltaries(i, 1);
			}
		}
	}
}

// public Iterator
// public GameSquare getlatestLoopSquare()
// public ComputerDecisionResult autoSearch()
// public void setFirstIndexOfLoop(int index)