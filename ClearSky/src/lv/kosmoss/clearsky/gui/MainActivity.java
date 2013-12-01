package lv.kosmoss.clearsky.gui;

import java.util.Timer;
import java.util.TimerTask;

import lv.kosmoss.clearsky.core.LocalService;
import lv.kosmoss.clearsky.core.SensorConsumer;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.kosmoss.clearsky.R;

public class MainActivity extends Activity {
	private Timer autoUpdate;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startService(new Intent(this, LocalService.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		bindService(new Intent(this, LocalService.class), mConnection,
				Context.BIND_AUTO_CREATE);

		autoUpdate = new Timer();
		autoUpdate.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						updateReadings();
					}
				});
			}
		}, 0, 200); // updates UI each 200 msecs
	}

	@Override
	protected void onPause() {
		super.onPause();
		autoUpdate.cancel();
		unbindService(mConnection);
	}

	private void updateReadings() {
		String textX, textY, textZ;
		if (mService == null)
			return;
		SensorConsumer consumer = mService.getConsumer();

		float[] acc = consumer.getAcceleration();
		if (acc != null) {
			textX = String.format("%+1.2f", acc[0] / 9.81);
			textY = String.format("%+1.2f", acc[1] / 9.81);
			textZ = String.format("%+1.2f", acc[2] / 9.81);
			TextView textViewX = (TextView) findViewById(R.id.textViewAccelX);
			TextView textViewY = (TextView) findViewById(R.id.textViewAccelY);
			TextView textViewZ = (TextView) findViewById(R.id.textViewAccelZ);
			textViewX.setText(textX);
			textViewY.setText(textY);
			textViewZ.setText(textZ);
		}
		float[] mag = consumer.getMagfield();
		if (mag != null) {
			textX = String.format("%+1.2f", mag[0] / 50);
			textY = String.format("%+1.2f", mag[1] / 50);
			textZ = String.format("%+1.2f", mag[2] / 50);
			TextView textViewX = (TextView) findViewById(R.id.textViewMagX);
			TextView textViewY = (TextView) findViewById(R.id.textViewMagY);
			TextView textViewZ = (TextView) findViewById(R.id.textViewMagZ);
			textViewX.setText(textX);
			textViewY.setText(textY);
			textViewZ.setText(textZ);
		}
	}

	private LocalService mService = null;

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {
			mService = ((LocalService.MyBinder) binder).getService();

			// Toast.makeText(MainActivity.this, "Connected",
			// Toast.LENGTH_SHORT)
			// .show();
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};
}
