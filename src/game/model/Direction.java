package game.model;

public enum Direction {
	LEFT, RIGHT;

	// set opposite direction
	public Direction getOppositeDirection() {
		if (this == Direction.LEFT)
			return Direction.RIGHT;
		return Direction.LEFT;
	}
}
