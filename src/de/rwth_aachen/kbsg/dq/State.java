package de.rwth_aachen.kbsg.dq;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * The state represents which points on the field are empty or occupied by one of the players.
 * Objects of this class are immutable. 
 */
public class State {
	private final Color occupancy[][] = new Color[3][8];

	public State() {
	}
	
	/**
	 * The occupancy of the value is either <code>null</code> or the occupying player's color.
	 */
	public Color getOccupancy(Point p) {
		return occupancy[p.getFrame()][p.getIndex()];
	}
	
	/**
	 * Sets the occupancy value to either <code>null</code> or the occupying player's color.
	 * This method is private because State is an immutable class.
	 */
	private void setOccupancy(Point p, Color c) {
		occupancy[p.getFrame()][p.getIndex()] = c;
	}

	public boolean isOccupied(Point p) {
		return getOccupancy(p) != null;
	}
	
	public boolean isOccupiedBy(Point p, Color c) {
		return getOccupancy(p) == c;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(occupancy);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (!Arrays.deepEquals(occupancy, other.occupancy))
			return false;
		return true;
	}
	
	protected State copy() {
		State s = new State();
		for (int i = 0; i < occupancy.length; ++i) {
			s.occupancy[i] = Arrays.copyOf(this.occupancy[i], this.occupancy[i].length);
		}
		return s;
	}
	
	/**
	 * Creates a new state where the point is occupied by the given player.
	 * @throws IllegalOccupationException if the point was occupied
	 */
	public State occupy(Point p, Color c) {
		if (getOccupancy(p) != null) {
			throw new IllegalOccupationException("point "+ p +" is already occupied");
		}
		State s = copy();
		s.setOccupancy(p, c);
		return s;
	}
	
	/**
	 * Creates a new state where the piece is moved from the first to the second position.
	 * @throws IllegalMoveException if p was not occupied or q was occupied
	 */
	public State move(Point p, Point q) {
		if (!isOccupied(p)) {
			throw new IllegalMoveException("point "+ p +" is not occupied");
		}
		if (isOccupied(q)) {
			throw new IllegalMoveException("point "+ q +" is already occupied");
		}
		State s = copy();
		Color c = getOccupancy(p);
		s.setOccupancy(p, null);
		s.setOccupancy(q, c);
		return s;
	}
	
	/**
	 * Creates a new state where the piece at the given position is removed.
	 * @return IllegalTakeException if p was not occupied
	 */
	public State take(Point p) {
		if (!isOccupied(p)) {
			throw new IllegalTakeException("point "+ p +" is not occupied");
		}
		State s = copy();
		s.setOccupancy(p, null);
		return s;
	}
	
	private final static Collection<Point> points = new Vector<Point>(4*8);
	
	static {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 8; ++j) {
				points.add(new Point(i, j));
			}
		}
	}
	
	public Iterable<Point> pointsOfField() {
		return points;
	}
	
	private static int mod(int x, int y) {
		int r = x % y;
		if (r < 0) {
			r += y;
		}
		return r;
	}

	/**
	 * Returns the direct neighbors of the given point.
	 * Each point has at least two and no more than four neighbors.
	 */
	public Iterable<Point> neighborsOf(Point p) {
		Set<Point> ps = new HashSet<Point>();
		if (p.getIndex() % 2 == 1) { // can switch to neighboring frames
			if (p.getFrame() > 0) {
				ps.add(new Point(p.getFrame() - 1, p.getIndex()));
			}
			if (p.getFrame() < 2) {
				ps.add(new Point(p.getFrame() + 1, p.getIndex()));
			}
		}
		ps.add(new Point(p.getFrame(), mod(p.getIndex() - 1, 8)));
		ps.add(new Point(p.getFrame(), mod(p.getIndex() + 1, 8)));
		return ps;
	}
	
	/**
	 * Returns the lines in which the given point is in.
	 * Each of these lines is a potential mill.
	 * Each point is part in two lines. 
	 */
	public Iterable<? extends Collection<Point>> linematesOf(Point p) {
		Collection<Collection<Point>> lines = new Vector<Collection<Point>>(3);
		Collection<Point> line;
		
		line = new Vector<Point>(2);
		int i = (p.getIndex() / 2) * 2;
		line.add(new Point(p.getFrame(), mod(i + 0, 8)));
		line.add(new Point(p.getFrame(), mod(i + 1, 8)));
		line.add(new Point(p.getFrame(), mod(i + 2, 8)));
		lines.add(line);
		
		if (p.getIndex() % 2 == 0) {
			line = new Vector<Point>(3);
			line.add(new Point(p.getFrame(), mod(p.getIndex() + 0, 8)));
			line.add(new Point(p.getFrame(), mod(p.getIndex() + 1, 8)));
			line.add(new Point(p.getFrame(), mod(p.getIndex() + 2, 8)));
			lines.add(line);
		}
		
		if (p.getIndex() % 2 == 1) {
			line = new Vector<Point>(3);
			line.add(new Point(0, p.getIndex()));
			line.add(new Point(1, p.getIndex()));
			line.add(new Point(2, p.getIndex()));
			lines.add(line);
		}
		return lines;
	}
	
	public Iterable<Point> onlyFree(Iterable<Point> ps) {
		Set<Point> qs = new HashSet<Point>();
		for (Point p : ps) {
			if (!isOccupied(p)) {
				qs.add(p);
			}
		}
		return qs;
	}
	
	public Iterable<Point> onlyOccupiedBy(Iterable<Point> ps, Color c) {
		Set<Point> qs = new HashSet<Point>();
		for (Point p : ps) {
			if (isOccupiedBy(p, c)) {
				qs.add(p);
			}
		}
		return qs;
	}
	
	public Set<State> getPossibleNextStates(Phase phase, Color color) {
		Set<State> states = new HashSet<State>();
		switch (phase) {
		case OCCUPY:
			for (Point p : onlyFree(pointsOfField())) {
				states.add(occupy(p, color));
			}
			break;
		case MOVE:
			final boolean mayJump = mayJump(color);
			for (Point p : onlyOccupiedBy(pointsOfField(), color)) {
				for (Point q : onlyFree(mayJump ? pointsOfField() : neighborsOf(p))) {
					states.add(move(p, q));
				}
			}
			break;
		case TAKE:
			for (Point p : onlyOccupiedBy(pointsOfField(), color.opponentOf())) {
				if (!isMuehle(p, color.opponentOf())) {
					states.add(take(p));
				}
			}
			break;
		case WIN:
		case DRAW:
		}
		return states;
	}
	
	public boolean isMuehle(Point p, Color color) {
		for (Collection<Point> line : linematesOf(p)) {
			boolean mill = true;
			for (Point q : line) {
				mill &= isOccupiedBy(q, color);
			}
			if (mill) {
				return true;
			}
		}
		return false;
	}

	public int countPieces(Color color) {
		int men = 0;
		for (Point p : onlyOccupiedBy(pointsOfField(), color)) {
			++men;
		}
		return men;
	}
	
	public boolean mayJump(Color color) {
		return countPieces(color) <= 3;
	}
}