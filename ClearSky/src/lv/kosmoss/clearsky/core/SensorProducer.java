package lv.kosmoss.clearsky.core;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorProducer implements SensorEventListener {
	SensorManager mSensorManager;
	Context mContext;

	SensorProducer(Context c, SensorManager m) {
		mSensorManager = m;
		mContext = c;
	}

	void Start() {
		Sensor mMagSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor mAccSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor mBarSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_PRESSURE);
		// Sensor mTempSensor =
		// sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

		if (mAccSensor != null) {
			mSensorManager.registerListener(this, mAccSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
		if (mMagSensor != null) {
			mSensorManager.registerListener(this, mMagSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
		if (mBarSensor != null) {
			mSensorManager.registerListener(this, mBarSensor,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	void Stop() {
		mSensorManager.unregisterListener(this);
	}
	
	private List<SensorConsumer> mConsumers;
	
	public void AddConsumer(SensorConsumer consumer)
	{
		mConsumers.add(consumer);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		for (SensorConsumer consumer : mConsumers)
		{
			consumer.Consume(event);
		}
	}

	// {
	// LocalBroadcastManager manager = LocalBroadcastManager
	// .getInstance(mContext);
	//
	// Intent intent = new Intent(mContext, MainActivity.class);
	// switch (event.sensor.getType()) {
	// case Sensor.TYPE_ACCELEROMETER:
	// intent.putExtra("acc", event.values);
	// break;
	// case Sensor.TYPE_MAGNETIC_FIELD:
	// intent.putExtra("mag", event.values);
	// break;
	// }
	// manager.sendBroadcast(intent);
	// }
}
