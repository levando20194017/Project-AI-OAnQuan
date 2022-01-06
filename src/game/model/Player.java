package game.model;

public class Player {
	public static final Player PLAYER_1 = new Player("Player 1", 0);
	public static final Player PLAYER_2 = new Player("Player 2", 0);
	
	public int miltaries;
	private String name;
	private boolean isComputer;

	public Player(String name, int miltaries) {
		super();
		this.name = name;
		this.miltaries = miltaries;
	}
	public Player cpy() {
		Player rs = new Player(this.name, this.miltaries);
		rs.isComputer = this.isComputer;
		return rs;
	}

	public String getName() {
		return name;
	}

	public boolean isComputer() {
		return isComputer;
	}
	public void setComputer(boolean b) {
		this.isComputer = b;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Player))
			return false;
		Player o = (Player) obj;
		return this.name.equalsIgnoreCase(o.name);
	}

	@Override
	public String toString() {
		return "(" + getName() + " " + miltaries + ")";
	}

	public void setName(String string) {
		this.name = string;
	}
}

