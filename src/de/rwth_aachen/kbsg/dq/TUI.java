package de.rwth_aachen.kbsg.dq;

public class TUI {
	public void printState(State currentState){
		Spot[][] occupancy=currentState.getOccupancy();
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
}

