package de.rwth_aachen.kbsg.dq;

import java.util.HashMap;
import java.util.Map;


public class Game {
	private UI ui;
	private Player white;
	private Player black;
	
	private int nOccupations = 0;
	private Phase oldPhase;
	private Phase phase = Phase.OCCUPY;
	private Player active;
	private State state = new State();
	private int noMillClosedSince = 0;
	private Map<State, Integer> history = new HashMap<State, Integer>();
	
	public Game(UI ui, Player p1, Player p2) {
		if (p1.getColor() == null || p2.getColor() == null || p1.getColor() == p2.getColor()) {
			throw new IllegalArgumentException("invalid colors of players "+ p1 +" and "+ p2);
		}
		this.ui = ui;
		this.white = p1.getColor() == Color.WHITE ? p1 : p2;
		this.black = p1.getColor() == Color.WHITE ? p2 : p1;
		this.active = white;
	}

	/**
	 * Asks the player for the next move until it returns a legal move.
	 */
	private static State getNewState(UI ui, Phase phase, Player active, State state) {
		while (true) {
			try {
				switch (phase) {
				case OCCUPY: {
					State s = active.occupy(state);
					while (!state.getPossibleNextStates(phase, active.getColor()).contains(s)) {
						ui.notifyIllegalMove(active, state, s);
						s = active.occupy(state);
					}
					return s;
				}
				case MOVE: {
					State s = active.move(state);
					while (!state.getPossibleNextStates(phase, active.getColor()).contains(s)) {
						ui.notifyIllegalMove(active, state, s);
						s = active.occupy(state);
					}
					return s;
				}
				case TAKE: {
					State s = active.take(state);
					while (!state.getPossibleNextStates(phase, active.getColor()).contains(s)) {
						ui.notifyIllegalMove(active, state, s);
						s = active.occupy(state);
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
	
	private static boolean mayTake(Player active, State oldState, State newState) {
		for (Point p : oldState.pointsOfField()) {
			if (!oldState.isMuehle(p, active.getColor()) && newState.isMuehle(p, active.getColor())) {
				return true;
			}
		}
		return false;
	}
	
	private Player inactive() {
		return active == white ? black : white; 
	}
	
	private void addToHistory(State state) {
		if (!history.containsKey(state)) {
			history.put(state, 0);
		}
		history.put(state, history.get(state) + 1);
	}
	
	private boolean isDraw() {
		return noMillClosedSince >= 50 || (history.containsKey(state) && history.get(state) >= 3);
	}
	
	private boolean isWin(Player p) {
		Color opponent = p.getColor().opponentOf();
		return phase == Phase.MOVE &&
				(state.countPieces(opponent) < 3 || state.getPossibleNextStates(Phase.MOVE, opponent).isEmpty());
	}
	
	/**
	 * The transition function of the game's state machine.
	 * If it returns false, the game is over.
	 */
	private boolean transition() {
		if (isDraw()) {
			phase = Phase.DRAW;
			return false;
		} else if (isWin(white)) {
			phase = Phase.WIN;
			active = white;
		} else if (isWin(black)) {
			phase = Phase.WIN;
			active = black;
		}
		
		switch (phase) {
		case OCCUPY: {
			State newState = getNewState(ui, phase, active, state);
			++nOccupations;
			if (mayTake(active, state, newState)) {
				oldPhase = Phase.OCCUPY;
				phase = Phase.TAKE;
				state = newState;
			} else {
				phase = nOccupations < 18 ? Phase.OCCUPY : Phase.MOVE;
				active = inactive();
				state = newState;
			}
			return true;
		}
		case MOVE: {
			State newState = getNewState(ui, phase, active, state);
			if (mayTake(active, state, newState)) {
				oldPhase = Phase.MOVE;
				phase = Phase.TAKE;
				state = newState;
				noMillClosedSince = 0;
			} else {
				active = inactive();
				state = newState;
				addToHistory(state);
				++noMillClosedSince;
			}
			return true;
		}
		case TAKE: {
			State newState = getNewState(ui, phase, active, state);
			phase = oldPhase;
			active = inactive();
			state = newState;
			addToHistory(state);
			return true;
		}
		case WIN:
			return false;
		case DRAW:
			return false;
		default:
			return false;
		}
	}
	
	public void play() {
		while (transition()) {
			ui.notifyPhase(phase);
			ui.notifyState(state);
		}
		ui.notifyState(state);
		if (phase == Phase.WIN) {
			ui.notifyWin(active);
		} else if (phase == Phase.DRAW) {
			ui.notifyDraw();
		} else {
			throw new RuntimeException("game ended without WIN or DRAW but "+ phase);
		}
	}
}
