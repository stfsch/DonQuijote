package de.rwth_aachen.kbsg.dq;

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
	private State getNewState() {
		Player p = stateMachine.getActiveColor() == Color.WHITE ? white : black;
		while (true) {
			try {
				switch (stateMachine.getPhase()) {
				case OCCUPY: {
					State s = p.occupy(stateMachine);
					while (!stateMachine.getPossibleNextStates().contains(s)) {
						ui.illegalMove(p.getColor(), stateMachine, s);
						s = p.occupy(stateMachine);
					}
					return s;
				}
				case MOVE: {
					State s = p.move(stateMachine);
					while (!stateMachine.getPossibleNextStates().contains(s)) {
						ui.illegalMove(p.getColor(), stateMachine, s);
						s = p.move(stateMachine);
					}
					return s;
				}
				case TAKE: {
					State s = p.take(stateMachine);
					while (!stateMachine.getPossibleNextStates().contains(s)) {
						ui.illegalMove(p.getColor(), stateMachine, s);
						s = p.take(stateMachine);
					}
					return s;
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
		if (stateMachine.getPhase() == Phase.DRAW || stateMachine.getPhase() == Phase.WIN) {
			return false;
		} else {
			State newState = getNewState();
			for (StateMachine m : stateMachine.getPossibleNextStateMachines()) {
				if (m.getState().equals(newState)) {
					stateMachine = m;
					return true;
				}
			}
			return false;
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
}
