package de.rwth_aachen.kbsg.dq;
import java.util.Random;
public class RandomAgent extends Player{
	Random random;
	public RandomAgent(Color color) {
		super(color);
		this.random = new Random();		
	}

	@Override
	public State occupy(StateMachine stateMachine) {
		Point p = new Point (random.nextInt(3), random.nextInt(8));
		return stateMachine.getState().occupy(p, getColor());
	}

	@Override
	public State take(StateMachine stateMachine) {
		Point p = new Point(random.nextInt(3), random.nextInt(8));
		return stateMachine.getState().take(p);
	}

	@Override
	public State move(StateMachine stateMachine) {
		Point p = new Point(random.nextInt(3), random.nextInt(8));
		Point q = new Point(random.nextInt(3), random.nextInt(8));
		return stateMachine.getState().move(p, q);
	}
	

}
