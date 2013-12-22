package lv.kosmoss.clearsky.gui;

import java.util.Timer;
import java.util.TimerTask;

import lv.kosmoss.clearsky.core.StateReport;
import lv.kosmoss.clearsky.support.LocalService;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.widget.TextView;

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
		StateReport report = mService.getReport();
		// report acceleration
		if (report.acc != null)
		{
			textX = String.format("%+1.2f", report.acc.x / SensorManager.STANDARD_GRAVITY);
			textY = String.format("%+1.2f", report.acc.y / SensorManager.STANDARD_GRAVITY);
			textZ = String.format("%+1.2f", report.acc.z / SensorManager.STANDARD_GRAVITY);
			TextView textViewX = (TextView) findViewById(R.id.textViewAccelX);
			TextView textViewY = (TextView) findViewById(R.id.textViewAccelY);
			TextView textViewZ = (TextView) findViewById(R.id.textViewAccelZ);
			textViewX.setText(textX);
			textViewY.setText(textY);
			textViewZ.setText(textZ);
		}
		// report magnetic field
		if (report.mag != null) {
			textX = String.format("%+1.2f", report.mag.x / SensorManager.MAGNETIC_FIELD_EARTH_MAX);
			textY = String.format("%+1.2f", report.mag.y / SensorManager.MAGNETIC_FIELD_EARTH_MAX);
			textZ = String.format("%+1.2f", report.mag.z / SensorManager.MAGNETIC_FIELD_EARTH_MAX);
			TextView textViewX = (TextView) findViewById(R.id.textViewMagX);
			TextView textViewY = (TextView) findViewById(R.id.textViewMagY);
			TextView textViewZ = (TextView) findViewById(R.id.textViewMagZ);
			textViewX.setText(textX);
			textViewY.setText(textY);
			textViewZ.setText(textZ);
		}
		// report orientation
		TextView textViewVertical = (TextView) findViewById(R.id.textViewVertical);
		String orientation = new String();
		if (report.isVertical)
			orientation += "vertical ";
		if (report.isUp)
			orientation += "up";
		else
			orientation += "down";
		textViewVertical.setText(orientation);
	}

	private LocalService mService = null;

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {
			mService = ((LocalService.MyBinder) binder).getService();
			if (mService != null) mService.Start();

			// Toast.makeText(MainActivity.this, "Connected",
			// Toast.LENGTH_SHORT)
			// .show();
		}

		public void onServiceDisconnected(ComponentName className) {
			if (mService != null) mService.Stop();
			mService = null;
		}
	};
}
