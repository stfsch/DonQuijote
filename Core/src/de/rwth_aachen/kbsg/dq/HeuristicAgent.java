package de.rwth_aachen.kbsg.dq;

public class HeuristicAgent extends Player{
	public Heuristic h;
	public HeuristicAgent (Color color, Heuristic heuristic){
		super (color);
		this.h = heuristic;
	}

	@Override
	public State occupy(StateMachine stateMachine) {
		int max = 0;
		State bestMove = null;
		for (StateMachine possibleNextStateMachine : stateMachine.getPossibleNextStateMachines().values()){
			int value = h.evaluate(possibleNextStateMachine);
			if(max == 0 || ((value > max) == (stateMachine.getActiveColor() == Color.WHITE))){
				max = value;
				bestMove = possibleNextStateMachine.getState();
			}
		}
		return bestMove;
	}

	@Override
	public State take(StateMachine stateMachine) {
		int max = 0;
		State bestMove = null;
		for (StateMachine possibleNextStateMachine : stateMachine.getPossibleNextStateMachines().values()){
			int value = h.evaluate(possibleNextStateMachine);
			if(max == 0 || ((value > max) == (stateMachine.getActiveColor() == Color.WHITE))){
				max = value;
				bestMove = possibleNextStateMachine.getState();
			}
		}
		return bestMove;
	}

	@Override
	public State move(StateMachine stateMachine) {
		int max = 0;
		State bestMove = null;
		for (StateMachine possibleNextStateMachine : stateMachine.getPossibleNextStateMachines().values()){
			int value = h.evaluate(possibleNextStateMachine);
			if(max == 0 || ((value > max) == (stateMachine.getActiveColor() == Color.WHITE))){
				max = value;
				bestMove = possibleNextStateMachine.getState();
			}
		}
		return bestMove;
	}
}
