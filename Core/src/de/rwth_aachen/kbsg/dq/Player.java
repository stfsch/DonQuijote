package de.rwth_aachen.kbsg.dq;

public abstract class Player {

	protected final Color color;
	
	public Player(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public abstract State occupy(StateMachine s);
	
	public abstract State take(StateMachine s);
	
	public abstract State move(StateMachine s);
}
