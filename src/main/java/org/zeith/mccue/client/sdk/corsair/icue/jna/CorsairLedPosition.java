package org.zeith.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CorsairLedPosition
		extends Structure
{
	public int ledId;
	public double top;
	public double left;
	public double height;
	public double width;

	public CorsairLedPosition()
	{
	}

	@Override
	protected List<String> getFieldOrder()
	{
		return Arrays.asList("ledId", "top", "left", "height", "width");
	}

	public CorsairLedPosition(int ledId, double top, double left, double height, double width)
	{
		this.ledId = ledId;
		this.top = top;
		this.left = left;
		this.height = height;
		this.width = width;
	}

	public static class ByValue
			extends CorsairLedPosition
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairLedPosition
			implements Structure.ByReference
	{
	}
}