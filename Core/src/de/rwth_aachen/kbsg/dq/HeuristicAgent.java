package de.rwth_aachen.kbsg.dq;

public class HeuristicAgent extends Player{
	public Heuristic h;
	public HeuristicAgent (Color color, Heuristic heuristic){
		super (color);
		this.h = heuristic;
	}

	@Override
	public State occupy(StateMachine stateMachine) {
		State s = stateMachine.getState();
		int max = 0;
		State bestMove = null;
		for (State possibleNextState : s.getPossibleNextStates(Phase.OCCUPY, getColor())){
			if(max == 0){
				max = h.evaluate(possibleNextState, getColor());
				bestMove = possibleNextState;
			}
			if(h.evaluate(possibleNextState, getColor()) >= max){
				max = h.evaluate(possibleNextState, getColor());
				bestMove = possibleNextState;
			}
		}
		return bestMove;
	}

	@Override
	public State take(StateMachine stateMachine) {
		State s = stateMachine.getState();
		int max = 0;
		State bestMove = null;
		for (State possibleNextState : s.getPossibleNextStates(Phase.TAKE, getColor())){
			if(max == 0){
				max = h.evaluate(possibleNextState, getColor());
				bestMove = possibleNextState;
			}
			if(h.evaluate(possibleNextState, getColor()) >= max){
				max = h.evaluate(possibleNextState, getColor());
				bestMove = possibleNextState;
			}
		}
		return bestMove;
	}

	@Override
	public State move(StateMachine stateMachine) {
		State s = stateMachine.getState();
		int max = 0;
		State bestMove = null;
		for (State possibleNextState : s.getPossibleNextStates(Phase.MOVE, getColor())){
			if(max == 0){
				max = h.evaluate(possibleNextState, getColor());
				bestMove = possibleNextState;
			}
			if(h.evaluate(possibleNextState, getColor()) >= max){
				max = h.evaluate(possibleNextState, getColor());
				bestMove = possibleNextState;
			}
		}
		return bestMove;
	}
}
