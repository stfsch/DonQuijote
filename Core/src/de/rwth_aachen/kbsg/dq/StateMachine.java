package de.rwth_aachen.kbsg.dq;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * A state machine encapsulates a state and transitions to successor state machines.
 * Similar to the State class, StateMachine is immutable.
 */
public class StateMachine {
	private final State state;
	private final StateMachine pred;
	private int nOccupations;
	private Phase oldPhase;
	private Phase phase;
	private Color active;
	private int noMillClosedSince;
	
	public StateMachine() {
		this.state = new State();
		this.pred = null;
		this.nOccupations = 0;
		this.oldPhase = null;
		this.phase = Phase.OCCUPY;
		this.active = Color.WHITE;
		this.noMillClosedSince = 0;
	}
	
	protected StateMachine(State s, StateMachine m) {
		this.state = s;
		this.pred = m;
		this.nOccupations = m.nOccupations;
		this.oldPhase = m.oldPhase;
		this.phase = m.phase;
		this.active = m.active;
		this.noMillClosedSince = m.noMillClosedSince;
	}

	private static boolean mustTake(Color active, State oldState, State newState) {
		for (Point p : oldState.pointsOfField()) {
			if (!oldState.isMill(p, active) && newState.isMill(p, active)) {
				return true;
			}
		}
		return false;
	}
	
	public Color getActiveColor() {
		return active;
	}
	
	public Phase getPhase() {
		return phase;
	}
	
	public State getState() {
		return state;
	}
	
	private int historyCount(State state) {
		int n = 0;
		for (StateMachine m = pred; m != null && m.getPhase() != Phase.OCCUPY; m = m.pred) {
			if (m.getPhase() == Phase.MOVE && m.getState().equals(state)) {
				++n;
			}
		}
		return n;
	}
	
	private boolean isDraw() {
		return noMillClosedSince >= 50 || historyCount(getState()) >= 3;
	}
	
	private boolean isWin(Color c) {
		return phase == Phase.MOVE && (state.countPieces(c.opponent()) < 3 || state.getPossibleNextStates(Phase.MOVE, c.opponent()).isEmpty());
	}
	
	public Collection<StateMachine> getPossibleNextStateMachines() {
		if (isDraw()) {
			phase = Phase.DRAW;
			return Collections.emptySet();
		} else if (isWin(Color.WHITE)) {
			phase = Phase.WIN;
			active = Color.WHITE;
		} else if (isWin(Color.BLACK)) {
			phase = Phase.WIN;
			active = Color.BLACK;
		}
		
		switch (phase) {
		case OCCUPY: {
			Collection<StateMachine> ms = new Vector<StateMachine>(24);
			for (State newState : state.getPossibleNextStates(phase, active)) {
				StateMachine m = new StateMachine(newState, this);
				m.nOccupations = nOccupations + 1;
				if (mustTake(active, state, newState)) {
					m.oldPhase = m.nOccupations < 18 ? Phase.OCCUPY : Phase.MOVE;
					m.phase = Phase.TAKE;
				} else {
					m.phase = m.nOccupations < 18 ? Phase.OCCUPY : Phase.MOVE;
					m.active = active.opponent();
				}
				ms.add(m);
			}
			return ms;
		}
		case MOVE: {
			Collection<StateMachine> ms = new Vector<StateMachine>(24);
			for (State newState : state.getPossibleNextStates(phase, active)) {
				StateMachine m = new StateMachine(newState, this);
				if (mustTake(active, state, newState)) {
					m.oldPhase = Phase.MOVE;
					m.phase = Phase.TAKE;
					m.noMillClosedSince = 0;
				} else {
					m.active = active.opponent();
					m.noMillClosedSince = noMillClosedSince + 1;
				}
				ms.add(m);
			}
			return ms;
		}
		case TAKE: {
			Collection<StateMachine> ms = new Vector<StateMachine>(24);
			for (State newState : state.getPossibleNextStates(phase, active)) {
				StateMachine m = new StateMachine(newState, this);
				m.phase = oldPhase;
				m.active = active;
				ms.add(m);
			}
			return ms;
		}
		case WIN:
			return Collections.emptyList();
		case DRAW:
			return Collections.emptyList();
		default:
			return Collections.emptyList();
		}
	}
	
	public Set<State> getPossibleNextStates() {
		Set<State> states = new HashSet<State>();
		for (StateMachine sm : getPossibleNextStateMachines()) {
			states.add(sm.getState());
		}
		return states;
	}
}
