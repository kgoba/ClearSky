package lv.kosmoss.clearsky.core;

public class StateReport {
	public int state = 0;

	public XYZVector acc = null;
	public XYZVector mag = null;

	public float magUp = Float.NaN;
	public float magDown = Float.NaN;

	public boolean isUp;
	public boolean isVertical;
}
