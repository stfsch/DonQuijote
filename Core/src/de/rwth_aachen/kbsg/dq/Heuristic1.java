package de.rwth_aachen.kbsg.dq;

/**
 * evaluates states by counting pieces and mills
 */
public class Heuristic1 extends Heuristic {

	@Override
	public int evaluate(StateMachine m) {
		int value = 0;
		State s = m.getState();
		value += s.countPieces(Color.WHITE);
		value -= s.countPieces(Color.BLACK);
		value += (countMills(s, Color.WHITE)*3) +1;
		value -= ((countMills(s, Color.BLACK)*3)+1);
		return value;
	}
}
