package org.zeith.mccue.api.sdk;

import com.zeitheron.hammercore.utils.color.ColorHelper;

import java.awt.*;

public interface IRGBPosition
{
	int id();

	boolean set(int rgb);

	default boolean set(float red, float green, float blue)
	{
		return set(ColorHelper.packRGB(red, green, blue));
	}

	default boolean set(Color rgb)
	{
		return set(rgb.getRGB());
	}
}