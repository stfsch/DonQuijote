package de.rwth_aachen.kbsg.dq;

/**
 * evaluates states by counting free direct neighbors (phase = occupation) or by counting the pieces (phase = move)
 */
public class Heuristic4 extends Heuristic{

	@Override
	public int evaluate(StateMachine m) {
		Phase phase = m.getPhase();
		State s = m.getState();
		int value = 0;
		switch (phase){
		case OCCUPY:
			for (Point p : s.getOccupations(Color.WHITE)){
				for(Point n : s.onlyFree(s.neighborsOf(p))){
						value ++;
				}
			}
			for (Point p : s.getOccupations(Color.BLACK)){
				for(Point n : s.onlyFree(s.neighborsOf(p))){
						value --;
				}
			}
			break;
		case MOVE:
			value += s.countPieces(Color.WHITE);
			value -= s.countPieces(Color.BLACK);
			break;
		default:
		}
		return value;
	}

}
