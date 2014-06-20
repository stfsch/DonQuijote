package de.rwth_aachen.kbsg.dq;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * The state represents which points on the field are empty or occupied by one of the players.
 * Objects of this class are immutable. 
 */
public class State {
	private final BitSet white = new BitSet(3*8);
	private final BitSet black = new BitSet(3*8);

	public State() {
	}
	
	/**
	 * The occupancy of the value is either <code>null</code> or the occupying player's color.
	 */
	public Color getOccupancy(Point p) {
		final int i = p.getFrame() * 8 + p.getIndex();
		return white.get(i) ? Color.WHITE : black.get(i) ? Color.BLACK : null;
	}
	
	private static Point indexToPoint(int i) {
		return new Point(i / 8, i % 8);
	}
	
	private static int pointToIndex(Point p) {
		return p.getFrame() * 8 + p.getIndex();
	}
	
	/**
	 * Sets the occupancy value to either <code>null</code> or the occupying player's color.
	 * This method is private because State is an immutable class.
	 */
	private void setOccupancy(Point p, Color c) {
		final int i = pointToIndex(p);
		white.set(i, c == Color.WHITE);
		black.set(i, c == Color.BLACK);
	}

	public boolean isOccupied(Point p) {
		return getOccupancy(p) != null;
	}
	
	public boolean isOccupiedBy(Point p, Color c) {
		return getOccupancy(p) == c;
	}
	
	public Iterable<Point> getOccupations(Color c) {
		BitSet bs = c == Color.WHITE ? white : black;
		Collection<Point> ps = new Vector<Point>(9);
		for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
			ps.add(indexToPoint(i));
		}
		return ps;
	}
	
	public int countPieces(Color color) {
		switch (color) {
		case WHITE:
			return white.cardinality();
		case BLACK:
			return black.cardinality();
		default:
			return 3*8 - white.cardinality() - black.cardinality();
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((black == null) ? 0 : black.hashCode());
		result = prime * result + ((white == null) ? 0 : white.hashCode());
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
		if (black == null) {
			if (other.black != null)
				return false;
		} else if (!black.equals(other.black))
			return false;
		if (white == null) {
			if (other.white != null)
				return false;
		} else if (!white.equals(other.white))
			return false;
		return true;
	}

	public State copy() {
		State s = new State();
		s.white.or(white);
		s.black.or(black);
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
	public Iterable<? extends Collection<Point>> linesOf(Point p) {
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
			line.add(new Point(p.getFrame(), mod(p.getIndex() - 0, 8)));
			line.add(new Point(p.getFrame(), mod(p.getIndex() - 1, 8)));
			line.add(new Point(p.getFrame(), mod(p.getIndex() - 2, 8)));
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
				if (!isMill(p, color.opponentOf()) || hasOnlyMills(color.opponentOf())) {
					states.add(take(p));
				}
			}
			break;
		case WIN:
		case DRAW:
		}
		return states;
	}
	
	public boolean isMill(Point p, Color color) {
		for (Collection<Point> line : linesOf(p)) {
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
	
	public boolean hasOnlyMills(Color color) {
		for (Point p : getOccupations(color)) {
			if (!isMill(p, color)) {
				return false;
			}
		}
		return true;
	}

	public boolean mayJump(Color color) {
		return countPieces(color) <= 3;
	}
}