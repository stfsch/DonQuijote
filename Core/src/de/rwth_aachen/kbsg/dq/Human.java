package de.rwth_aachen.kbsg.dq;

public class Human extends Player {
	private final UI ui;

	public Human(Color color, UI ui) {
		super(color);
		this.ui = ui;
	}
	
	public State occupy(State s) {
		Point p = ui.getPoint(getColor());
		return s.occupy(p, getColor());
	}
	
	public State take(State s) {
		Point p = ui.getPoint(getColor());
		return s.take(p);
	}
	
	public State move(State s) {
		Point from = ui.getPoint(getColor());
		Point to = ui.getPoint(getColor());
		return s.move(from,  to);
	}
}
