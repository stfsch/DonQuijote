package de.rwth_aachen.kbsg.dq;

/**
 * evaluates states by counting the own and the opponement's neighbors
 */
public class Heuristic3 extends Heuristic {
	public int evaluate (StateMachine m){
		int value = 0;
		State s = m.getState();
		for ( Point p : s.pointsOfField()){
			if (s.isOccupiedBy(p, Color.WHITE)){
				value += 1;
				for (Point q : m.getState().neighborsOf(p)){
					if (s.getOccupancy(q) == Color.WHITE){
						value +=2;
					}
					if (s.getOccupancy(q) == Color.BLACK){
						value -=2;
					}
					if (s.getOccupancy(q) == null){
						value += 1;
					}
				}
			}
			if (s.isOccupiedBy(p, Color.BLACK)){
				value -= 1;
				for (Point q : s.neighborsOf(p)){
					if (s.getOccupancy(q) == Color.WHITE){
						value -=2;
					}
					if (s.getOccupancy(q) == Color.BLACK){
						value +=2;
					}
					if (s.getOccupancy(q) == null){
						value -= 1;
					}
				}
			}
		}
		return value;
	}
}

