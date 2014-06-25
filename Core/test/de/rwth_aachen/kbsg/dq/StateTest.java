package de.rwth_aachen.kbsg.dq;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import de.rwth_aachen.kbsg.dq.Color;
import de.rwth_aachen.kbsg.dq.IllegalMoveException;
import de.rwth_aachen.kbsg.dq.Point;
import de.rwth_aachen.kbsg.dq.State;

public class StateTest {

	@Test
	public void testGetOccupancy() {
		State s = new State();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 8; ++j) {
				assertNull(s.getOccupancy(new Point(i, j)));
			}
		}
	}

	@Test
	public void testIsOccupied() {
		State s = new State();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 8; ++j) {
				assertFalse(s.isOccupied(new Point(i, j)));
			}
		}
	}

	@Test
	public void testIsOccupiedBy() {
		State s = new State();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 8; ++j) {
				assertFalse(s.isOccupiedBy(new Point(i, j), Color.BLACK));
				assertFalse(s.isOccupiedBy(new Point(i, j), Color.WHITE));
			}
		}
	}

	@Test
	public void testCopy() {
		State s = new State();
		assertNotSame(s, new State(s));
		assertTrue(s.equals(new State(s)));
	}

	@Test
	public void testOccupy() {
		State s = new State();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 8; ++j) {
				Point p = new Point(i, j);
				State s1 = s.occupy(p, Color.BLACK);
				State s2 = s.occupy(p, Color.WHITE);
				assertTrue(!s.isOccupiedBy(p, Color.BLACK));
				assertTrue(!s.isOccupiedBy(p, Color.WHITE));
				assertTrue(s1.isOccupiedBy(p, Color.BLACK));
				assertTrue(!s1.isOccupiedBy(p, Color.WHITE));
				assertTrue(s1.isOccupied(p));
				assertTrue(s1.isOccupied(p));
				assertTrue(!s2.isOccupiedBy(p, Color.BLACK));
				assertTrue(s2.isOccupiedBy(p, Color.WHITE));
				assertTrue(s2.isOccupied(p));
				assertTrue(s2.isOccupied(p));
			}
		}
	}
	
	private State makeState() {
		State s = new State();
		s = s.occupy(new Point(0, 0), Color.BLACK);
		s = s.occupy(new Point(0, 1), Color.BLACK);
		s = s.occupy(new Point(0, 7), Color.BLACK);
		s = s.occupy(new Point(1, 0), Color.BLACK);
		s = s.occupy(new Point(1, 1), Color.BLACK);
		s = s.occupy(new Point(2, 0), Color.BLACK);
		s = s.occupy(new Point(2, 1), Color.BLACK);
		s = s.occupy(new Point(0, 2), Color.WHITE);
		s = s.occupy(new Point(0, 6), Color.WHITE);
		s = s.occupy(new Point(1, 2), Color.WHITE);
		s = s.occupy(new Point(1, 6), Color.WHITE);
		s = s.occupy(new Point(2, 3), Color.WHITE);
		s = s.occupy(new Point(2, 6), Color.WHITE);
		return s;
	}

	@Test
	public void testMove() {
		State s = makeState();
		assertTrue(s.isOccupiedBy(new Point(0, 0), Color.BLACK));
		assertTrue(s.isOccupiedBy(new Point(0, 1), Color.BLACK));
		assertTrue(s.isOccupiedBy(new Point(0, 7), Color.BLACK));
		assertTrue(s.isOccupiedBy(new Point(1, 0), Color.BLACK));
		assertTrue(s.isOccupiedBy(new Point(1, 1), Color.BLACK));
		assertTrue(s.isOccupiedBy(new Point(2, 0), Color.BLACK));
		assertTrue(s.isOccupiedBy(new Point(2, 1), Color.BLACK));
		assertTrue(s.isOccupiedBy(new Point(0, 2), Color.WHITE));
		assertTrue(s.isOccupiedBy(new Point(0, 6), Color.WHITE));
		assertTrue(s.isOccupiedBy(new Point(1, 2), Color.WHITE));
		assertTrue(s.isOccupiedBy(new Point(1, 6), Color.WHITE));
		assertTrue(s.isOccupiedBy(new Point(2, 3), Color.WHITE));
		assertTrue(s.isOccupiedBy(new Point(2, 6), Color.WHITE));
		
		for (int j = 0; j < 8; ++j) {
			Point p = new Point(0, j);
			assertTrue(s.isOccupiedBy(p, Color.BLACK) == (j == 0 || j == 1 || j == 7));
		}
		
		State t = s.move(new Point(0, 1), new Point(0, 3)).move(new Point(0, 7), new Point(0, 5));
		for (int j = 0; j < 8; ++j) {
			Point p = new Point(0, j);
			assertTrue(t.isOccupiedBy(p, Color.BLACK) == (j == 0 || j == 3 || j == 5));
		}
	}
	
	@Test
	public void testMove2() {
		State s = makeState();
		s.move(new Point(0, 7), new Point(2, 7));
	}
	
	@Test(expected=IllegalMoveException.class)
	public void testMove3() {
		State s = makeState();
		s.move(new Point(0, 7), new Point(0, 6));
	}

	@Test
	public void testTake() {
		State s = makeState();
		
		for (int j = 0; j < 8; ++j) {
			Point p = new Point(0, j);
			assertTrue(s.isOccupiedBy(p, Color.BLACK) == (j == 0 || j == 1 || j == 7));
		}
		
		State t = s.take(new Point(0, 1)).take(new Point(0, 7));
		for (int j = 0; j < 8; ++j) {
			Point p = new Point(0, j);
			assertTrue(t.isOccupiedBy(p, Color.BLACK) == (j == 0));
		}
	}
	
	private static <E> Collection<E> makeCollection(Iterable<E> iter) {
	    Collection<E> list = new ArrayList<E>();
	    for (E item : iter) {
	        list.add(item);
	    }
	    return list;
	}

	@Test
	public void testPointsOfField() {
		State s = makeState();
		assertEquals(makeCollection(s.pointsOfField()).size(), 3*8);
	}

	@Test
	public void testNeighborsOf() {
		State s = makeState();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 8; ++j) {
				int n = (j % 2 == 0) ? 2 : (i == 1 ? 4 : 3); 
				assertEquals(makeCollection(s.neighborsOf(new Point(i, j))).size(), n);
			}
		}
	}

	@Test
	public void testLinematesOf() {
//		fail("Not yet implemented");
	}

	@Test
	public void testOnlyFree() {
//		fail("Not yet implemented");
	}

	@Test
	public void testOnlyOccupiedBy() {
//		fail("Not yet implemented");
	}

	@Test
			public void testGetPossibleNextStates() {
		//		fail("Not yet implemented");
			}

	@Test
	public void testIsMill() {
		State s = makeState();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 8; ++j) {
				Point p = new Point(i, j);
				assertEquals(s.isMill(p, Color.BLACK), p.getIndex() == 1);
				assertFalse(s.isMill(p, Color.WHITE));
			}
		}
	}

	@Test
	public void testIsMill2() {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 8; j += 2) {
				State s = new State().occupy(new Point(i,j), Color.WHITE).occupy(new Point(i,j+1), Color.WHITE).occupy(new Point(i,(j+2) % 8), Color.WHITE);
				for (int x = 0; x < 3; ++x) {
					for (int y = 0; y < 8; ++y) {
						assertEquals(s.isMill(new Point(x,y), Color.WHITE), x == i && (y == j || y == j+1 || y == (j+2) % 8));
					}
				}
			}
		}
	}

	@Test
	public void testCountPieces() {
		State s = makeState();
		assertEquals(s.countPieces(Color.BLACK), 7);
		assertEquals(s.countPieces(Color.WHITE), 6);
	}

	@Test
	public void testMayJump() {
		State s = makeState();
		assertFalse(s.mayJump(Color.WHITE));
		assertFalse(s.mayJump(Color.BLACK));
		s = new State();
		assertTrue(s.mayJump(Color.WHITE));
		assertTrue(s.mayJump(Color.BLACK));
		s = s.occupy(new Point(0, 0), Color.BLACK).occupy(new Point(0, 1), Color.BLACK).occupy(new Point(0, 2), Color.BLACK);
		assertTrue(s.mayJump(Color.WHITE));
		assertTrue(s.mayJump(Color.BLACK));
		s = s.occupy(new Point(1, 0), Color.BLACK);
		assertTrue(s.mayJump(Color.WHITE));
		assertFalse(s.mayJump(Color.BLACK));
	}

}
