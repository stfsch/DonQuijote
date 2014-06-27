package de.rwth_aachen.kbsg.dq;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
	
	private StateMachine(State s, StateMachine m) {
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
	
	protected int historyCount(State state) {
		int n = 0;
		for (StateMachine m = pred; m != null && m.getPhase() != Phase.OCCUPY; m = m.pred) {
			if (m.getPhase() == Phase.MOVE && m.getState().equals(state)) {
				++n;
			}
		}
		return n;
	}
	
	public StateMachine withoutDrawTest() {
		return new StateMachine() {
			@Override
			protected boolean isDraw() {
				return false;
			}
		};
	}
	
	protected boolean isDraw() {
		return noMillClosedSince >= 50 || historyCount(getState()) >= 3;
	}
	
	protected boolean isWin(Color c) {
		return phase == Phase.MOVE && (state.countPieces(c.opponent()) < 3 || state.getPossibleNextStates(Phase.MOVE, c.opponent()).isEmpty());
	}
	
	/**
	 * Computes a non-empty map of states and state machines.
	 * Using a map of state to state machine allows to efficiently determine whether or not a certain state is reachable and access the corresponding state machine.
	 * The set is never empty, because if there is no possible move, the respective player has lost and in this case a singleton map is returned.
	 */
	public Map<State, StateMachine> getPossibleNextStateMachines() {
		if (isDraw()) {
			StateMachine m = new StateMachine(state, this);
			m.phase = Phase.DRAW;
			return Collections.singletonMap(m.state, m);
		} else if (isWin(Color.WHITE)) {
			StateMachine m = new StateMachine(state, this);
			m.phase = Phase.WIN;
			m.active = Color.WHITE;
			return Collections.singletonMap(m.state, m);
		} else if (isWin(Color.BLACK)) {
			StateMachine m = new StateMachine(state, this);
			m.phase = Phase.WIN;
			m.active = Color.BLACK;
			return Collections.singletonMap(m.state, m);
		}
		
		switch (phase) {
		case OCCUPY: {
			Map<State, StateMachine> sms = new HashMap<State, StateMachine>();
			for (State s : state.getPossibleNextStates(phase, active)) {
				StateMachine m = new StateMachine(s, this);
				m.nOccupations = nOccupations + 1;
				if (mustTake(active, state, s)) {
					m.oldPhase = m.nOccupations < 18 ? Phase.OCCUPY : Phase.MOVE;
					m.phase = Phase.TAKE;
				} else {
					m.phase = m.nOccupations < 18 ? Phase.OCCUPY : Phase.MOVE;
					m.active = active.opponent();
				}
				sms.put(s, m);
			}
			return sms;
		}
		case MOVE: {
			Map<State, StateMachine> sms = new HashMap<State, StateMachine>();
			for (State s : state.getPossibleNextStates(phase, active)) {
				StateMachine m = new StateMachine(s, this);
				if (mustTake(active, state, s)) {
					m.oldPhase = Phase.MOVE;
					m.phase = Phase.TAKE;
					m.noMillClosedSince = 0;
				} else {
					m.active = active.opponent();
					m.noMillClosedSince = noMillClosedSince + 1;
				}
				sms.put(s, m);
			}
			return sms;
		}
		case TAKE: {
			Map<State, StateMachine> sms = new HashMap<State, StateMachine>();
			for (State s : state.getPossibleNextStates(phase, active)) {
				StateMachine m = new StateMachine(s, this);
				m.phase = oldPhase;
				m.active = active.opponent();
				sms.put(s, m);
			}
			return sms;
		}
		case WIN:
			return Collections.emptyMap();
		case DRAW:
			return Collections.emptyMap();
		default:
			return Collections.emptyMap();
		}
	}
}
