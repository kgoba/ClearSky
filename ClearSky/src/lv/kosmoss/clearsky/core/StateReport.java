package lv.kosmoss.clearsky.core;

public class StateReport {
	public int state = 0;
	public String stateName = null;

	public XYZVector acc = null;
	public XYZVector mag = null;

	public float magCalibUp = Float.NaN;
	public float magCalibDown = Float.NaN;

	public boolean isUp;
	public boolean isVertical;
}
