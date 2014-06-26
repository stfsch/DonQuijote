package de.rwth_aachen.kbsg.drq;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FieldView fw = (FieldView) findViewById(R.id.field);
		ConfigButton cb = (ConfigButton) findViewById(R.id.config);
		GameController gc = new GameController(fw);
		fw.setListener(gc);
		cb.setListener(gc);
	}
}
