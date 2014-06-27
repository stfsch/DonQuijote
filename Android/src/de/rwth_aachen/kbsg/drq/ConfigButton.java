package de.rwth_aachen.kbsg.drq;

import java.util.Vector;

import de.rwth_aachen.kbsg.dq.Color;
import de.rwth_aachen.kbsg.dq.Heuristic;
import de.rwth_aachen.kbsg.dq.Heuristic1;
import de.rwth_aachen.kbsg.dq.Heuristic2;
import de.rwth_aachen.kbsg.dq.MiniMaxAgent;
import de.rwth_aachen.kbsg.dq.Player;
import de.rwth_aachen.kbsg.dq.RandomAgent;
import de.rwth_aachen.kbsg.dq.RuleBasedAgent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
	
	private static interface Handler {
		public void handle();
	}
	
	public static class ListDialogFragment extends DialogFragment {
		private String title;
		private Vector<String> strings = new Vector<String>();
		private Vector<Handler> handlers = new Vector<Handler>();

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			String[] stringsArr = new String[strings.size()];
			strings.toArray(stringsArr);
			builder.setTitle(title).setItems(stringsArr, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handlers.get(which).handle();
				}
			});
			return builder.create();
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
		public void addEntry(String string, Handler handler) {
			strings.add(string);
			handlers.add(handler);
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
		final int[] heuristicNames = { R.string.heuristic1, R.string.heuristic2 };
		final Heuristic[] heuristics = { new Heuristic1(), new Heuristic2() };
		final Activity a = (Activity) getContext();
		final ListDialogFragment ldfHeuristic = new ListDialogFragment();
		ldfHeuristic.setTitle(a.getString(R.string.choose_heuristic));
		for (int i = 0; i < heuristicNames.length; ++i) {
			final int name = heuristicNames[i];
			final Heuristic heuristic = heuristics[i];
			for (int j = 1000; j <= 5000; j+=1000) {
				final int depth = j;
				ldfHeuristic.addEntry(a.getString(name) +" x "+ depth, new Handler() {
					@Override
					public void handle() {
						listener.onAgentSelected(new MiniMaxAgent(c, depth, heuristic));
					}
				});
			}
		}
		
		final ListDialogFragment ldf = new ListDialogFragment();
		ldf.setTitle(a.getString(c == Color.WHITE ? R.string.choose_white : R.string.choose_black));
		ldf.addEntry(a.getString(R.string.player_human), new Handler() {
			@Override
			public void handle() {
				listener.onHumanSelected(c);
			}
		});
		ldf.addEntry(a.getString(R.string.player_random), new Handler() {
			@Override
			public void handle() {
				listener.onAgentSelected(new RandomAgent(c));
			}
		});
		ldf.addEntry(a.getString(R.string.player_rulebased), new Handler() {
			@Override
			public void handle() {
				listener.onAgentSelected(new RuleBasedAgent(c));
			}
		});
		ldf.addEntry(a.getString(R.string.player_minimax), new Handler() {
			@Override
			public void handle() {
				ldfHeuristic.show(a.getFragmentManager(), "heuristic");
			}
		});
		ldf.show(a.getFragmentManager(), "player");
	}
	
	private void showPlayerMenu2(final Color c) {
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
