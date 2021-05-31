package org.zeith.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CorsairDeviceInfo
		extends Structure
{
	public int type;
	public Pointer model;
	public int physicalLayout;
	public int logicalLayout;
	public int capsMask;
	public int ledsCount;
	public CorsairChannelsInfo channels;
	public byte[] deviceId = new byte[128];

	public CorsairDeviceInfo()
	{
	}

	@Override
	protected List<String> getFieldOrder()
	{
		return Arrays.asList("type", "model", "physicalLayout", "logicalLayout", "capsMask", "ledsCount", "channels", "deviceId");
	}

	public CorsairDeviceInfo(int type, Pointer model, int physicalLayout, int logicalLayout, int capsMask, int ledsCount, CorsairChannelsInfo channels, byte[] deviceId)
	{
		this.type = type;
		this.model = model;
		this.physicalLayout = physicalLayout;
		this.logicalLayout = logicalLayout;
		this.capsMask = capsMask;
		this.ledsCount = ledsCount;
		this.channels = channels;
		if(deviceId.length != this.deviceId.length)
			throw new IllegalArgumentException("Wrong array size !");
		this.deviceId = deviceId;
	}

	public CorsairDeviceType getType()
	{
		return CorsairDeviceType.byOrdinal(this.type);
	}

	public CorsairPhysicalLayout getPhysicalLayout()
	{
		return CorsairPhysicalLayout.byOrdinal(this.type);
	}

	public CorsairLogicalLayout getLogicalLayout()
	{
		return CorsairLogicalLayout.byOrdinal(this.type);
	}

	public String getDeviceId()
	{
		return new String(deviceId);
	}

	public static class ByValue
			extends CorsairDeviceInfo
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairDeviceInfo
			implements Structure.ByReference
	{
	}
}