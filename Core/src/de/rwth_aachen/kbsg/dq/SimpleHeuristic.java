package de.rwth_aachen.kbsg.dq;

public class SimpleHeuristic implements Heuristic{

	@Override
	public int evaluate(State s, Color c) {
		int value = 0;
		value += s.countPieces(c);
		value -= s.countPieces(c.opponent());
		value += countMills(s, c)*3;
		value -= countMills(s, c.opponent())*3;
		return value;
	}

	@Override
	public int countMills(State s, Color color) {
		{
			int mills = 0;
			for (int [][] line : lines){
				int k = 0;
				for(int[] point : line){
					if(!s.isOccupiedBy(new Point(point[0], point[1]), color)){
						k = 1;
					}
				}
				if(k == 0){
					mills ++;
				}
			}
			return mills;
		}
	}
}
