package com.zeitheron.mccue.custom.shaders;

import com.zeitheron.mccue.api.RgbTask;
import com.zeitheron.mccue.api.base.RgbShader;
import net.minecraft.util.math.MathHelper;

public class RgbRainbowShader
		extends RgbShader
{
	public RgbRainbowShader(RgbTask task)
	{
		super(task);
	}

	@Override
	public void shadeColor(float brightness, int[] outData)
	{
		long speed = this.task.getTaskData().getLong("S_speed");
		if(speed <= 0L) speed = 6000L;
		int color = MathHelper.hsvToRGB((System.currentTimeMillis() % speed) / (float) speed, 1.0f, brightness);
		outData[0] = color >> 16 & 255;
		outData[1] = color >> 8 & 255;
		outData[2] = color & 255;
	}
}