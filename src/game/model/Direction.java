package game.model;

public enum Direction {
	LEFT, RIGHT;

	// opposite direction
	public Direction getOppositeDirection() {
		if (this == Direction.LEFT)
			return Direction.RIGHT;
		return Direction.LEFT;
	}

	// get direction
	public String getDirection() {
		if (this == Direction.LEFT)
			return "LEFT";
		return "RIGHT";
	}
}
