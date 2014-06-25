package de.rwth_aachen.kbsg.dq;
import java.util.Random;
import java.util.Arrays;
public class RuleBasedAgent extends Player{
	public RuleBasedAgent(Color color) {
		super(color);
	}

	@Override
	public State occupy(State s) {
		Random random = new Random();
		Point p = new Point (random.nextInt(3), random.nextInt(8));
			if(getHalfMill(s, getColor()) != null){
				Point [] halfMill = getHalfMill(s, getColor()); 
				System.out.println( getColor() + " hat eine halbe mühle: " + Arrays.toString(halfMill));
				return s.occupy(halfMill [2], getColor());
			}
			else if(getHalfMill(s, getColor().opponent()) != null){
					Point [] halfMill = getHalfMill(s, getColor().opponent());
					System.out.println( getColor().opponent() + " (Gegner) hat eine halbe mühle: " + Arrays.toString(halfMill));
					return s.occupy(halfMill [2], getColor());
			}
			else {
				for( int k = 8; k > 0; k--){
					for(Point point : s.pointsOfField()){
						int n = countFreeNeighbors(s, point);
						int o = countOwnNeighbors (s, point);
						System.out.println("n = " + n);
						System.out.println("o = " + o);
							if(n + 2*o == k && !s.isOccupied(point)){
								p = point;
								return s.occupy(p, getColor());
							}
						}
					}
				}
		return s.occupy(p, getColor());
		}


	@Override
	public State take(State s) {
		Point p = null;
		if(s.hasOnlyMills(color.opponent()) == true){
			for(int i=1; i<=7; i+=2){
				p = new Point (1, i);
				if(s.isOccupiedBy(p, color.opponent()) == true){
					return s.take(p);
				}
			}
			for(int i=1; i<=7; i+=2){
				p = new Point (0, i);
				if(s.isOccupiedBy(p, color.opponent()) == true){
					return s.take(p);
				}
				p = new Point (2, i);
				if(s.isOccupiedBy(p, color.opponent()) == true){
					return s.take(p);
				}
			}
		}
		else if(getOpenMill(s, color.opponent()) != null){
			for(Point point : getOpenMill(s, color.opponent())){
				if(s.isMill(point, color.opponent()) == false){
					return s.take(point);
				}
			}
		}
		else if(getHalfMill(s, color.opponent()) != null){
			for(Point point : getHalfMill(s, color.opponent())){
				if(s.isMill(point, color.opponent()) == false){
					return s.take(point);
				}
			}
		}
		else{
			for(Point point : s.getOccupations(color.opponent())){
				if(s.isMill(point, color.opponent()) == false){
					return s.take(point);
				}
			}
		}
		return s.take(p);
	}

	@Override
	public State move(State s) {
		Point k = null;
		Point f = null;
		if(getOpenMill(s, color)!= null) {
			Point to = getOpenMill(s, color)[2];
			for(Point p : s.neighborsOf(to)){
				for(Point point : getOpenMill(s, color)){
					if(p.equals(point)){
						Point from = p;
						System.out.println( getColor() + "hat eine offene Mühle." );
						return (s.move(from, to));
					}
				}
			}
		}
		else if(getOpenMill(s, color.opponent()) != null){
			Point p = getOpenMill(s, color.opponent())[2];
			for( Point point : s.neighborsOf(p)){
				if(s.isOccupiedBy(point, color)){
					System.out.println( getColor().opponent() + " hat eine offene Mühle");
					return s.move(point, p);
				}
			}
			
		}
		else{
				for( State possibleNextState : s.getPossibleNextStates(Phase.MOVE, getColor())){
					for( Point p : possibleNextState.getOccupations(getColor())){
						Point [] openMill = getOpenMill(possibleNextState, p);
						if( openMill != null){
							for (Point point : openMill){
								if (s.getOccupancy(point) != possibleNextState.getOccupancy(point)){
									for (Point n : s.neighborsOf(point)){
										if (s.getOccupancy(n) != s.getOccupancy(point)){
											System.out.println(" eine offene Mühle ist möglich");
											return s.move(n, point);
										}
									}
								}
							}
						}
						Point [] halfMill = getHalfMill(possibleNextState, p);
						if (halfMill != null){
							for (Point point : halfMill){
								if (s.getOccupancy(point) != possibleNextState.getOccupancy(point)){
									for (Point n : s.neighborsOf(point)){
										if (s.isOccupiedBy(n, getColor()) && !s.isOccupied(point)){
											System.out.println("eine Halbe Mühle ist möglich" + n + point);
											return s.move(n, point);
										}
									}
								}
							}
						}
					}
				}
			}
			Random random;
			random = new Random();
			f = new Point(random.nextInt(3), random.nextInt(8));
			k = new Point(random.nextInt(3), random.nextInt(8));
			return s.move(f, k);
		}
	/**
	 * tests whether an occupied point is part of a half mill 
	 * @param s the state of the board
	 * @param p the occupied point
	 * @return returns an Array of points containing the first and second point of the half mill
	 * and the empty point that has to be occupied to occupy the whole line.
	 */
	private Point [] getHalfMill(State s, Point p) {
		for (Iterable <Point> line : s.linesOf(p)){
			for (Point point : line){
				Point halfMill1 = null;
				Point missingPoint = null;
				if (s.isOccupiedBy(point,s.getOccupancy(point))){
					halfMill1 = point;
					if (missingPoint != null){
						Point [] halfMill = {p, halfMill1, missingPoint};
						return halfMill;
					}
				}
				if (!s.isOccupied(point)){
					missingPoint = point;
					if (halfMill1 != null){
						Point [] halfMill = {p, halfMill1, missingPoint};
						return halfMill;
					}
				}
			}
			
		}
		return null;
	}

