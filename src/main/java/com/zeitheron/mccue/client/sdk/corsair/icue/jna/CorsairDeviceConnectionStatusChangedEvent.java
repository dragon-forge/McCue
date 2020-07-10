package com.zeitheron.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CorsairDeviceConnectionStatusChangedEvent
		extends Structure
{
	public byte[] deviceId = new byte[128];
	public byte isConnected;

	public CorsairDeviceConnectionStatusChangedEvent()
	{
	}

	@Override
	protected List<String> getFieldOrder()
	{
		return Arrays.asList("deviceId", "isConnected");
	}

	public CorsairDeviceConnectionStatusChangedEvent(byte[] deviceId, byte isConnected)
	{
		if(deviceId.length != this.deviceId.length)
		{
			throw new IllegalArgumentException("Wrong array size !");
		}
		this.deviceId = deviceId;
		this.isConnected = isConnected;
	}

	public static class ByValue
			extends CorsairDeviceConnectionStatusChangedEvent
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairDeviceConnectionStatusChangedEvent
			implements Structure.ByReference
	{
	}
}