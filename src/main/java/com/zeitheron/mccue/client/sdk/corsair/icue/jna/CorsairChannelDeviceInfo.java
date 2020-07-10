package com.zeitheron.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CorsairChannelDeviceInfo
		extends Structure
{
	public int type;
	public int deviceLedCount;

	public CorsairChannelDeviceInfo()
	{
	}

	protected List<String> getFieldOrder()
	{
		return Arrays.asList("type", "deviceLedCount");
	}

	public CorsairChannelDeviceInfo(int type, int deviceLedCount)
	{
		this.type = type;
		this.deviceLedCount = deviceLedCount;
	}

	public static class ByValue
			extends CorsairChannelDeviceInfo
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairChannelDeviceInfo
			implements Structure.ByReference
	{
	}

}