package de.rwth_aachen.kbsg.dq;

public class Game {
private Spieler spielerWeiss;
private Spieler spielerSchwarz;
protected String phase= "setzen";
Zustand currentZustand=Zustand.getZustand();

public void play(){
while(phase!="ende"){
	int steine=Zustand.getAnzahlSteine("");
	int steineSchwarz=Zustand.getAnzahlSteine("schwarz");
	int steineWeiss=Zustand.getAnzahlSteine("weiss");
	if(steine==18){
		phase="ziehen";
	}
	else if(phase=="ziehen"&&steineWeiss==3){
		phase="springenWeiss";
		if(steineSchwarz==3){
			phase="springen";
		}
	}
	else if(phase=="ziehen"&&steineSchwarz==3){
		phase="springenSchwarz";
	}
	else if(phase=="springen"&&steineSchwarz<3||steineWeiss<3){
		phase="ende";
	}
	spielerWeiss.nextMove(currentZustand, phase);
	
}
}
}
