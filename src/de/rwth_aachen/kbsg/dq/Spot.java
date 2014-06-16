package de.rwth_aachen.kbsg.dq;

public enum Spot {
	EMPTY, BLACK, WHITE;
	
	public char spotToChar(){
		switch(this){
			case EMPTY:
				return '+';
			case BLACK:
				return 'X';
			case WHITE:
				return '0';
				default: return '-';
		}
	}
}
