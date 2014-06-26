package de.rwth_aachen.kbsg.dq;

public class MiniMaxAgent extends Player{
	public MiniMax minimax;
	public MiniMaxAgent(Color color, int depth, Heuristic heuristic) {
		super(color);
		minimax = new MiniMax(color, depth, heuristic);
	}

	@Override
	public State occupy(StateMachine s) {
		return minimax.minimax(s);
	}

	@Override
	public State take(StateMachine s) {
		return minimax.minimax(s);
	}

	@Override
	public State move(StateMachine s) {
		return minimax.minimax(s);
	}

}
