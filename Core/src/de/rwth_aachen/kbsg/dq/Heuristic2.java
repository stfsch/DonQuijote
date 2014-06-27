package de.rwth_aachen.kbsg.dq;

/**
 * evaluates states by counting pieces, mills and direct free neighbors
 */
public class Heuristic2 extends Heuristic {

	@Override
	public int evaluate(StateMachine m) {
		int value = 0;
		State s = m.getState();
		value += s.countPieces(Color.WHITE)*3;
		value -= s.countPieces(Color.BLACK)*3;
		value += ((countMills(s, Color.WHITE)*6)+1);
		value -= ((countMills(s, Color.BLACK)*6)+1);
		value += countFreeNeighbors(s, Color.WHITE);
		value -= countFreeNeighbors(s, Color.BLACK);
		return value;
	}
}
