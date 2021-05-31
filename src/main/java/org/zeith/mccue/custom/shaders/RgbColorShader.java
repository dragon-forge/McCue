package org.zeith.mccue.custom.shaders;

import org.zeith.mccue.api.RgbTask;
import org.zeith.mccue.api.base.RgbShader;

public class RgbColorShader
		extends RgbShader
{
	public RgbColorShader(RgbTask task)
	{
		super(task);
	}

	@Override
	public void shadeColor(float brightness, int[] outData)
	{
		int color = this.task.getTaskData().getInteger("S_color");
		int red = color >> 16 & 255;
		int green = color >> 8 & 255;
		int blue = color & 255;
		red = (int) ((float) red * brightness);
		green = (int) ((float) green * brightness);
		blue = (int) ((float) blue * brightness);
		outData[0] = red;
		outData[1] = green;
		outData[2] = blue;
	}
}