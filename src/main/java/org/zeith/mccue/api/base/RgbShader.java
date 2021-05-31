package org.zeith.mccue.api.base;

import org.zeith.mccue.api.RgbTask;

public abstract class RgbShader
{
	protected final RgbTask task;

	public RgbShader(RgbTask task)
	{
		this.task = task;
	}

	public RgbTask getTask()
	{
		return this.task;
	}

	public abstract void shadeColor(float var1, int[] var2);

	protected void outputColor(int rgb, int[] outData)
	{
		outData[0] = rgb >> 24 & 255;
		outData[1] = rgb >> 16 & 255;
		outData[2] = rgb >> 8 & 255;
	}
}