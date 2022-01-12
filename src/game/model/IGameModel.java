package game.model;

import game.lib.AI.ComputerDecisionResult;

public interface IGameModel extends Iterable<GameSquare> {
	// Check if the player has it's military on the board
	boolean stillHaveMilitaryOnBoard(Player player);

	// NORMAL , PLAYER1_WIN , PLAYER2_WIN , DRAW
	GameState getGameState();

	// when end game, compare their militaries to select the winner
	Player winner(Player player1, Player player2);

	// Check if the number of militaries on boss square is 0
	boolean isEndGame();

	//
	void getRewardInSquare(Player player, int index);

	int getMilitaryAt(int index);

	void removeMiltaryAt(int index);

	default int getAndRemoveMilitaryAt(int index) {
		int miltaries = getMilitaryAt(index);
		removeMiltaryAt(index);
		return miltaries;
	}

	Direction getLatestLoopDirection();

	void addMiltaries(int index, int numberMiltary);

	void setLoopDirection(Direction direction);

	void setIndexLoop(int index);

	GameSquare[] getGameSquares();

	boolean isValidMove(int c, int r, Player curPlayer);

	void reAssign();

	ComputerDecisionResult autoSearch();

	void setGameLevel(int inputGameLevel);

	void outMilitaries(Player curPlayer);

	void outMilitariesIf(Player curPlayer);

}
