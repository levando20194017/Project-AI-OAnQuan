package game.model;

public class GameSquare {

    private int index;
    private Player player;
    private int miltaries;
    private boolean isBossSquare;

    public GameSquare(int index, Player player, int miltaries, boolean isBossSquare) {
        this.index = index;
        this.player = player;
        this.miltaries = miltaries;
        this.isBossSquare = isBossSquare;
    }

    public GameSquare(GameSquare gameSquare) {
        this.index = gameSquare.index;
        this.player = gameSquare.player.currentPlayer();
        this.miltaries = gameSquare.miltaries;
        this.isBossSquare = gameSquare.isBossSquare;
    }

    public int getIndex() {
        return index;
    }

    public Player getPlayer() {
        return player;
    }

    public int getMilitaries() {
        return miltaries;
    }

    public void setMiltaries(int miltaries) {
        this.miltaries = miltaries;
    }

    public boolean isBossSquare() {
        return isBossSquare;
    }

    public void setBossSquare(boolean v) {
        this.isBossSquare = v;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return index + " - " + miltaries;
    }
}
