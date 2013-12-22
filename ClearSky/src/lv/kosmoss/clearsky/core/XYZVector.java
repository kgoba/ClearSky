package lv.kosmoss.clearsky.core;

public class XYZVector {
	public float x;
	public float y;
	public float z;
	
	public XYZVector()
	{
		x = 0;
		y = 0;
		z = 0;
	}
	
	public XYZVector(float[] data) throws Exception
	{
		assign(data);
	}
	
	public void assign(float[] data) throws Exception
	{
		if (data == null) throw new Exception("Assigning null value");
		if (data.length != 3) throw new Exception("Expected 3 values in a vector");
		x = data[0];
		y = data[1];
		z = data[2];
	}
	
	public void rescale(float scale)
	{
		x *= scale;
		y *= scale;
		z *= scale;
	}
}
