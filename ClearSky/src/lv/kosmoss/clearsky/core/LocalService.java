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

public class LocalService extends Service implements SensorEventListener {
	private final IBinder mBinder = new MyBinder();

	private SensorManager mSensorManager = null;
	private Sensor mMagSensor = null;
	private Sensor mAccSensor = null;
	private Sensor mBarSensor = null;

    @Override
    public void onCreate() {
        // code to execute when the service is first created
		// List<Sensor> deviceSensors = mSensorManager
		// .getSensorList(Sensor.TYPE_ALL);

    	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    	
		mMagSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mBarSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		
		if (mAccSensor != null) {
			mSensorManager.registerListener(this, mAccSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
		if (mMagSensor != null) {
			mSensorManager.registerListener(this, mMagSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
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
		//mSensorManager.unregisterListener(this);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	private float[] accValues = null;
	private float[] magValues = null;
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			accValues = event.values.clone();
		else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			magValues = event.values.clone();
	}
	
	public float[] getAcceleration()
	{
		return accValues;
	}
	
	public float[] getMagfield()
	{
		return magValues;
	}
	
	public class MyBinder extends Binder {
		public LocalService getService() {
			return LocalService.this;
		}
	}
}
