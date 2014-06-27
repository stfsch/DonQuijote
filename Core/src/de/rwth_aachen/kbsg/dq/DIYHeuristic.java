package de.rwth_aachen.kbsg.dq;

public class DIYHeuristic extends Heuristic2{
	int factorMills = 0;
	int factorPieces = 0;
	int factorMobility = 0;
	public DIYHeuristic(int mills, int pieces, int mobility){
		factorMills = mills;
		factorPieces = pieces;
		factorMobility = mobility;
	}
	
	@Override
	public int evaluate(StateMachine stateMachine) {
		int value = 0;
		State s = stateMachine.getState();
		value += s.countPieces(Color.WHITE)*factorPieces;
		value -= s.countPieces(Color.BLACK)*factorPieces;
		value += countMills(s, Color.WHITE)*factorMills;
		value -= countMills(s, Color.BLACK)*factorMills;
		value += countFreeNeighbors(s,Color.WHITE)*factorMobility;
		value -= countFreeNeighbors(s, Color.BLACK)*factorMobility;
		return value;
	}
}
