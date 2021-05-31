package org.zeith.mccue.api.base;

import org.zeith.mccue.api.RgbTask;

public abstract class RgbAnimator
{
	protected final RgbTask task;

	public RgbAnimator(RgbTask task)
	{
		this.task = task;
	}

	public RgbTask getTask()
	{
		return this.task;
	}

	public abstract float supplyBrightness();

	public abstract void update();

	public abstract boolean isAlive();
}