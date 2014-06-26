package de.rwth_aachen.kbsg.dq;

public class MiniMax {
	public Color color;
	int depth;
	public Heuristic h;
	public MiniMax(Color color, int depth, Heuristic heuristic) {
		this.color = color;
		this.depth = depth;
		this.h = heuristic;
	}
	
	public State minimax (StateMachine stateMachine){
		State bestMove = null;
		int bestValue = 0;
		for (StateMachine child : stateMachine.getPossibleNextStateMachines().values()){
			int value = value(child, depth-1);
			if (bestValue == 0 || ((value > bestValue) == (stateMachine.getActiveColor() == color))){
				bestValue = value;
				bestMove = child.getState();
			}
		}
		return bestMove;
	}
	
	public int value (StateMachine stateMachine, int depth){
		if(stateMachine.getPhase().isTerminal() || depth == 0){
			return h.evaluate(stateMachine.getState(), color);
		}
		else {
			int bestValue = 0;
			for (StateMachine child : stateMachine.getPossibleNextStateMachines().values()){
				int thisValue = value(child, depth-1);
				if(bestValue == 0 || (thisValue > bestValue) == (stateMachine.getActiveColor() == color)){
					bestValue = thisValue;
				}
			}
			return bestValue;
		}
	}
}
