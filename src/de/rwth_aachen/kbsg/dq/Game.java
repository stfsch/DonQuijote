package de.rwth_aachen.kbsg.dq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
	private TUI tui;
	private Player playerWhite;
	private Player playerBlack;
	public Phase phase;
	private State currentState;

	public Game(){
		tui= new TUI();
		playerWhite= new Player();
		playerWhite.colour=Spot.WHITE;
		playerBlack= new Player();
		playerBlack.colour=Spot.BLACK;
		phase= Phase.SETZEN;
		currentState=new State();
	}
	
	private int stringToInt(String s){
		int i=Integer.parseInt(s);
		return i;
	}
	
	private boolean prüfeZug(State newState, Spot colour){
		State[] possibleNextStates = currentState.getPossibleNextStates(colour, phase.name());
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
			int men=currentState.countMen("");
			int menBlack=currentState.countMen("black");
			int menWhite=currentState.countMen("white");
			if(men==18){
				phase=Phase.ZIEHEN;
				System.out.println("all men have been placed, switching to moving, phase = " + phase.name());
			}
	
			else if(phase==Phase.ZIEHEN && menWhite==3){
				phase=Phase.SPRINGENWHITE;
				System.out.println("white only has three men left, phase = " + phase.name());
				if(menBlack==3){
					phase=Phase.SPRINGENBOTH;
					System.out.println("black also only has three men left, phase = " + phase.name());
				}
			}
			else if(phase==Phase.ZIEHEN && menBlack==3){
				phase=Phase.SPRINGENBLACK;
				System.out.println("black only has three men left, phase = " + phase.name());
			}
			else if (phase!=Phase.SETZEN && (menBlack<3 || menWhite<3)){
				phase=Phase.ENDE;
				System.out.println("game is over, phase = " + phase.name());
			}
			tui.printState(currentState);
			State zug;
			System.out.println("phase = "+phase.name() +"\n");
			if (phase==Phase.SETZEN) {
				System.out.println("phase=setzen. Weiß ist dran.");
				System.out.println("Waehle einen Rahmen(0, 1 oder2, von außen nach innen).");
				BufferedReader in = new BufferedReader(new InputStreamReader( System.in ));
				String wR="";
				try {
					wR = in.readLine();
				} catch (IOException e) {
					// TODO Automatisch generierter Erfassungsblock
					System.out.println(e.getMessage());
				}
				int wunschRahmen=stringToInt(wR);
				System.out.println("Waehle eine Position (zwischen 0 und 7)");
				String wP = "";
				try {
					wP = in.readLine();
				} catch (IOException e) {
					// TODO Automatisch generierter Erfassungsblock
					System.out.println(e.getMessage());
				}
				int wunschPosition=stringToInt(wP);
				zug=playerWhite.placeNextMan(wunschRahmen, wunschPosition, currentState);
				while(prüfeZug(zug,Spot.WHITE)==false){
					System.out.println("Zug ungültig.");
					System.out.println("Waehle einen Rahmen(0, 1 oder2, von außen nach innen).");
					try {
						wR = in.readLine();
					} catch (IOException e) {
						// TODO Automatisch generierter Erfassungsblock
						System.out.println(e.getMessage());
					}
					wunschRahmen=stringToInt(wR);
					System.out.println("Waehle eine Position (zwischen 0 und 7)");
					try {
						wP = in.readLine();
					} catch (IOException e) {
						// TODO Automatisch generierter Erfassungsblock
						System.out.println(e.getMessage());
					}
					wunschPosition=stringToInt(wP);
					zug=playerWhite.placeNextMan(wunschRahmen, wunschPosition,currentState);
				}
				currentState=zug;
				tui.printState(currentState);
				System.out.println("Schwarz ist dran.");
				System.out.println("Waehle einen Rahmen(0, 1 oder2, von außen nach innen).");
				try {
					wR = in.readLine();
				} catch (IOException e) {
					// TODO Automatisch generierter Erfassungsblock
					System.out.println(e.getMessage());
				}
				wunschRahmen=stringToInt(wR);
				System.out.println("Waehle eine Position (zwischen 0 und 7)");
				try {
					wP = in.readLine();
				} catch (IOException e) {
					// TODO Automatisch generierter Erfassungsblock
					System.out.println(e.getMessage());
				}
				wunschPosition=stringToInt(wP);
				zug=playerBlack.placeNextMan(wunschRahmen, wunschPosition,currentState);
				while(prüfeZug(zug,Spot.BLACK)==false){
					System.out.println("Zug ungültig.");
					System.out.println("Waehle einen Rahmen(0, 1 oder2, von außen nach innen).");
					try {
						wR = in.readLine();
					} catch (IOException e) {
						// TODO Automatisch generierter Erfassungsblock
						System.out.println(e.getMessage());
					}
					wunschRahmen=stringToInt(wR);
					System.out.println("Waehle eine Position (zwischen 0 und 7)");
					try {
						wP = in.readLine();
					} catch (IOException e) {
						// TODO Automatisch generierter Erfassungsblock
						System.out.println(e.getMessage());
					}
					wunschPosition=stringToInt(wP);
					zug=playerBlack.placeNextMan(wunschRahmen, wunschPosition,currentState);
				}
				currentState=zug;
				tui.printState(currentState);
			}
			if(phase==Phase.ZIEHEN||phase==Phase.SPRINGENBOTH||phase==Phase.SPRINGENWHITE||phase==Phase.SPRINGENBLACK){
				System.out.println("phase=" + phase.name());
				Phase lastPhase=phase;
				System.out.println("Weiß ist dran.");
				System.out.println("Waehle den Rahmen(0, 1 oder2, von außen nech innen) des Steins, der bewegt werden soll.");
				String mR=System.console().readLine();
				int manRahmen=stringToInt(mR);
				System.out.println("Waehle die Position (zwischen 0 und 7), auf der der Stein sich befindet.");
				String mP=System.console().readLine();
				int manPosition=stringToInt(mP);
				System.out.println("Waehle den Rahmen(0, 1 oder2, von außen nech innen), auf den der Stein soll.");
				String wR=System.console().readLine();
				int wunschRahmen=stringToInt(wR);
				System.out.println("Waehle die Position (zwischen 0 und 7), auf die der Stein soll.");
				String wP=System.console().readLine();
				int wunschPosition=stringToInt(wP);
				zug=playerWhite.nextMove(manRahmen, manPosition, wunschRahmen, wunschPosition,currentState);
				while(prüfeZug(zug,Spot.WHITE)==false){
					System.out.println("Zug ungültig.");
					zug=playerWhite.nextMove(manRahmen, manPosition, wunschRahmen, wunschPosition,currentState);
				}
				currentState=zug;
				if(currentState.isMühle(wunschRahmen, wunschPosition, Spot.WHITE)==true){
					phase=Phase.WEGNEHMEN;
					System.out.println("Weiß hat eine Mühle geschlossen. Entferne einen gegnerischen Stein.");
					System.out.println("Waehle den Rahmen(0, 1 oder2, von außen nach innen), auf dem der Stein steht, der weg soll.");
					mR=System.console().readLine();
					manRahmen=stringToInt(mR);
					System.out.println("Waehle die Position (zwischen 0 und 7)auf der der Stein steht");
					mP=System.console().readLine();
					manPosition=stringToInt(mP);
					zug=playerWhite.removeMan(manRahmen, manPosition,currentState);
					while(prüfeZug(zug, Spot.WHITE)==false){
						System.out.println("Zug ungültig.");
						System.out.println("Waehle den Rahmen(0, 1 oder2, von außen nach innen), auf dem der Stein steht, der weg soll.");
						mR=System.console().readLine();
						manRahmen=stringToInt(mR);
						System.out.println("Waehle die Position (zwischen 0 und 7)auf der der Stein steht");
						mP=System.console().readLine();
						manPosition=stringToInt(mP);
						zug=playerWhite.removeMan(manRahmen, manPosition,currentState);
					}
					currentState=zug;
					tui.printState(currentState);
					phase=lastPhase;
				}
				System.out.println("Schwarz ist dran.");
				System.out.println("Waehle den Rahmen(0, 1 oder2, von außen nech innen) des Steins, der bewegt werden soll.");
				mR=System.console().readLine();
				manRahmen=stringToInt(mR);
				System.out.println("Waehle die Position (zwischen 0 und 7), auf der der Stein sich befindet.");
				mP=System.console().readLine();
				manPosition=stringToInt(mP);
				System.out.println("Waehle den Rahmen(0, 1 oder2, von außen nech innen), auf den der Stein soll.");
				wR=System.console().readLine();
				wunschRahmen=stringToInt(wR);
				System.out.println("Waehle die Position (zwischen 0 und 7), auf die der Stein soll.");
				wP=System.console().readLine();
				wunschPosition=stringToInt(wP);
				zug=playerBlack.nextMove(manRahmen, manPosition, wunschRahmen, wunschPosition,currentState);
				while(prüfeZug(zug,Spot.BLACK)==false){
					System.out.println("Zug ungültig.");
					System.out.println("Waehle den Rahmen(0, 1 oder2, von außen nech innen) des Steins, der bewegt werden soll.");
					mR=System.console().readLine();
					manRahmen=stringToInt(mR);
					System.out.println("Waehle die Position (zwischen 0 und 7), auf der der Stein sich befindet.");
					mP=System.console().readLine();
					manPosition=stringToInt(mP);
					System.out.println("Waehle den Rahmen(0, 1 oder2, von außen nech innen), auf den der Stein soll.");
					wR=System.console().readLine();
					wunschRahmen=stringToInt(wR);
					System.out.println("Waehle die Position (zwischen 0 und 7), auf die der Stein soll.");
					wP=System.console().readLine();
					wunschPosition=stringToInt(wP);
					zug=playerBlack.nextMove(manRahmen, manPosition, wunschRahmen, wunschPosition,currentState);
				}
				currentState=zug;
				if(currentState.isMühle(wunschRahmen, wunschPosition,Spot.BLACK)==true){
					phase=Phase.WEGNEHMEN;
					System.out.println("Schwarz hat eine Mühle geschlossen. Entferne einen gegnerischen Stein.");
					System.out.println("Waehle den Rahmen(0, 1 oder2, von außen nach innen), auf dem der Stein steht, der weg soll.");
					mR=System.console().readLine();
					manRahmen=stringToInt(mR);
					System.out.println("Waehle die Position (zwischen 0 und 7)auf der der Stein steht");
					mP=System.console().readLine();
					manPosition=stringToInt(mP);
					zug=playerBlack.removeMan(manRahmen, manPosition,currentState);
					while(prüfeZug(zug, Spot.BLACK)==false){
						System.out.println("Zug ungültig.");
						System.out.println("Waehle den Rahmen(0, 1 oder2, von außen nach innen), auf dem der Stein steht, der weg soll.");
						mR=System.console().readLine();
						manRahmen=stringToInt(mR);
						System.out.println("Waehle die Position (zwischen 0 und 7)auf der der Stein steht");
						mP=System.console().readLine();
						manPosition=stringToInt(mP);
						zug=playerBlack.removeMan(manRahmen, manPosition,currentState);
					}
					currentState=zug;
					tui.printState(currentState);
					phase=lastPhase;
				}
			}
		}
	}
}
