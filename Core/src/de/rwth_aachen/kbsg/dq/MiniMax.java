package de.rwth_aachen.kbsg.dq;

public class MiniMax {
	public int depth;
	public Phase phase;
	public Color color;
	public State state;
	final int [][][] lines;
	public MiniMax(Color color, int depth, State state, Phase phase) {
		lines = new int [][][] {{{0, 0},{0, 1},{0, 2}}, {{0, 2}, {0, 3}, {0, 4}}, {{0, 4}, {0, 5}, {0, 6}}, {{0, 6}, {0, 7}, {0, 0}}, {{1, 0}, {1, 1}, {1, 2}}, {{1, 2}, {1, 3}, {1, 4}}, {{1, 4}, {1, 5}, {1, 6}}, {{1, 6}, {1, 7}, {1, 0}}, {{2, 0}, {2, 1}, {2, 2}}, {{2, 2}, {2, 3}, {2, 4}}, {{2, 4}, {2, 5}, {2, 6}}, {{2, 6}, {2, 7}, {2, 0}}, {{0, 1}, {1, 1}, {2, 1}}, {{0, 3}, {1, 3}, {2, 3}}, {{0, 5}, {1, 5}, {2, 5}}, {{0, 7}, {1, 7}, {2, 7}}};
	}
	
	public max(State state, int depth, Phase phase, Color color){
		
	}
	
	public  min(State state, int depth, Phase phase, Color color){

	}
	private int evaluate (State state){
		int value = 0;
		value += state.countPieces(color);
		value -= state.countPieces(color.opponent());
		value += countMills(state, color)*3;
		value -= countMills(state, color.opponent())*3;
		return value;
	}
	
	private int countMills (State s, Color color){
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
