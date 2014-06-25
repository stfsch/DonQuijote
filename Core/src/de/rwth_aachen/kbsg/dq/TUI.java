package de.rwth_aachen.kbsg.dq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TUI implements UI {
	
	public void showState(State pState){
		char[][] cs= new char[3][8];
		for (Point p : pState.pointsOfField()) {
			char c;
			if (pState.isOccupiedBy(p, Color.BLACK)) {
				c = 'X';
			} else if (pState.isOccupiedBy(p, Color.WHITE)) {
				c = 'O';
			} else {
				c = '+';
			}
			cs[p.getFrame()][p.getIndex()] = c;
		}
		//System.out.println(System.console());	
		System.out.println(cs[0][0] +"-----------"+cs[0][1] +"-----------"+cs[0][2]);
		System.out.println("|           |           |");
		System.out.println("|   "+cs[1][0]+"-------"+cs[1][1] +"-------"+cs[1][2] +"   |");
		System.out.println("|   |       |       |   |");
		System.out.println("|   |   "+cs[2][0] +"---"+cs[2][1] +"---"+cs[2][2] +"   |   |");
		System.out.println("|   |   |       |   |   |");
		System.out.println(cs[0][7] +"---"+cs[1][7] +"---"+cs[2][7] +"       "+cs[2][3] +"---"+cs[1][3] +"---"+cs[0][3]);
		System.out.println("|   |   |       |   |   |");
		System.out.println("|   |   "+cs[2][6] +"---"+cs[2][5] +"---"+cs[2][4] +"   |   |");
		System.out.println("|   |       |       |   |");
		System.out.println("|   "+cs[1][6] +"-------"+cs[1][5] +"-------"+cs[1][4] +"   |");
		System.out.println("|           |           |");
		System.out.println(cs[0][6] +"-----------"+cs[0][5] +"-----------"+cs[0][4]);					
	}

	@Override
	public Point inputPoint(Color pActivePlayerColour) {
		System.out.println(pActivePlayerColour.name() + " ist dran.");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				System.out.println("Waehle einen Rahmen (0, 1 oder 2, von außen nach innen).");
				int frame = Integer.parseInt(in.readLine());
				if (frame > 2) {
					System.out.println("Rahmen muss zwischen 0 und 2 liegen. Bitte neu eingeben:");
					continue;
				}	
				System.out.println("Waehle eine Position (zwischen 0 und 7)");				
				int index = Integer.parseInt(in.readLine());
				if (index > 7) {
					System.out.println("Position muss zwischen 0 und 7 liegen. Bitte neu eingeben:");
					continue;
				}
				return new Point(frame, index);
			} catch (IOException | NumberFormatException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	@Override
	public void phaseChanged(Phase phase, Color color) {
		System.out.println("Phase ist "+ phase.name() +" für "+ color.name() +".");
	}

	@Override
	public void stateChanged(State state) {
		showState(state);
	}
	
	@Override
	public void illegalMove(Color active, State state, State newState) {
		System.out.println("Zug von "+ active.name() +" ungültig.");
	}
	
	@Override
	public void gameWon(Color winner) {
		System.out.println("Spieler "+ winner.name() +" hat gewonnen.");
	}
	
	@Override
	public void gameDrawn() {
		System.out.println("Unentschieden.");
	}

	@Override
	public Player inputPlayer(Color pColor) {
		Player player= null;
		System.out.println("Wähle einen Spielertypen. 1 = Mensch, 2 = Zufallsspieler, 3 = regel-basierter Spieler, 4 = heuristic Agent");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (player == null){
			try {
				int choice = Integer.parseInt(in.readLine());
				switch (choice){
				case 1:
					player = new Human (pColor, this);
					break;
				case 2:
					player = new RandomAgent (pColor);
					break;
				case 3:
					player = new RuleBasedAgent (pColor);
					break;
				case 4:
					player = new HeuristicAgent (pColor);
					break;
				default:
					System.out.println("Unzulässige Wahl, bitte neu eingeben");
				}
			}
			catch (NumberFormatException |IOException e) {
				System.out.println(e.getMessage());
			}
		}
		return player;
	}
}
