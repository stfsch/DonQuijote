package de.rwth_aachen.kbsg.dq;

public class Human extends Player {
	private final UI ui;

	public Human(Color color, UI ui) {
		super(color);
		this.ui = ui;
	}
	
	public State occupy(StateMachine s) {
		Point p = ui.inputPoint(getColor());
		return s.getState().occupy(p, getColor());
	}
	
	public State take(StateMachine s) {
		Point p = ui.inputPoint(getColor());
		return s.getState().take(p);
	}
	
	public State move(StateMachine s) {
		Point from = ui.inputPoint(getColor());
		Point to = ui.inputPoint(getColor());
		return s.getState().move(from,  to);
	}
}
