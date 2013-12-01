package lv.kosmoss.clearsky.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

public class LocalService extends Service {
	private final IBinder mBinder = new MyBinder();

	private SensorProducer mProducer = null;
	private SensorConsumer mConsumer = null;

	@Override
	public void onCreate() {
		// code to execute when the service is first created
		SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mProducer = new SensorProducer(this, sensorManager);
		mProducer.Start();
		mConsumer = new SensorConsumer();
		mProducer.AddConsumer(mConsumer);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent arg0) {
		// mSensorManager.unregisterListener(this);
		return true;
	}

	public class MyBinder extends Binder {
		public LocalService getService() {
			return LocalService.this;
		}
	}
	
	public SensorConsumer getConsumer()
	{
		return mConsumer;
	}
}
