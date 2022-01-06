package game.lib.AI;

import game.model.Direction;

public class ComputerDecisionResult {
    public int squareIndex;
    public Direction dir;

    public ComputerDecisionResult(int squareIndex, Direction d) {
        this.squareIndex = squareIndex;
        this.dir = d;
    }

    @Override
    public String toString() {
        return "should move at: " + squareIndex + " , dir: " + dir;
    }
}
