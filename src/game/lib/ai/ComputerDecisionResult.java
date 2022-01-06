package game.lib.ai;

import game.model.Direction;

public class ComputerDecisionResult {
    public ComputerDecisionResult(int index, Direction d) {
        this.index = index;
        this.dir = d;
    }

    public int index;
    public Direction dir;

    @Override
    public String toString() {
        return "should move at: " + index + " , dir: " + dir;
    }
}
