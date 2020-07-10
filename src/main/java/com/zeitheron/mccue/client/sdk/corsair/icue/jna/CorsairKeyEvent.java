package com.zeitheron.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CorsairKeyEvent
		extends Structure
{
	public byte[] deviceId = new byte[128];
	public CUESDKLibrary.CorsairKeyId keyId;
	public byte isPressed;

	public CorsairKeyEvent()
	{
	}

	@Override
	protected List<String> getFieldOrder()
	{
		return Arrays.asList("deviceId", "keyId", "isPressed");
	}

	public CorsairKeyEvent(byte[] deviceId, CUESDKLibrary.CorsairKeyId keyId, byte isPressed)
	{
		if(deviceId.length != this.deviceId.length)
		{
			throw new IllegalArgumentException("Wrong array size !");
		}
		this.deviceId = deviceId;
		this.keyId = keyId;
		this.isPressed = isPressed;
	}

	public static class ByValue
			extends CorsairKeyEvent
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairKeyEvent
			implements Structure.ByReference
	{
	}
}