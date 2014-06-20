package de.rwth_aachen.kbsg.dq;

public class Human extends Player {
	private final UI ui;

	public Human(Color color, UI ui) {
		super(color);
		this.ui = ui;
	}
	
	public State occupy(State s) {
		Point p = ui.inputPoint(getColor());
		return s.occupy(p, getColor());
	}
	
	public State take(State s) {
		Point p = ui.inputPoint(getColor());
		return s.take(p);
	}
	
	public State move(State s) {
		Point from = ui.inputPoint(getColor());
		Point to = ui.inputPoint(getColor());
		return s.move(from,  to);
	}
}
