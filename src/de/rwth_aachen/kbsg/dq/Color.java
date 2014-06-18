package de.rwth_aachen.kbsg.dq;

public enum Color {
	BLACK, WHITE;
	
	public Color opponentOf() {
		if (this == BLACK) {
			return WHITE;
		} else {
			return BLACK;
		}
	}
}
