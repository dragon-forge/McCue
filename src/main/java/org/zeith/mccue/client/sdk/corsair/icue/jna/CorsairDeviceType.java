package org.zeith.mccue.client.sdk.corsair.icue.jna;

import java.io.Serializable;

public enum CorsairDeviceType
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
	CDT_Cooler,
	CDT_Motherboard,
	CDT_GraphicsCard;

	private static final CorsairDeviceType[] values = values();

	public static CorsairDeviceType byOrdinal(int ordinal)
	{
		if(ordinal >= 0 && ordinal < values.length)
			return values[ordinal];
		return null;
	}

	public boolean isKnown()
	{
		return this != CDT_Unknown;
	}
}