package de.rwth_aachen.kbsg.dq;

public class HelloWorld {
	public static void main(String[] args) {
		UI ui = new TUI();
		Game game =new Game(ui, new Human(Color.WHITE, ui), new Human(Color.BLACK, ui));
		game.play();
	}

}
