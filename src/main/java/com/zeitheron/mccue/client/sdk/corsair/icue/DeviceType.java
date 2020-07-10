package com.zeitheron.mccue.client.sdk.corsair.icue;

import java.io.Serializable;

public enum DeviceType
		implements Serializable
{
	CDT_Unknown,
	CDT_Mouse,
	CDT_Keyboard,
	CDT_Headset,
	CDT_MouseMat,
	CDT_HeadsetStand,
	CDT_CommanderPro,
	CDT_LightingNodePro,
	CDT_MemoryModule,
	CDT_Cooler;

	private static DeviceType[] values;

	public static DeviceType byOrdinal(int ordinal)
	{
		if(ordinal >= 0 && ordinal < values.length)
		{
			return values[ordinal];
		}
		return null;
	}

	public boolean isKnown()
	{
		return this != CDT_Unknown;
	}

	static
	{
		values = DeviceType.values();
	}
}