package de.rwth_aachen.kbsg.drq;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((FieldView) findViewById(R.id.field)).registerConfigButton((Button) findViewById(R.id.config_button));
		((FieldView) findViewById(R.id.field)).registerGameButton((Button) findViewById(R.id.game_button));
	}
}
