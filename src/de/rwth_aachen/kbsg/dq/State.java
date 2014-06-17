package de.rwth_aachen.kbsg.dq;

public class State {

	public Spot occupancy[][];
	public State() {
		occupancy = new Spot[3][8];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				occupancy[i][j] = Spot.EMPTY;
			}
		}
	}

	public boolean isMühle(int rahmen, int position, Spot colour) {
		if (position % 2 == 1 && position != 7) {
			if (occupancy[rahmen][position + 1] == colour
					&& occupancy[rahmen][position - 1] == colour) {
				return true;
			} else if (occupancy[0][position] == colour
					&& occupancy[1][position] == colour
					&& occupancy[2][position] == colour) {
				return true;
			} else {
				return false;
			}
		} else if (position == 7) {
			if (occupancy[rahmen][0] == colour && occupancy[rahmen][6] == colour) {
				return true;
			} else if (occupancy[0][position] == colour
					&& occupancy[1][position] == colour
					&& occupancy[2][position] == colour) {
				return true;
			} else {
				return false;
			}
		} else if (position % 2 == 0) {
			if (isMühle(rahmen, position + 1, colour) == true) {
				return true;
			} else {
				return false;
			}
		} else if (position != 0) {
			if (isMühle(rahmen, position - 1, colour) == true) {
				return true;
			} else {
				return false;
			}
		} else if (position == 0) {
			if (isMühle(rahmen, 7, colour) == true) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public Spot[][] getOccupancy() {
		return occupancy;
	}
	
	public boolean isSameAs(State state){
		Spot [][]occupancy2=state.getOccupancy();
		int k=0;
		for(int i=0;i<3;i++){
			for(int j=0;j<8;j++){
				if(occupancy[i][j]!= occupancy2[i][j]){
					k=1;
				}
			}
		}
		if(k==1){
			return false;
		}
		else{
			return true;
		}
	}
	
	public State[]getPossibleNextStates(Spot colour, String phase) {
		State[] possibleNextStates= new State[54];
		int p=0;
		for(int i=0;i<54;i++){
			possibleNextStates[i]= new State();
		}
		switch (phase) {
		case "SETZEN":
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 8; j++) {
					if(occupancy[i][j] == Spot.EMPTY && isMühle(i, j, colour) == false) {
						State state = new State();
						for(int k = 0; k < 3; k++) {
							for (int l = 0; l < 8; l++) {
								state.occupancy[k][l] = this.occupancy[k][l];
							}
						}
						possibleNextStates[p]=state;
						possibleNextStates[p].occupancy[i][j]=colour;
						p++;
					}
				}
			}
		break;
		case "ZIEHEN":
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 8; j++) {
					if (occupancy[i][j] == colour && j != 7) {
						if (occupancy[i][j + 1] == Spot.EMPTY) {
							State state = new State();
							state.occupancy = getOccupancy();
							state.occupancy[i][j + 1] = colour;
							state.occupancy[i][j] = Spot.EMPTY;
							possibleNextStates[p]=state;
							p++;
						}
					} else if (occupancy[i][j] == colour && j == 7) {
						if (occupancy[i][0] == Spot.EMPTY) {
							State state = new State();
							state.occupancy = getOccupancy();
							state.occupancy[i][j+1] = colour;
							state.occupancy[i][j] = Spot.EMPTY;
							possibleNextStates[p]=state;
							p++;
						}
					} else if (occupancy[i][j] == colour && j != 0) {
						if (occupancy[i][j - 1] == Spot.EMPTY) {
							State state = new State();
							state.occupancy = getOccupancy();
							state.occupancy[i][j-1] = colour;
							state.occupancy[i][j] = Spot.EMPTY;
							possibleNextStates[p]=state;
							p++;
						}
					} else if (occupancy[i][j] == colour && j == 0) {
						if (occupancy[i][7] == Spot.EMPTY) {
							State state = new State();
							state.occupancy = getOccupancy();
							state.occupancy[i][7] = colour;
							state.occupancy[i][j] = Spot.EMPTY;
							possibleNextStates[p]=state;
							p++;
						}
					}
					if (j % 2 == 1) {
						if (occupancy[i][j] == colour && i != 2) {
							if (occupancy[i + 1][j] == Spot.EMPTY) {
								State state = new State();
								state.occupancy = getOccupancy();
								state.occupancy[i + 1][j] = colour;
								state.occupancy[i][j] = Spot.EMPTY;
								possibleNextStates[p]=state;
								p++;
							}
						}
					} else if (occupancy[i][j] == colour && i != 0) {
						if (occupancy[i - 1][j] == Spot.EMPTY) {
							State state = new State();
							state.occupancy = getOccupancy();
							state.occupancy[i-1][j]=colour;
							state.occupancy[i][j] = Spot.EMPTY;
							possibleNextStates[p]=state;
							p++;
						}
					}
				}
			}
		break;
		case "SPRINGENBOTH":
		case "SPRINGENWHITE":
		case "SPRINGENBLACK":
			if (phase == "SPRINGENWHITE" && colour == Spot.BLACK) {
				phase = "ZIEHEN";
				getPossibleNextStates(colour, phase);
				phase = "SPRINGENWHITE";
			} 
			else if (phase == "SPRINGENBLACK" && colour == Spot.WHITE) {
				phase = "ZIEHEN";
				getPossibleNextStates(colour, phase);
				phase = "SPRINGENBLACK";
			}
			else {
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 8; j++) {
						if (occupancy[i][j] == colour) {
							for (int k = 0; k < 3; k++) {
								for (int l = 0; l < 8; l++) {
									if (occupancy[k][l] == Spot.EMPTY) {
										State state = new State();
										state.occupancy = getOccupancy();
										state.occupancy[k][l] = colour;
										state.occupancy[i][j] = Spot.EMPTY;
										possibleNextStates[p]=state;
										p++;
									}
								}
							}
						}
					}
				}
			}
		break;	
		case "WEGNEHMEN":
			if (colour == Spot.WHITE) {
				colour = Spot.BLACK;
			}
			else {
				colour = Spot.WHITE;
			}
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 8; j++) {
					if (occupancy[i][j] == colour) {
						if (isMühle(i, j, colour) == false) {
							State state = new State();
							state.occupancy = getOccupancy();
							state.occupancy[i][j] = Spot.EMPTY;
							possibleNextStates[p]=state;
							p++;
						}
					}
				}
			}
		}

		return possibleNextStates;
	}

	public int countMen(String colour) {
		int men = 0;
		switch (colour) {
		case "":
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 8; j++) {
					if (occupancy[i][j] == Spot.EMPTY) {
					} else if (occupancy[i][j] != Spot.EMPTY) {
						men++;
					}
				}
			}
			return men;
		case "white":
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 8; j++) {
					if (occupancy[i][j] == Spot.WHITE) {
						men++;
					}
				}
			}
			return men;
		case "schwarz":
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 8; j++) {
					if (occupancy[i][j] == Spot.BLACK) {
						men++;
					}
				}
			}
			return men;
		default:
			return men;
		}
	}
}