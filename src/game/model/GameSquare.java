package game.model;

public class GameSquare {
	private int squareIndex;
	private Player player;
	private int militaries;
	private boolean isBossSquare;

	// constructor
	public GameSquare(int squareIndex, Player player, int militaries, boolean isBossSquare) {
		this.squareIndex = squareIndex;
		this.player = player;
		this.militaries = militaries;
		this.isBossSquare = isBossSquare;
	}

	public GameSquare(GameSquare gameSquare) {
		this.squareIndex = gameSquare.squareIndex;
		this.player = gameSquare.player.currentPlayer();
		this.militaries = gameSquare.militaries;
		this.isBossSquare = gameSquare.isBossSquare;
	}

	@Override
	public String toString() {
		return squareIndex + " - " + militaries;
	}

	// getter
	public int getSquareIndex() {
		return squareIndex;
	}

	public Player getPlayer() {
		return player;
	}

	public int getMilitaries() {
		return militaries;
	}

	// setter
	public void setMilitaries(int militaries) {
		this.militaries = militaries;
	}

	public boolean isBossSquare() {
		return isBossSquare;
	}

	public void setBossSquare(boolean v) {
		this.isBossSquare = v;
	}
}
