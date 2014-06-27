package de.rwth_aachen.kbsg.dq;

public class Heuristic2 extends Heuristic1 {

	@Override
	public int evaluate(StateMachine m) {
		int value = 0;
		State s = m.getState();
		value += s.countPieces(Color.WHITE)*3;
		value -= s.countPieces(Color.BLACK)*3;
		value += (countMills(s, Color.WHITE)*6)+1;
		value -= ((countMills(s, Color.BLACK)*6)+1);
		value += countFreeNeighbors(s, Color.WHITE);
		value -= countFreeNeighbors(s, Color.BLACK);
		return value;
	}
	
	protected int countFreeNeighbors(State s, Color color){
		int freeNeighbors = 0;
		for (Point p : s.getOccupations(color)){
			for (Point n : s.neighborsOf(p)){
				if (s.getOccupancy(n) == null){
					freeNeighbors++;
				}
			}
		}
		return freeNeighbors;
	}
}
