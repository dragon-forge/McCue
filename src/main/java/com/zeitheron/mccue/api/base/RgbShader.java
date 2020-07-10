package com.zeitheron.mccue.api.base;

import com.zeitheron.mccue.api.RgbTask;

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