package game.model;

import game.lib.AI.ComputerDecisionResult;

public interface IGameModel extends Iterable<GameSquare> {
	// ?????????
	boolean stillHasOnBoardMilitary(Player player);

	// NORMAL , PLAYER1_WIN , PLAYER2_WIN , DRAW
	GameState getGameState();

	Player winner(Player player1, Player player2);

	boolean isEndGame();

	void getRewardInSquare(Player player, int index);

	int getMilitaryAt(int index);

	void removeMiltaryAt(int index);

	default int getAndRemoveMilitaryAt(int index) {
		int miltaries = getMilitaryAt(index);
		removeMiltaryAt(index);
		return miltaries;
	}

	GameSquare getLastestLoopedSquare();

	void addMiltaries(int index, int numberMiltary);

	Direction getLastestLoopedDirection();

	void setLoopDirection(Direction direction);

	void setIndexLoop(int index);

	GameSquare[] getGameSquares();

	boolean isValidMove(int c, int r, Player curPlayer);

	void reAssign();

	ComputerDecisionResult autoSearch();

	void setGameLevel(int inputGameLevel);

	void outMilitaries(Player curPlayer);

}
