package de.rwth_aachen.kbsg.drq;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

public class Util {
	public static void showToast(View v, String msg) {
		showToast(v, msg, true);
	}
	
	public static void showToast(final View v, final String msg, final boolean shortDuration) {
		((Activity) v.getContext()).runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(v.getContext(), msg, shortDuration ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
			}
		});
	}
}
