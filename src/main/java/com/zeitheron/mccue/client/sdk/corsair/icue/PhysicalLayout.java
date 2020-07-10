package com.zeitheron.mccue.client.sdk.corsair.icue;

import java.io.Serializable;

public enum PhysicalLayout
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
	CPL_Zones4;

	private static PhysicalLayout[] values;

	public static PhysicalLayout byOrdinal(int ordinal)
	{
		if(ordinal >= 0 && ordinal < values.length)
		{
			return values[ordinal];
		}
		return null;
	}

	static
	{
		values = PhysicalLayout.values();
	}
}