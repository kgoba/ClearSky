package lv.kosmoss.clearsky.support;

import lv.kosmoss.clearsky.core.SensorConsumer;
import lv.kosmoss.clearsky.core.SensorProducer;
import lv.kosmoss.clearsky.core.StateMachine;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

public class LocalService extends Service {
	private final IBinder mBinder = new MyBinder();

	private SensorProducer mProducer = null;
	private SensorConsumer mConsumer = null;
	
	private StateMachine mStateMachine = null;

	@Override
	public void onCreate() {
		// code to execute when the service is first created
		SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mProducer = new SensorProducer(this, sensorManager);
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

	public class MyBinder extends Binder {
		public LocalService getService() {
			return LocalService.this;
		}
	}
	
	public SensorConsumer getConsumer()
	{
		return mConsumer;
	}
	
	public void Start()
	{
		mProducer.Start();
	}
	
	public void Stop()
	{
		mProducer.Stop();
	}
}
