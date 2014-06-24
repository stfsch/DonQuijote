package de.rwth_aachen.kbsg.dq;

public class Main {
	public static void main(String[] args) {
		UI ui = new TUI();
		
		Game game =new Game(ui);
		game.play();
	}

}
