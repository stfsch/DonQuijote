package de.rwth_aachen.kbsg.dq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TUI extends UI {
	
	@Override
	public void showState(State pState){
		Spot[][] occupancy=pState.getOccupancy();
		char[][] c= new char[3][8];
		for(int i=0; i<3; i++){
			for(int j=0;j<8;j++){
				c[i][j] = occupancy[i][j].spotToChar();//wandelt Spot in Char um
			}
		}
		//System.out.println(System.console());	
		System.out.println(c[0][0] +"-----------"+c[0][1] +"-----------"+c[0][2]);
		System.out.println("|           |           |");
		System.out.println("|   "+c[1][0]+"-------"+c[1][1] +"-------"+c[1][2] +"   |");
		System.out.println("|   |       |       |   |");
		System.out.println("|   |   "+c[2][0] +"---"+c[2][1] +"---"+c[2][2] +"   |   |");
		System.out.println("|   |   |       |   |   |");
		System.out.println(c[0][7] +"---"+c[1][7] +"---"+c[2][7] +"       "+c[2][3] +"---"+c[1][3] +"---"+c[0][3]);
		System.out.println("|   |   |       |   |   |");
		System.out.println("|   |   "+c[2][6] +"---"+c[2][5] +"---"+c[2][4] +"   |   |");
		System.out.println("|   |       |       |   |");
		System.out.println("|   "+c[1][6] +"-------"+c[1][5] +"-------"+c[1][4] +"   |");
		System.out.println("|           |           |");
		System.out.println(c[0][6] +"-----------"+c[0][5] +"-----------"+c[0][4]);					
	}

	/* (nicht-Javadoc)
	 * @see de.rwth_aachen.kbsg.dq.UI#getSpot(de.rwth_aachen.kbsg.dq.Spot)
	 */
	@Override
	public int[] getSpot(Spot pActivePlayerColour) {
		System.out.println("phase=setzen." + pActivePlayerColour.name() + " ist dran.");
						
		BufferedReader in = new BufferedReader(new InputStreamReader( System.in ));
		
		int[] spot = new int[2];		
		boolean correctSpot = false;
		while(!correctSpot){
			try {			
				System.out.println("Waehle einen Rahmen(0, 1 oder 2, von außen nach innen).");
				spot[0] = Integer.parseInt(in.readLine());
				if (spot[0] > 2){
					throw new IncorrectPositionException("Rahmen muss zwischen 0 und 2 liegen. Bitte neu eingeben:");
				}	
				System.out.println("Waehle eine Position (zwischen 0 und 7)");				
				spot[1] = Integer.parseInt(in.readLine());
				if (spot[1] > 7){
					throw new IncorrectPositionException("Position muss zwischen 0 und 7 liegen. Bitte neu eingeben:");
				}
				correctSpot = true;

			} catch (IOException | IncorrectPositionException e) {
				System.out.println(e.getMessage());
			}
		}
			
		
		return spot;
		
		
	}
	
	/* (nicht-Javadoc)
	 * @see de.rwth_aachen.kbsg.dq.UI#incorrectMove(de.rwth_aachen.kbsg.dq.Spot)
	 */
	public int[] incorrectMove(Spot activePlayer) {
		System.out.println("Zug ungültig.");
		int[] spot = null;
		spot = getSpot(activePlayer);		
		return spot;
	}
}

