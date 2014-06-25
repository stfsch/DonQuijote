package de.rwth_aachen.kbsg.dq;

public enum Phase {
	OCCUPY, MOVE, TAKE, WIN, DRAW;
	
	public boolean isTerminal() {
		return this == WIN || this == DRAW;
	}
}
