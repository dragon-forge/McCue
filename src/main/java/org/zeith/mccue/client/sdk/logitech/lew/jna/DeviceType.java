package org.zeith.mccue.client.sdk.logitech.lew.jna;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.lang.reflect.Field;

public final class DeviceType
{
	public static final int Keyboard = 0x0;
	public static final int Mouse = 0x3;
	public static final int Mousemat = 0x4;
	public static final int Headset = 0x8;
	public static final int Speaker = 0xe;

	private static final int[] VALUES;

	public static int[] values()
	{
		return VALUES;
	}

	static
	{
		IntArrayList ints = new IntArrayList();
		// collect keys
		for(Field f : DeviceType.class.getDeclaredFields())
			if(int.class.isAssignableFrom(f.getType()))
			{
				try
				{
					ints.add(f.getInt(null));
				} catch(IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		VALUES = ints.toIntArray();
	}
}