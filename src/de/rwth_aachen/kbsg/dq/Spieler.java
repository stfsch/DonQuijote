package de.rwth_aachen.kbsg.dq;

public class Spieler {
protected String farbe;
public Zustand nextMove(int steinRahmen, int steinPosition, int wunschRahmen, int wunschPosition, Zustand currentZustand){
	Zustand newZustand = new Zustand();
	newZustand=currentZustand;
	String[][]belegung=newZustand.getBelegung();
	belegung[steinRahmen][steinPosition]="empty";
	belegung[wunschRahmen][wunschPosition]=farbe;
	return newZustand;
	}

}
