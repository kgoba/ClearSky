package lv.kosmoss.clearsky.support;

import lv.kosmoss.clearsky.core.StateMachine;
import lv.kosmoss.clearsky.core.StateReport;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

public class LocalService extends Service {
	private final IBinder mBinder = new MyBinder();

	private StateMachine mStateMachine = null;

	@Override
	public void onCreate() {
		// code to execute when the service is first created
		SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mStateMachine = new StateMachine(this, sensorManager);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class MyBinder extends Binder {
		public LocalService getService() {
			return LocalService.this;
		}
	}
	
	public StateReport getReport()
	{
		return mStateMachine.getReport();
	}
	
	public void Start()
	{
		mStateMachine.Start();
	}
	
	public void Stop()
	{
		mStateMachine.Stop();
	}
}
