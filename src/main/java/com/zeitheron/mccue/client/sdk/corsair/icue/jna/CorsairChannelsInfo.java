package com.zeitheron.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CorsairChannelsInfo
		extends Structure
{
	public int channelsCount;
	public CorsairChannelInfo.ByReference channels;

	public CorsairChannelsInfo()
	{
	}

	@Override
	protected List<String> getFieldOrder()
	{
		return Arrays.asList("channelsCount", "channels");
	}

	public CorsairChannelsInfo(int channelsCount, CorsairChannelInfo.ByReference channels)
	{
		this.channelsCount = channelsCount;
		this.channels = channels;
	}

	public static class ByValue
			extends CorsairChannelsInfo
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairChannelsInfo
			implements Structure.ByReference
	{
	}
}