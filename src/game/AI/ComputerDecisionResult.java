package game.AI;

import game.model.Direction;

public class ComputerDecisionResult {
    public int squareIndex;
    public Direction dir;

    public ComputerDecisionResult(int squareIndex, Direction dir) {
        this.squareIndex = squareIndex;
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "should move at: " + squareIndex + " , dir: " + dir;
    }
}