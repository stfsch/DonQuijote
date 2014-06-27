package de.rwth_aachen.kbsg.dq;

public class Heuristic1 extends Heuristic {

	@Override
	public int evaluate(StateMachine m) {
		int value = 0;
		State s = m.getState();
		value += s.countPieces(Color.WHITE);
		value -= s.countPieces(Color.BLACK);
		value += (countMills(s, Color.WHITE)*3) +1;
		value -= (countMills(s, Color.BLACK)*3)+1;
		return value;
	}

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
}
