package de.rwth_aachen.kbsg.dq;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

/**
 * The state represents which points on the field are empty or occupied by one of the players.
 * Objects of this class are immutable. 
 */
public class State {
	private int white = 0;
	private int black = 0;

	public State() {
	}
	
	public State(State s) {
		white = s.white;
		black = s.black;
	}
	
	/**
	 * The occupancy of the value is either <code>null</code> or the occupying player's color.
	 */
	public Color getOccupancy(Point p) {
		final int mask = 1 << p.getNumber();
		return (white & mask) != 0 ? Color.WHITE : (black & mask) != 0 ? Color.BLACK : null;
	}

	/**
	 * Sets the occupancy value to either <code>null</code> or the occupying player's color.
	 * This method is private because State is an immutable class.
	 */
	private void setOccupancy(Point p, Color c) {
		final int mask = 1 << p.getNumber();
		if (c == Color.WHITE) {
			white |= mask;
			black &= ~ mask;
		} else if (c == Color.BLACK) {
			white &= ~ mask;
			black |= mask;
		} else {
			white &= ~ mask;
			black &= ~ mask;
		}
	}

	public boolean isOccupied(Point p) {
		final int mask = 1 << p.getNumber();
		return ((white | black) & mask) != 0;
	}
	
	public boolean isOccupiedBy(Point p, Color c) {
		final int mask = 1 << p.getNumber();
		return c == Color.WHITE && (white & mask) != 0 ||
			   c == Color.BLACK && (black & mask) != 0 ||
			   c == null        && ((white | black) & mask) != 0;
	}
	
	public Iterable<Point> getOccupations(Color c) {
		final int bits = c == Color.WHITE ? white : black;
		Collection<Point> ps = new Vector<Point>(Integer.bitCount(bits));
		for (Point p : pointsOfField()) {
			if (isOccupiedBy(p, c)) {
				ps.add(p);
			}
		}
		return ps;
	}
	
	public int countPieces(Color c) {
		return c == Color.WHITE ? Integer.bitCount(white) : c == Color.BLACK ? Integer.bitCount(black) : 3*8 - Integer.bitCount(white | black);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + black;
		result = prime * result + white;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (obj != null && obj instanceof State && white == ((State) obj).white && black == ((State) obj).black);
	}

	/**
	 * Creates a new state where the point is occupied by the given player.
	 * @throws IllegalOccupationException if the point was occupied
	 */
	public State occupy(Point p, Color c) {
		if (isOccupied(p)) {
			throw new IllegalOccupationException("point "+ p +" is already occupied");
		}
		State s = new State(this);
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
		State s = new State(this);
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
		State s = new State(this);
		s.setOccupancy(p, null);
		return s;
	}
	
	private static final Collection<Point> points = new Vector<Point>(4*8);
	
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
	
	private static final Collection<Iterable<Point>> lines = new Vector<Iterable<Point>>(4*4);
	
	static {
		Vector<Point> l;
		l = new Vector<Point>(3); l.add(new Point(0, 0)); l.add(new Point(0, 1)); l.add(new Point(0, 2)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(0, 2)); l.add(new Point(0, 3)); l.add(new Point(0, 4)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(0, 4)); l.add(new Point(0, 5)); l.add(new Point(0, 6)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(0, 6)); l.add(new Point(0, 7)); l.add(new Point(0, 0)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(1, 0)); l.add(new Point(1, 1)); l.add(new Point(1, 2)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(1, 2)); l.add(new Point(1, 3)); l.add(new Point(1, 4)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(1, 4)); l.add(new Point(1, 5)); l.add(new Point(1, 6)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(1, 6)); l.add(new Point(1, 7)); l.add(new Point(1, 0)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(2, 0)); l.add(new Point(2, 1)); l.add(new Point(2, 2)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(2, 2)); l.add(new Point(2, 3)); l.add(new Point(2, 4)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(2, 4)); l.add(new Point(2, 5)); l.add(new Point(2, 6)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(2, 6)); l.add(new Point(2, 7)); l.add(new Point(2, 0)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(0, 1)); l.add(new Point(1, 1)); l.add(new Point(2, 1)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(0, 3)); l.add(new Point(1, 3)); l.add(new Point(2, 3)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(0, 5)); l.add(new Point(1, 5)); l.add(new Point(2, 5)); lines.add(l);
		l = new Vector<Point>(3); l.add(new Point(0, 7)); l.add(new Point(1, 7)); l.add(new Point(2, 7)); lines.add(l);
	}
	
