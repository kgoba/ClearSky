package lv.kosmoss.clearsky.core;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class LastValueConsumer implements SensorEventListener {
	private float[] accValues = null;
	private float[] magValues = null;
	
	public float[] getAcceleration() {
		return accValues;
	}

	public float[] getMagfield() {
		return magValues;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			// intent.putExtra("acc", event.values);
			accValues = event.values.clone();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			// intent.putExtra("mag", event.values);
			magValues = event.values.clone();
			break;
		case Sensor.TYPE_PRESSURE:
			break;
		}
	}
}
