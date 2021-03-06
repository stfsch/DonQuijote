package de.rwth_aachen.kbsg.dq;

import java.util.Collection;

public class MiniMax {
	public Color color;
	long maxNodes;
	StateMachine stateMachine;
	long visitedNodes;
	public Heuristic h;
	
	public MiniMax(Color color, long maxNodes, Heuristic heuristic, StateMachine stateMachine) {
		this.color = color;
		this.maxNodes = maxNodes;
		this.h = heuristic;
		visitedNodes = 0;
		this.stateMachine = stateMachine;
	}
	public State minimax(){
		int depth = 1;
		State lastMove;
		State move = null;
		do{
			lastMove = move;
			move = minimax(depth);
			depth ++;
		}
		while(visitedNodes <= maxNodes);
		System.out.println(depth-1);
		if(lastMove == null){
			throw new RuntimeException("too small maxNodes. maxNodes = " + maxNodes);
		}
		return lastMove;
	}
	public State minimax (int depth){
		Collection<StateMachine> children = stateMachine.getPossibleNextStateMachines().values();
		State bestMove = null;
		int bestValue = 0;
		for (StateMachine child : children){
			int value = value(child, depth-1);
			if (bestValue == 0 || ((value > bestValue) == (stateMachine.getActiveColor() == Color.WHITE))){
				bestValue = value;
				bestMove = child.getState();
			}
			visitedNodes++;
		}
		return bestMove;
	}
	
	public int value (StateMachine stateMachine, int depth){
		if(stateMachine.getPhase().isTerminal() || depth <= 0 || visitedNodes > maxNodes){
			return h.evaluate(stateMachine);
		}
		else {
			stateMachine = stateMachine.withoutDrawTest();
			int bestValue = 0;
			for (StateMachine child : stateMachine.getPossibleNextStateMachines().values()){
				int thisValue = value(child, depth-1);
				if(bestValue == 0 || (thisValue > bestValue) == (stateMachine.getActiveColor() == Color.WHITE)){
					bestValue = thisValue;
				}
				visitedNodes++;
			}
			return bestValue;
		}
	}
}