	public Iterable<Iterable<Point>> linesOfField() {
		return lines; 
	}
	
	private static int mod(int x, int y) {
		int r = x % y;
		if (r < 0) {
			r += y;
		}
		return r;
	}
	
	private static Map<Point, Iterable<Point>> neighborsOf = new HashMap<Point, Iterable<Point>>(3*8, 1.0f);
	
	static {
		for (Point p : points) {
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
			neighborsOf.put(p, ps);
		}
	}

	/**
	 * Returns the direct neighbors of the given point.
	 * Each point has at least two and no more than four neighbors.
	 */
	public Iterable<Point> neighborsOf(Point p) {
		return neighborsOf.get(p);
	}
	
	private static final Map<Point, Iterable<Iterable<Point>>> linesOf = new HashMap<Point, Iterable<Iterable<Point>>>(3*8, 1.0f); 
	
	static {
		for (Point p : points) {
			Collection<Iterable<Point>> lines = new Vector<Iterable<Point>>(3);
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
			linesOf.put(p, lines);
		}
	}
	
	/**
	 * Returns the lines in which the given point is in.
	 * Each of these lines is a potential mill.
	 * Each point is part in two lines. 
	 */
	public Iterable<Iterable<Point>> linesOf(Point p) {
		return linesOf.get(p);
	}
	
	private static interface Predicate<E> {
		public boolean holds(E e);
	}
	
	private static class FilteringIterator<E> implements Iterator<E> {
		private final Iterator<E> iter;
		private final Predicate<E> pred;
		private boolean have = false;
		private E elem = null;
		
		public FilteringIterator(Iterable<E> iterable, Predicate<E> p) {
			iter = iterable.iterator();
			pred = p;
			while ((have = iter.hasNext())) {
				elem = iter.next();
				if (pred.holds(elem)) {
					break;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return have;
		}

		@Override
		public E next() {
			if (!have) {
				throw new NoSuchElementException();
			}
			E lastElem = elem;
			while ((have = iter.hasNext())) {
				elem = iter.next();
				if (pred.holds(elem)) {
					break;
				}
			}
			return lastElem;
		}
	}
	
	private static <E> Iterable<E> filter(final Iterable<E> iterable, final Predicate<E> pred) {
		return new Iterable<E>() {
			@Override
			public Iterator<E> iterator() {
				return new FilteringIterator<E>(iterable, pred);
			}
		};
	}
	
	public Iterable<Point> onlyFree(Iterable<Point> ps) {
		return filter(ps, new Predicate<Point>() {
			@Override
			public boolean holds(Point p) {
				return !isOccupied(p);
			}
		});
	}
	
	public Iterable<Point> onlyOccupiedBy(Iterable<Point> ps, final Color c) {
		return filter(ps, new Predicate<Point>() {
			@Override
			public boolean holds(Point p) {
				return isOccupiedBy(p, c);
			}
		});
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
			for (Point p : onlyOccupiedBy(pointsOfField(), color.opponent())) {
				if (!isMill(p, color.opponent()) || hasOnlyMills(color.opponent())) {
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
		for (Iterable<Point> line : linesOf(p)) {
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
	
	public boolean areAllOccupiedBy(Iterable<Point> ps, Color color) {
		for (Point p : ps) {
			if (!isOccupiedBy(p, color)) {
				return false;
			}
		}
		return true;
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