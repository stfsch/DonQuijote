package de.rwth_aachen.kbsg.dq;

public class RuleBasedAgent extends Player{
	private final UI ui;
	private Color otherColor;
	public RuleBasedAgent(Color color, UI ui) {
		super(color);
		this.ui = ui;
		if(color == Color.WHITE){
			otherColor = Color.BLACK;
		}
		else{
			otherColor = Color.WHITE;
		}
	}

	@Override
	public State occupy(State s) {
		if(isHalfMill(color) == true){
			 Point p;
			 p = s.getMissingPoint(getHalfMill(color));
			 return s.occupy(p, color);
		}
		else if(isHalfMill(otherColor)){
			p = getMissingPoint(getHalfMill(otherColor));
			 return s.occupy(p, color);
		}
		else{
			int k;
			for(int i = 4; i!=k; i--)
				for(Point point : s.getOccupations(color)){
					for(Point p : s.neighborsOf(point)){
						k = 0;
						if(s.getOccupancy(p) == null){
							k++;
						}
					}
				}
				return s.occupy(p, color);
			}
		}

	@Override
	public State take(State s) {
		if(s.hasOnlyMills(otherColor) == true){
			Point p;
			for(int i=1; i<=7; i+=2){
				p = new Point (1, i);
				if(s.isOccupiedBy(p, otherColor) == true){
					return s.take(p);
				}
			}
			for(int i=1; i<=7; i+=2){
				p = new Point (0, i);
				if(s.isOccupiedBy(p, otherColor) == true){
					return s.take(p);
				}
				p = new Point (2, i);
				if(s.isOccupiedBy(p, otherColor) == true){
					return s.take(p);
				}
			}
		}
		else if(isOpenMill(otherColor) == true){
			for(Point point : getOpenMill(otherColor)){
				if(s.isMill(point, otherColor) == false){
					return s.take(point);
				}
			}
		}
		else if(isHalfMill(otherColor) == true){
			for(Point point : getHalfMill(otherColor)){
				if(s.isMill(point, otherColor) == false){
					return s.take(point);
				}
			}
		}
		else{
			for(Point point : s.getOccupations(otherColor)){
				if(s.isMill(point, otherColor) == false){
					return s.take(point);
				}
			}
		}
	}

	@Override
	public State move(State s) {
		if(isOpenMill(color)== true) {
			Point to = getMissingPoint(getOpenMill(color));
			for(Point p : s.neighborsOf(to)){
				for(Point point : getOpenMill(color)){
					if(p.equals(point)){
						Point from = p;
						return (s.move(from, to));
					}
				}
			}
		}
		else if(isOpenMill(otherColor) == true){
			Point p = getMissingPoint(getOpenMill(otherColor));
			for( Point point : s.neighborsOf(p)){
				if(s.isOccupiedBy(point, color)){
					return s.move(point, p);
				}
			}
			
		}
		else{
				for( State possibleNextState : s.getPossibleNextStates(Phase.MOVE, color)){
					for( Point p : possibleNextState.getOccupations(color)){
						if(possibleNextState.isOpenMill(p)|| possibleNextState.isHalfMill(p)){
							//zug ermitteln fehlt noch.
						}
					}
				}
			}
		}
	
	private Object getHalfMill(Color otherColor2) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object getOpenMill(Color otherColor2) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean isOpenMill(Color otherColor2) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isHalfMill(Color otherColor2) {
		// TODO Auto-generated method stub
		return false;
	}

	private Point getMissingPoint(Object openMill) {
		// TODO Auto-generated method stub
		return null;
	}
}
