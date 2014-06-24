package de.rwth_aachen.kbsg.dq;
import java.util.Random;
public class RandomPlayer extends Player{
	Random random;
	public RandomPlayer(Color color) {
		super(color);
		this.random = new Random();		
	}

	@Override
	public State occupy(State s) {
		Point p = new Point (random.nextInt(3), random.nextInt(8));
		return s.occupy(p, getColor());
	}

	@Override
	public State take(State s) {
		Point p = new Point(random.nextInt(3), random.nextInt(8));
		return s.take(p);
	}

	@Override
	public State move(State s) {
		Point p = new Point(random.nextInt(3), random.nextInt(8));
		Point q = new Point(random.nextInt(3), random.nextInt(8));
		return s.move(p, q);
	}
	

}
