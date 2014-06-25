package de.rwth_aachen.kbsg.dq;

public class HeuristicAgent extends Player{
	final int [][][] lines;
	public HeuristicAgent (Color color){
		super (color);
		lines = new int [][][] {{{0, 0},{0, 1},{0, 2}}, {{0, 2}, {0, 3}, {0, 4}}, {{0, 4}, {0, 5}, {0, 6}}, {{0, 6}, {0, 7}, {0, 0}}, {{1, 0}, {1, 1}, {1, 2}}, {{1, 2}, {1, 3}, {1, 4}}, {{1, 4}, {1, 5}, {1, 6}}, {{1, 6}, {1, 7}, {1, 0}}, {{2, 0}, {2, 1}, {2, 2}}, {{2, 2}, {2, 3}, {2, 4}}, {{2, 4}, {2, 5}, {2, 6}}, {{2, 6}, {2, 7}, {2, 0}}, {{0, 1}, {1, 1}, {2, 1}}, {{0, 3}, {1, 3}, {2, 3}}, {{0, 5}, {1, 5}, {2, 5}}, {{0, 7}, {1, 7}, {2, 7}}};
	}

	@Override
	public State occupy(State s) {
		int max = 0;
		State bestMove = null;
		for (State possibleNextState : s.getPossibleNextStates(Phase.OCCUPY, getColor())){
			if(max == 0){
				max = evaluate(possibleNextState);
				bestMove = possibleNextState;
			}
			if(evaluate(possibleNextState) >= max){
				max = evaluate(possibleNextState);
				bestMove = possibleNextState;
			}
		}
		return bestMove;
	}

	@Override
	public State take(State s) {
		int max = 0;
		State bestMove = null;
		for (State possibleNextState : s.getPossibleNextStates(Phase.TAKE, getColor())){
			if(max == 0){
				max = evaluate(possibleNextState);
				bestMove = possibleNextState;
			}
			if(evaluate(possibleNextState) >= max){
				max = evaluate(possibleNextState);
				bestMove = possibleNextState;
			}
		}
		return bestMove;
	}

	@Override
	public State move(State s) {
		int max = 0;
		State bestMove = null;
		for (State possibleNextState : s.getPossibleNextStates(Phase.MOVE, getColor())){
			if(max == 0){
				max = evaluate(possibleNextState);
				bestMove = possibleNextState;
			}
			if(evaluate(possibleNextState) >= max){
				max = evaluate(possibleNextState);
				bestMove = possibleNextState;
			}
		}
		return bestMove;
	}
	
	/**
	 * evaluates how good the given state is for the player
	 * @param s the state
	 * @return returns an integer. the higher the value of the integer, the better is the state for the player.
	 */
	private int evaluate (State s){
		int value = 0;
		value = s.countPieces(getColor());
		value += countMills(s, getColor())*3;
		value -= s.countPieces(getColor().opponent());
		value -= countMills(s, getColor().opponent())*3;
		return value;
	}
	
	/**
	 * counts the mills of one color in a state
	 * @param s the state
	 * @param color the color of the mills you want to count
	 * @return returns the amount of mills of the given color in the given state
	 */
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
