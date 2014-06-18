package de.rwth_aachen.kbsg.dq;

public class Human extends Player {
	public State placeNextMan (int pSpot[], State currentState){
		State newState=new State();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				newState.occupancy[i][j] = currentState.occupancy[i][j];
			}
		}
		newState.occupancy[pSpot[0]][pSpot[1]]=colour;
		return newState;
	}
	public State removeMan (int pSpot[],State currentState){
		State newState=new State();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				newState.occupancy[i][j] = currentState.occupancy[i][j];
			}
		}
		newState.occupancy[pSpot[0]][pSpot[1]]=Spot.EMPTY;
		return newState;
	}
	public State nextMove( int oldSpot[], int newSpot[],State currentState) {
		State newState = new State();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				newState.occupancy[i][j] = currentState.occupancy[i][j];
			}
		}
		Spot[][] occupancy = newState.getOccupancy();
		occupancy[oldSpot[0]][oldSpot[1]] = Spot.EMPTY;
		occupancy[newSpot[0]][newSpot[1]] = colour;
		return newState;
	}

}