	/**
	 * tests whether a point occupied by a piece is part of an open mill
	 * @param s the state of the board
	 * @param p the occupied point
	 * @return returns an Array of Points containing the first and second point of the open mill
	 * and the missing piece that can close the mill with the next move
	 */
	private Point [] getOpenMill(State s, Point p) {
		for (Iterable <Point> line : s.linesOf(p)){
			for (Point point : line){
				Point openMill1 = null;
				Point missingPiece = null;
				if (! s.isOccupied(point)){
					Point missingPoint = point;
					if (missingPoint.getIndex() % 2 == 1){
						if (countOwnNeighbors(s, missingPoint) >= 3){
							for (Point n : s.onlyOccupiedBy(s.neighborsOf(missingPoint), s.getOccupancy(point))){
								if (!n.equals(p) ){
									if (((missingPoint.getIndex() == p.getIndex() && missingPoint.getFrame() == n.getFrame()) ||(missingPoint.getFrame() == p.getFrame() && n.getIndex() == missingPoint.getIndex()) )){
										missingPiece = n;
										if ( openMill1 != null){
											Point [] openMill = {p, openMill1, missingPiece};
											return openMill;
										}
									}
									else {
										openMill1 = n;
										if (missingPiece != null){
											Point [] openMill = {p, openMill1, missingPiece};
											return openMill;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * method to get the points of a 'half mill'
	 * @param s state of the board
	 * @param pColor color of the half mill you look for
	 * @return returns an Array of points containing the first and second point of the half mill
	 * and the empty point that has to be occupied to fill the complete line.
	 */
	private Point [] getHalfMill(State s, Color pColor) {
		for (Point p : s.getOccupations(pColor)){
			for (Iterable <Point> line : s.linesOf(p)){
				Point halfMill1 = null;
				Point halfMill2 = null;
				Point missingPoint = null;
				for (Point point : line){
					if (halfMill1 == null && s.isOccupiedBy(point, pColor)){
						halfMill1 = point;
					}
					else if (halfMill2 == null && s.isOccupiedBy(point, pColor)){
						halfMill2 = point;
					}
					if(!s.isOccupied(point)) {
						missingPoint = point;
					}
				}
				if (halfMill1 != null && halfMill2 != null && missingPoint != null){
					Point [] halfMill = new Point[3];
					halfMill [0] = halfMill1;
					halfMill [1] = halfMill2;
					halfMill [2] = missingPoint;
					return halfMill;
					
				}
			}
		}
		return 	null;
	}

	/**
	 * method to get the position of an open mill on the board.
	 * @param s the state of the board
	 * @param pColor the color of the mill
	 * @return returns an Array of Points containing the first and second point of the mill,
	 * the empty Point that needs to be occupied and the point with the missing piece.
	 */
	private Point [] getOpenMill(State s, Color pColor) {
		
		Point [] openMill = new Point [4];
		for (Point p : s.getOccupations(pColor)){
			for (Iterable <Point> line : s.linesOf(p)){
				Point openMill1 = null;
				Point openMill2 = null;
				Point missingPoint = null;
				Point missingPiece = null;
				for (Point point : line){
					if(s.getOccupancy(point)== pColor){
						if (openMill1==null){
							openMill1 = point;
						}
						else if (openMill2 == null){
							openMill2 = point;
						}
					}
					else {
						if(s.getOccupancy(point)== pColor.opponent()){
							
						}
						else {
							missingPoint = point;
							for (Point n : s.neighborsOf(point)){
								if (s.getOccupancy(n)== pColor){
									if (!n.equals(openMill1) && !n.equals(openMill2)){
										missingPiece = n;
									}
								}
							}
						}
					}
					if (openMill1 != null && openMill2 != null && missingPoint != null && missingPiece != null){
						openMill [0] = openMill1;
						openMill [1] = openMill2;
						openMill [2] = missingPoint;
						openMill [3] = missingPiece;
						return openMill;
					}
				} 
			}
		}
		return null;
	}
	
	private int countFreeNeighbors ( State pState, Point pPoint){
		int neighbors = 0;
		for (Point nPoint : pState.neighborsOf(pPoint)){
			if (! pState.isOccupied(nPoint)){
				neighbors ++;
			}
		}
		return neighbors;
	}
	private int countOwnNeighbors (State pState, Point pPoint){
		int neighbors = 0;
		for (Point nPoint : pState.neighborsOf(pPoint)){
			if ( pState.isOccupiedBy(nPoint, getColor())){
				neighbors ++;
			}
		}
		return neighbors;
	}
}
