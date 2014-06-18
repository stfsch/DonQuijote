package de.rwth_aachen.kbsg.dq;


public class Game {
	private TUI tui;
	private Player playerWhite;
	private Player playerBlack;
	public Phase phase;
	private State currentState;
	public Colour colour;
	public Player player;
	public Spot activePlayer;
	
	public Game(){
		tui= new TUI();
		playerWhite= new Player();
		playerWhite.colour=Spot.WHITE;
		playerBlack= new Player();
		playerBlack.colour=Spot.BLACK;
		phase= Phase.SETZEN;
		currentState=new State();
		colour = Colour.WHITE;
		activePlayer = Spot.WHITE;
		player = playerWhite;
	}
	
	private boolean prüfeZug(State newState, Spot colour){
		State[] possibleNextStates = currentState.getPossibleNextStates(colour, phase);
		int k=0;
		for(State possibleState : possibleNextStates){
			if(newState.isSameAs(possibleState)==true){
				k=1;
			}
		}
		if(k==1){
			return true;
		}
		else{
			return false;
		}

	}
	public void play(){
		System.out.println("entering play, phase = " + phase.name());
		while(phase!=Phase.ENDE){
			System.out.println("still playing, phase = " + phase.name());
			int men=currentState.countMen(Colour.BOTH);
			int menBlack=currentState.countMen(Colour.BLACK);
			int menWhite=currentState.countMen(Colour.WHITE);
			if(men==18){
				phase=Phase.ZIEHEN;
				System.out.println("all men have been placed, switching to moving, phase = " + phase.name());
			}
	
			else if(phase==Phase.ZIEHEN && menWhite==3){
				phase=Phase.SPRINGENWHITE;
				System.out.println("white has only three men left, phase = " + phase.name());
				if(menBlack==3){
					phase=Phase.SPRINGENBOTH;
					System.out.println("black also only has three men left, phase = " + phase.name());
				}
			}
			else if(phase==Phase.ZIEHEN && menBlack==3){
				phase=Phase.SPRINGENBLACK;
				System.out.println("black has only three men left, phase = " + phase.name());
			}
			else if (phase!=Phase.SETZEN && (menBlack<3 || menWhite<3)){
				phase=Phase.ENDE;
				System.out.println("game is over, phase = " + phase.name());
			}
			tui.showState(currentState);
			State zug;
			System.out.println("phase = "+phase.name() +"\n");

			if (phase==Phase.SETZEN) {
				
				boolean correctSpot = false;
				int[] spot = null;
				spot = tui.getSpot(activePlayer);
				correctSpot = true;				
				zug=player.placeNextMan(spot, currentState);
				while(prüfeZug(zug,activePlayer)==false||zug.isMühle(spot[0], spot[1], activePlayer)){
					spot = tui.incorrectMove(activePlayer);
					zug=player.placeNextMan(spot,currentState);
				}
				currentState=zug;
				tui.showState(currentState);
				spielerwechsel();
											
			}
			
			if(phase==Phase.ZIEHEN||phase==Phase.SPRINGENBOTH||phase==Phase.SPRINGENWHITE||phase==Phase.SPRINGENBLACK){
				System.out.println("phase=" + phase.name());
				Phase lastPhase=phase;
				boolean correctSpot = false;
				int[] spot = null;
				int[] newSpot = null;
				System.out.println("welcher Stein soll bewegt werden?");
				spot = tui.getSpot(activePlayer);
				System.out.println("wohin soll der Stein gesetzt werden?");
				newSpot =tui.getSpot(activePlayer);
				correctSpot = true;				
				zug=player.nextMove(spot,newSpot, currentState);
				while(prüfeZug(zug,activePlayer)==false){
					spot = tui.incorrectMove(activePlayer);
					zug=player.nextMove(spot,newSpot,currentState);
				}
				currentState=zug;
				tui.showState(currentState);
				if(currentState.isMühle(newSpot[0], newSpot[1],activePlayer)==true){
					phase=Phase.WEGNEHMEN;
					System.out.println(activePlayer.name()+" hat eine Mühle geschlossen. Entferne einen gegnerischen Stein.");
					System.out.println("Welcher Stein soll weg?");
					correctSpot = false;
					spot = null;
					spot = tui.getSpot(activePlayer);
					correctSpot = true;				
					zug=player.removeMan(spot, currentState);
					while(prüfeZug(zug,activePlayer)==false){
						spot = tui.incorrectMove(activePlayer);
						zug=player.removeMan(spot,currentState);
					}
					currentState=zug;
					tui.showState(currentState);
					phase=lastPhase;
					spielerwechsel();
				}
				
			}
		}
	}

	public void spielerwechsel() {
		if(activePlayer==Spot.WHITE){
		activePlayer = Spot.BLACK;
		player = playerBlack;
		}
		else{
			activePlayer = Spot.WHITE;
			player = playerWhite;
		}
	}


}
