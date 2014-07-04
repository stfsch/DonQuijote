package de.rwth_aachen.kbsg.dq;

/**
 * evaluates states by counting mills, pieces and prevented mills
 *
 */
public class Heuristic5 extends Heuristic{

	@Override
	public int evaluate(StateMachine stateMachine) {
		int value = 0;
		State s = stateMachine.getState();
		value += s.countPieces(Color.WHITE)*6;
		value -= s.countPieces(Color.BLACK)*6;
		value += (countMills(s, Color.WHITE)*3);
		value -= (countMills(s, Color.BLACK)*3);
		value += countPreventedMills(s,Color.WHITE);
		value -= countPreventedMills(s, Color.BLACK);
		return value;
	}
}
