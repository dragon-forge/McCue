package com.zeitheron.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CorsairProtocolDetails
		extends Structure
{
	public Pointer sdkVersion;
	public Pointer serverVersion;
	public int sdkProtocolVersion;
	public int serverProtocolVersion;
	public byte breakingChanges;

	public CorsairProtocolDetails()
	{
	}

	@Override
	protected List<String> getFieldOrder()
	{
		return Arrays.asList("sdkVersion", "serverVersion", "sdkProtocolVersion", "serverProtocolVersion", "breakingChanges");
	}

	public CorsairProtocolDetails(Pointer sdkVersion, Pointer serverVersion, int sdkProtocolVersion, int serverProtocolVersion, byte breakingChanges)
	{
		this.sdkVersion = sdkVersion;
		this.serverVersion = serverVersion;
		this.sdkProtocolVersion = sdkProtocolVersion;
		this.serverProtocolVersion = serverProtocolVersion;
		this.breakingChanges = breakingChanges;
	}

	public static class ByValue
			extends CorsairProtocolDetails
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairProtocolDetails
			implements Structure.ByReference
	{
	}
}