package de.rwth_aachen.kbsg.dq;
/**
 * Heuristics 2 and 5 beat all others, Heuristics 1, 3 and 4 are equal.
 */
public abstract class Heuristic {
	final int [][][] lines = new int [][][] {{{0, 0},{0, 1},{0, 2}}, {{0, 2}, {0, 3}, {0, 4}}, {{0, 4}, {0, 5}, {0, 6}}, {{0, 6}, {0, 7}, {0, 0}}, {{1, 0}, {1, 1}, {1, 2}}, {{1, 2}, {1, 3}, {1, 4}}, {{1, 4}, {1, 5}, {1, 6}}, {{1, 6}, {1, 7}, {1, 0}}, {{2, 0}, {2, 1}, {2, 2}}, {{2, 2}, {2, 3}, {2, 4}}, {{2, 4}, {2, 5}, {2, 6}}, {{2, 6}, {2, 7}, {2, 0}}, {{0, 1}, {1, 1}, {2, 1}}, {{0, 3}, {1, 3}, {2, 3}}, {{0, 5}, {1, 5}, {2, 5}}, {{0, 7}, {1, 7}, {2, 7}}};
	abstract int evaluate(StateMachine stateMachine);
	protected int countMills(State s, Color color) {
		int mills = 0;
		for (int [][] line : lines){
			int k = 0;
			if(mills < 4){
				for(int[] point : line){
					if(!s.isOccupiedBy(new Point(point[0], point[1]), color)){
						k = 1;
					}
				}
				if(k == 0){
					mills ++;
				}
			}
			else {
				return mills;
			}
		}
		return mills;
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
	/**
	 * counts the mills prevented by the player of the given color
	 * @param s the state of the board
	 * @param c the color of the preventing player
	 * @return number of prevented mills
	 */
	protected int countPreventedMills(State s, Color c){
		int preventedMills = 0;
		for( int [][] line : lines){
			int own = 0;
			int opponement = 0;
			for (int[] point : line){
				if(s.isOccupiedBy(new Point (point[0], point[1]), c)){
					own++;
				}
				if(s.isOccupiedBy(new Point (point[0], point[1]), c.opponent())){
					opponement++;
				}
			}
			if(opponement == 2 && own == 1){
				preventedMills++;
			}
		}
		return preventedMills;
	}
}
