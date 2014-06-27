package de.rwth_aachen.kbsg.dq;

/**
 * evaluates states by counting direct free neighbors, mills, prevented mills and pieces (importance of each of these can be regulated by the factors given to the constructor)
 */
public class DIYHeuristic extends Heuristic{
	int factorMills = 0;
	int factorPieces = 0;
	int factorMobility = 0;
	int factorPreventedMills = 0;
	public DIYHeuristic(int mills, int pieces, int mobility, int preventedMills){
		factorMills = mills;
		factorPieces = pieces;
		factorMobility = mobility;
		factorPreventedMills = preventedMills;
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
		value += countPreventedMills(s,Color.WHITE)*factorPreventedMills;
		value -= countPreventedMills(s, Color.BLACK)*factorPreventedMills;
		return value;
	}
}
