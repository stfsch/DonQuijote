package de.rwth_aachen.kbsg.dq;

import java.util.Map;

public class Game {
	private final UI ui;
	private final Player white;
	private final Player black;
	private StateMachine stateMachine = new StateMachine();
	
	public Game(UI ui){
		this.ui = ui;
		this.white = ui.inputPlayer(Color.WHITE);
		this.black = ui.inputPlayer(Color.BLACK);
	}

	/**
	 * Asks the player for the next move until it returns a legally reachable new state.
	 */
	private StateMachine getNextLegalStateMachine() {
		Player p = stateMachine.getActiveColor() == Color.WHITE ? white : black;
		Map<State, StateMachine> sms = stateMachine.getPossibleNextStateMachines();
		StateMachine firstStateMachine = sms.values().iterator().next();
		if (firstStateMachine.getPhase().isTerminal()) {
			return firstStateMachine;
		}
		while (true) {
			try {
				switch (stateMachine.getPhase()) {
				case OCCUPY: {
					State s = p.occupy(stateMachine);
					StateMachine m;
					while ((m = sms.get(s)) == null) {
						ui.illegalMove(p.getColor(), stateMachine, s);
						s = p.occupy(stateMachine);
					}
					return m;
				}
				case MOVE: {
					State s = p.move(stateMachine);
					StateMachine m;
					while ((m = sms.get(s)) == null) {
						ui.illegalMove(p.getColor(), stateMachine, s);
						s = p.move(stateMachine);
					}
					return m;
				}
				case TAKE: {
					State s = p.take(stateMachine);
					StateMachine m;
					while ((m = sms.get(s)) == null) {
						ui.illegalMove(p.getColor(), stateMachine, s);
						s = p.take(stateMachine);
					}
					return m;
				}
				case WIN:
				case DRAW:
				default:
					throw new IllegalArgumentException();
				}
			} catch (IllegalOccupationException | IllegalMoveException | IllegalTakeException exc) {
				System.err.println(exc.getMessage());
			}
		}
	}
	
	private boolean transition() {
		if (stateMachine.getPhase().isTerminal()) {
			return false;
		} else {
			stateMachine = getNextLegalStateMachine();
			assert stateMachine != null;
			return true;
		}
	}
	
	public void play() {
		while (transition()) {
			ui.stateMachineChanged(stateMachine);
		}
		ui.stateMachineChanged(stateMachine);
		if (stateMachine.getPhase() == Phase.WIN) {
			ui.gameWon(stateMachine.getActiveColor());
		} else if (stateMachine.getPhase() == Phase.DRAW) {
			ui.gameDrawn();
		} else {
			throw new RuntimeException("game ended without WIN or DRAW but "+ stateMachine.getPhase());
		}
	}

	public StateMachine getStateMachine() {
		return stateMachine;
	}
}
