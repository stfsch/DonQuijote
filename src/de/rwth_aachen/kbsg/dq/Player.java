package de.rwth_aachen.kbsg.dq;

public abstract class Player {

	private final Color color;
	
	public Player(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public abstract State occupy(State s);
	
	public abstract State take(State s);
	
	public abstract State move(State s);
}
