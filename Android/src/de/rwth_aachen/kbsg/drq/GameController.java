package de.rwth_aachen.kbsg.drq;

import de.rwth_aachen.kbsg.dq.Color;
import de.rwth_aachen.kbsg.dq.Game;
import de.rwth_aachen.kbsg.dq.Human;
import de.rwth_aachen.kbsg.dq.Player;
import de.rwth_aachen.kbsg.dq.Point;
import de.rwth_aachen.kbsg.dq.State;
import de.rwth_aachen.kbsg.dq.StateMachine;
import de.rwth_aachen.kbsg.dq.UI;

public class GameController implements FieldView.Listener, ConfigButton.Listener, UI {
	private FieldView fw;
	
	private Game game;
	private Thread thread;
	private Player white;
	private Player black;
	
	public GameController(FieldView fw) {
		this.fw = fw;
		onPlayerSelected(new Human(Color.WHITE, this));
		onPlayerSelected(new Human(Color.BLACK, this));
	}

	@Override
	public Point inputPoint(Color c) {
		return fw.waitForSelection();
	}

	@Override
	public Player inputPlayer(Color c) {
		return c == Color.WHITE ? white : black;
	}

	@Override
	public void stateMachineChanged(StateMachine stateMachine) {
		fw.updateStateMachine(stateMachine);
	}

	@Override
	public void illegalMove(Color active, StateMachine stateMachine, State newState) {
		Util.showToast(fw, "Illegal move by "+ stateMachine.getActiveColor().name());
	}

	@Override
	public void gameWon(Color winner) {
		Util.showToast(fw, winner.name() +" won!");
	}

	@Override
	public void gameDrawn() {
		Util.showToast(fw, "Draw!");
	}

	@Override
	public void onPrepareNewGame() {
		white = null;
		black = null;
		fw.updateStateMachine(new StateMachine());
	}

	@Override
	public void onHumanSelected(Color c) {
		Player p = new Human(c, this);
		onPlayerSelected(p);
	}

	@Override
	public void onAgentSelected(Player p) {
		onPlayerSelected(p);
	}
	
	private void onPlayerSelected(Player p) {
		if (p.getColor() == Color.WHITE) {
			white = p;
		} else if (p.getColor() == Color.BLACK) {
			black = p;
		}
		if (white != null && black != null) {
			if (thread != null) {
				thread.interrupt();
			}
			game = new Game(this);
			thread = new Thread() {
				@Override
				public void run() {
					try {
						game.play();
					} catch (GameInterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();
		}
	}

	@Override
	public void onPointSelected(Point p) {
	}

	@Override
	public void onPointUnselected(Point p) {
	}
}
