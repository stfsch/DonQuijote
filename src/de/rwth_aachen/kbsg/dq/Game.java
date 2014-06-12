package de.rwth_aachen.kbsg.dq;

public class Game {
private Spieler spielerWhite;
private Spieler spielerBlack;
protected String phase;
private Zustand currentZustand;
public Game(){
spielerWhite= new Spieler();
spielerBlack= new Spieler();
phase= "setzen";
currentZustand=new Zustand();
}
private Zustand prüfeZug(Zustand newZustand, String farbe){
	
	currentZustand
	return 
}
public void play(){
while(phase!="ende"){
	int steine=currentZustand.getAnzahlSteine("");
	int steineSchwarz=currentZustand.getAnzahlSteine("black");
	int steineWeiss=currentZustand.getAnzahlSteine("white");
	if(steine==18){
		phase="ziehen";
		}
	
	else if(phase=="ziehen"&&steineWeiss==3){
		phase="springenWhite";
		if(steineSchwarz==3){
			phase="springen";
		}
	}
	else if(phase=="ziehen"&&steineSchwarz==3){
		phase="springenBlack";
	}
	else if(phase=="springen"&&steineSchwarz<3||steineWeiss<3){
		phase="ende";
	}
	System.out.println("Weiß ist dran.");
	Zustand zug=spielerWhite.nextMove();
	prüfeZug(zug,"white");
	
	
	
}
}
}
