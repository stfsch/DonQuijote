package de.rwth_aachen.kbsg.dq;

public class MiniMaxAgent extends Player{
	private int maxNodes;
	private Heuristic heuristic;
	public MiniMaxAgent(Color color, int maxNodes, Heuristic heuristic) {
		super(color);
		this.maxNodes = maxNodes;
		this.heuristic = heuristic;
	}

	@Override
	public State occupy(StateMachine s) {
		MiniMax minimax = new MiniMax(getColor(), maxNodes, heuristic, s);
		return minimax.minimax();
	}

	@Override
	public State take(StateMachine s) {
		MiniMax minimax = new MiniMax(getColor(), maxNodes, heuristic, s);
		return minimax.minimax();
	}

	@Override
	public State move(StateMachine s) {
		MiniMax minimax = new MiniMax(getColor(), maxNodes, heuristic, s);
		return minimax.minimax();
	}

}
