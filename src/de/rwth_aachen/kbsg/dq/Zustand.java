package de.rwth_aachen.kbsg.dq;

public class Zustand {
	public currentZustand;
	protected boolean isMühle;
	public int steineSchwarz;
	public int steineWeiss;
	protected Liste <Zustand> erreichbareZuständeZiehen (farbe);
	protected Liste <Zustand> erreichbareZuständeSetzen(farbe);
	protected Liste <Zustand> erreichbareZuständeWegnehmen(farbe);
	public int getAnzahlSteine(String farbe);
		switch(farbe){
		case(""):return steineWeiss+steineSchwarz;
		break;
		case("weiss"): return steineWeiss;
		break;
		case("schwarz"): return steineSchwarz;
		break;
		default();
		break;
		}
	}
}