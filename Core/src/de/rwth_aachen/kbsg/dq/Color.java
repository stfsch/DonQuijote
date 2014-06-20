package de.rwth_aachen.kbsg.dq;

public enum Color {
	BLACK, WHITE;
	
	public Color opponent() {
		if (this == BLACK) {
			return WHITE;
		} else {
			return BLACK;
		}
	}
}
