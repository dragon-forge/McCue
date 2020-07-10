package com.zeitheron.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CorsairLedColor
		extends Structure
{
	public int ledId;
	public int r;
	public int g;
	public int b;

	public CorsairLedColor()
	{
	}

	@Override
	protected List<String> getFieldOrder()
	{
		return Arrays.asList("ledId", "r", "g", "b");
	}

	public CorsairLedColor(int ledId, int r, int g, int b)
	{
		this.ledId = ledId;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public static class ByValue
			extends CorsairLedColor
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairLedColor
			implements Structure.ByReference
	{
	}
}