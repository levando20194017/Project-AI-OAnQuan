package game.model;

import game.lib.AI.ComputerDecisionResult;

public interface IGameModel extends Iterable<GameSquare> {
	GameSquare[] getGameSquares();

	ComputerDecisionResult autoSearch();

	// when end game, compare their militaries to select the winner
	Player winner(Player player1, Player player2);

	// Check if the player has it's military on the board
	boolean stillHaveMilitaryOnBoard(Player player);

	// Check if the number of militaries on boss square is 0
	boolean isEndGame();

	// NORMAL , PLAYER1_WIN , PLAYER2_WIN , DRAW
	GameState getGameState();

	// get edible militaries
	void getRewardInSquare(Player player, int index);

	// basic action
	void addMilitaries(int index, int numberMilitaries);

	int getMilitariesAt(int index);

	void removeMilitariesAt(int index);

	default int getAndRemoveMilitariesAt(int index) {
		int militaries = getMilitariesAt(index);
		removeMilitariesAt(index);
		return militaries;
	}

	// depends on the last square is the boss or civilian
	Direction getLatestLoopDirection();

	// left or right
	void setLoopDirection(Direction direction);

	void setFirstIndexOfLoop(int index);

	boolean isValidMove(int c, int r, Player curPlayer);

	// replay game
	void reassign();

	void setGameLevel(int inputGameLevel);

	// spread militaries when the player does have militaries on board
	void outMilitaries(Player curPlayer);

	// ...
	void outMilitariesIf(Player curPlayer);
}
