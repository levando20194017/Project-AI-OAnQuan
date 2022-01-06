package game.model;

public class Player {
	public static final Player PLAYER_1 = new Player("Player 1", 0);
	public static final Player PLAYER_2 = new Player("Player 2", 0);

	private String name;
	public int militaries; // the mark of player
	private boolean isComputer;

	public Player(String name, int militaries) {
		super();
		this.name = name;
		this.militaries = militaries;
	}

	public Player cpy() {
		Player rs = new Player(this.name, this.militaries);
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
		return "(" + getName() + " " + militaries + ")";
	}

	public void setName(String string) {
		this.name = string;
	}
}
