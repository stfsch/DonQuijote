package de.rwth_aachen.kbsg.dq;

public class Game {
	private Player playerWhite;
	private Player playerBlack;
	public String phase;
	private State currentState;

	public Game(){
		playerWhite= new Player();
		playerBlack= new Player();
		phase= "setzen";
		currentState=new State();
	}
	
	private boolean prüfeZug(State newState, Spot colour){
		State[]possibleNextStates=newState.getPossibleNextStates(colour, newState, phase);
		int k=0;
		for(int i=0; i<57;i++){
			State possibleState=possibleNextStates[i];
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
		while(phase!="ende"){
			int men=currentState.countMen("");
			int menBlack=currentState.countMen("black");
			int menWhite=currentState.countMen("white");
			if(men==18){
				phase="ziehen";
			}
	
			else if(phase=="ziehen"&&menWhite==3){
				phase="springenWhite";
				if(menBlack==3){
					phase="springen";
				}
			}
			else if(phase=="ziehen"&&menBlack==3){
				phase="springenBlack";
			}
			else if(phase=="springen"&&menBlack<3||menWhite<3){
				phase="ende";
			}
			System.out.println("Weiß ist dran.");
			State zug=playerWhite.nextMove();
			prüfeZug(zug,"white");
			
		}
	}
}
