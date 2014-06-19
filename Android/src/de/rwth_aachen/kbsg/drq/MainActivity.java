package de.rwth_aachen.kbsg.drq;

import de.rwth_aachen.kbsg.dq.Color;
import de.rwth_aachen.kbsg.dq.Game;
import de.rwth_aachen.kbsg.dq.Human;
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FieldView fw = (FieldView) findViewById(R.id.field);
		final Game game = new Game(fw, new Human(Color.WHITE, fw), new Human(Color.BLACK, fw));
		new Thread() {
			@Override
			public void run() {
				game.play();
			}
		}.start();
	}
}
