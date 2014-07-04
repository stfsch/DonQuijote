package de.rwth_aachen.kbsg.dq;

import java.util.Iterator;

/**
 * Heuristics 2 and 5 beat all others, Heuristics 1, 3 and 4 are equal.
 */
public abstract class Heuristic {
	public abstract int evaluate(StateMachine stateMachine);
	
	protected int countMills(State s, Color color) {
		int mills = 0;
		for (Iterable<Point> line : s.linesOfField()) {
			if (s.areAllOccupiedBy(line, color)) {
				++mills;
			}
		}
		return mills;
	}
	
	protected int countFreeNeighbors(State s, Color color){
		int freeNeighbors = 0;
		for (Point p : s.getOccupations(color)){
			Iterator<Point> i = s.neighborsOf(p).iterator();
			while (!i.hasNext()) {
				Point n = i.next();
				if (s.getOccupancy(n) == null){
					freeNeighbors++;
				}
			}
		}
		return freeNeighbors;
	}
	/**
	 * counts the mills prevented by the player of the given color
	 * @param s the state of the board
	 * @param c the color of the preventing player
	 * @return number of prevented mills
	 */
	protected int countPreventedMills(State s, Color c){
		int preventedMills = 0;
		for (Iterable<Point> line : s.linesOfField()) {
			int own = 0;
			int opponent = 0;
			for (Point p : line) {
				if(s.isOccupiedBy(p, c)){
					own++;
				}
				if(s.isOccupiedBy(p, c.opponent())){
					opponent++;
				}
			}
			if(opponent == 2 && own == 1){
				preventedMills++;
			}
		}
		return preventedMills;
	}
}
