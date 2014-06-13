package de.rwth_aachen.kbsg.dq;

public class Player {

	protected Spot colour;

	public State nextMove( int manRahmen, int manPosition, int wunschRahmen,int wunschPosition, State currentState) {
		State newState = new State();
		newState = currentState;
		Spot[][] occupancy = newState.getOccupancy();
		occupancy[manRahmen][manPosition] = Spot.EMPTY;
		occupancy[wunschRahmen][wunschPosition] = colour;
		return newState;
	}

}
