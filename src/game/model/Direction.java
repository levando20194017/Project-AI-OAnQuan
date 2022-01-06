package game.model;

public enum Direction {
	LEFT, RIGHT;

	// set opposite direction
	public Direction setOppositeDirection() {
		if (this == Direction.LEFT)
			return Direction.RIGHT;
		return Direction.LEFT;
	}
}
