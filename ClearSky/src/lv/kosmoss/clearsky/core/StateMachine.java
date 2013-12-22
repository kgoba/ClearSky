package lv.kosmoss.clearsky.core;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StateMachine implements SensorEventListener {
	public static final int STATE_IDLE 		= 0;
	public static final int STATE_CALIB 	= 1;
	public static final int STATE_ARM 		= 2;
	public static final int STATE_COUNTDOWN	= 3;
	public static final int STATE_FLIGHT 	= 4;
	public static final int STATE_RECOVERY 	= 5;
	
	public static final int COMMAND_NONE	= 0;
	public static final int COMMAND_CALIB	= 1;
	public static final int COMMAND_ARM		= 2;
	
	private static final float THRESHOLD_VERTICAL_ALIGN = 0.05f * SensorManager.STANDARD_GRAVITY;	// ~3 deg
	private static final int VERTICAL_SUCCESS_COUNT = 20;
	
	private SensorManager mSensorManager = null;
	private Context mContext = null;
	private Timer mAutoUpdate = null;
	
	public StateMachine(Context c, SensorManager m)
	{
		mSensorManager = m;
		mContext = c;
	}
	
	public void Start()
	{
		Sensor mMagSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor mAccSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		if (mAccSensor != null) {
			mSensorManager.registerListener(this, mAccSensor,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mMagSensor != null) {
			mSensorManager.registerListener(this, mMagSensor,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		
		mAutoUpdate = new Timer();
		mAutoUpdate.schedule(new TimerTask() {
			@Override
			public void run() {
				updateState();
			}
		}, 0, 200);		
	}
	
	public void Stop()
	{
		mAutoUpdate.cancel();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// nothing to do
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		try {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				lastAcc.assign(event.values);
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				lastMag.assign(event.values);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int mState = STATE_IDLE;
	private int mCommand = COMMAND_NONE;
	
	private XYZVector lastMag = new XYZVector();
	private XYZVector lastAcc = new XYZVector();

	private float magCalibUp = Float.NaN;		// magnetic component along the axis of flight when the vehicle is facing up
	private float magCalibDown = Float.NaN;		// magnetic component along the axis of flight when the vehicle is facing down

	private float magCalibTotal = 0;
	private int magCalibCount = 0;
	
	private boolean isUp = false;
	private boolean isVertical = false;
	
	StateReport mReport = new StateReport();
	
	public StateReport getReport()
	{
		mReport.state = mState;
		switch (mState) {
		case STATE_IDLE:
			mReport.stateName = "IDLE";
			break;
		case STATE_CALIB:
			mReport.stateName = "CALIB";
			break;
		}
		
		mReport.acc = lastAcc;
		mReport.mag = lastMag;
		
		mReport.magCalibUp = magCalibUp;
		mReport.magCalibDown = magCalibDown;
		
		mReport.isUp = isUp;
		mReport.isVertical = isVertical;
		
		return mReport;
	}
	
	public void setCommand(int command)
	{
		mCommand = command;
	}

	private void updateState()
	{
		// check vertical alignment
		if (lastAcc != null) {
			isVertical = (Math.abs(lastAcc.x) < THRESHOLD_VERTICAL_ALIGN) 
				&& (Math.abs(lastAcc.z) < THRESHOLD_VERTICAL_ALIGN);
			isUp = lastAcc.y > 0;
		}
		else
			isVertical = false;

		// update state
		switch (mState) {
		case STATE_IDLE:
			// do nothing, wait for GUI to test or arm
			if (mCommand == COMMAND_CALIB) {
				mCommand = COMMAND_NONE;
				// reset calibration data
				magCalibUp = Float.NaN;
				magCalibDown = Float.NaN;
				magCalibCount = 0;
				magCalibTotal = 0;
				mState = STATE_CALIB;
			}
			break;
			
		case STATE_CALIB:			
			if (!isVertical) {
				magCalibCount = 0;
				magCalibTotal = 0;
				break;
			}
			magCalibCount++;
			magCalibTotal += lastMag.y;
			if (magCalibCount < VERTICAL_SUCCESS_COUNT) {
				break;
			}
			if (Float.isNaN(magCalibUp) && isUp) {
				magCalibUp = magCalibTotal / magCalibCount;
				// TODO produce a sound
			}
			if (Float.isNaN(magCalibDown) && !isUp) {
				magCalibDown = magCalibTotal / magCalibCount;
				// TODO produce a sound
			}
			if (!Float.isNaN(magCalibUp) && !Float.isNaN(magCalibDown)) 
				mState = STATE_IDLE;
			break;
			
		case STATE_ARM: 
		default:
		}
	}
	
	/*
	State state = new StateIdle(); 
	
	abstract class State
	{
		abstract State Process();
		abstract int GetStateId();
	}
	
	class StateIdle extends State
	{
		@Override
		State Process() {
			return this;
		}

		@Override
		int GetStateId() {
			return STATE_IDLE;
		}	
	}	
	*/
}
