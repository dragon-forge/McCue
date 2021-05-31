package org.zeith.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CorsairChannelInfo
		extends Structure
{
	public int totalLedsCount;
	public int devicesCount;
	public CorsairChannelDeviceInfo.ByReference devices;

	public CorsairChannelInfo()
	{
	}

	protected List<String> getFieldOrder()
	{
		return Arrays.asList("totalLedsCount", "devicesCount", "devices");
	}

	public CorsairChannelInfo(int totalLedsCount, int devicesCount, CorsairChannelDeviceInfo.ByReference devices)
	{
		this.totalLedsCount = totalLedsCount;
		this.devicesCount = devicesCount;
		this.devices = devices;
	}

	public static class ByValue
			extends CorsairChannelInfo
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairChannelInfo
			implements Structure.ByReference
	{
	}

}

