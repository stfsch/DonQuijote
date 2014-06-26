package de.rwth_aachen.kbsg.drq;

import de.rwth_aachen.kbsg.dq.Color;
import de.rwth_aachen.kbsg.dq.MiniMaxAgent;
import de.rwth_aachen.kbsg.dq.Player;
import de.rwth_aachen.kbsg.dq.RandomAgent;
import de.rwth_aachen.kbsg.dq.RuleBasedAgent;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class ConfigButton extends Button implements View.OnClickListener {
	public static interface Listener {
		public void onPrepareNewGame();
		public void onHumanSelected(Color c);
		public void onAgentSelected(Player p);
	}
	
	public static class Adapter implements Listener {
		@Override
		public void onPrepareNewGame() {
		}
		
		@Override
		public void onHumanSelected(Color c) {
		}

		@Override
		public void onAgentSelected(Player p) {
		}
	}

	private Listener listener = new Adapter();

	public ConfigButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	public Listener getListener() {
		return listener;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		listener.onPrepareNewGame();
		// reverse order, because the second popup menu is layover of the first
		showPlayerMenu(Color.BLACK);
		showPlayerMenu(Color.WHITE);
		// that should be the final proof that I'm the king of GUI
	}
	
	private void showPlayerMenu(final Color c) {
		final int HUMAN = 0;
		final int RANDOM = 1;
		final int RULE_BASED = 2;
		final int MINIMAX = 3;
		((Activity) getContext()).runOnUiThread(new Runnable() {
			public void run() {
		        PopupMenu popupMenu = new PopupMenu(getContext(), ConfigButton.this);
		        popupMenu.getMenu().add(Menu.NONE, HUMAN, Menu.NONE, "Human");
		        popupMenu.getMenu().add(Menu.NONE, RANDOM, Menu.NONE, "Random");
		        popupMenu.getMenu().add(Menu.NONE, RULE_BASED, Menu.NONE, "Rule-based");
		        SubMenu m = popupMenu.getMenu().addSubMenu(Menu.NONE, MINIMAX, Menu.NONE, "Minimax");
		        
		        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						case HUMAN:
							listener.onHumanSelected(c);
							break;
						case RANDOM:
							listener.onAgentSelected(new RandomAgent(c));
							break;
						case RULE_BASED:
							listener.onAgentSelected(new RuleBasedAgent(c));
							break;
						case MINIMAX:
							listener.onAgentSelected(new MiniMaxAgent(c, 0, null));
							break;
						default:
							throw new IllegalStateException();
						}
						return true;
					}
				});
		        popupMenu.show();
			}
		});
	}
}
