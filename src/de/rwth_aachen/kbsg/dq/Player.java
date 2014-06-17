package de.rwth_aachen.kbsg.dq;

public class Player {

	protected Spot colour;
	
	public State placeNextMan (int wunschRahmen, int wunschPosition, State currentState){
		State newState=new State();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				newState.occupancy[i][j] = currentState.occupancy[i][j];
			}
		}
		newState.occupancy[wunschRahmen][wunschPosition]=colour;
		return newState;
	}
	public State removeMan (int manRahmen, int manPosition,State currentState){
		State newState=new State();
		newState.occupancy=currentState.occupancy.clone();
		newState.occupancy[manRahmen][manPosition]=Spot.EMPTY;
		return newState;
	}
	public State nextMove(int manRahmen, int manPosition, int wunschRahmen, int wunschPosition,State currentState) {
		State newState = new State();
		newState = currentState;
		Spot[][] occupancy = newState.getOccupancy();
		occupancy[manRahmen][manPosition] = Spot.EMPTY;
		occupancy[wunschRahmen][wunschPosition] = colour;
		return newState;
	}

}
