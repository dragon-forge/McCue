package org.zeith.mccue.client.sdk.corsair.icue.jna;

import java.io.Serializable;

public enum CorsairPhysicalLayout
		implements Serializable
{
	CPL_Invalid,
	CPL_US,
	CPL_UK,
	CPL_BR,
	CPL_JP,
	CPL_KR,
	CPL_Zones1,
	CPL_Zones2,
	CPL_Zones3,
	CPL_Zones4,
	CPL_Zones5,
	CPL_Zones6,
	CPL_Zones7,
	CPL_Zones8,
	CPL_Zones9,
	CPL_Zones10,
	CPL_Zones11,
	CPL_Zones12,
	CPL_Zones13,
	CPL_Zones14,
	CPL_Zones15,
	CPL_Zones16,
	CPL_Zones17,
	CPL_Zones18,
	CPL_Zones19,
	CPL_Zones20;

	private static CorsairPhysicalLayout[] values;

	public static CorsairPhysicalLayout byOrdinal(int ordinal)
	{
		if(ordinal >= 0 && ordinal < values.length)
		{
			return values[ordinal];
		}
		return null;
	}

	static
	{
		values = CorsairPhysicalLayout.values();
	}
}