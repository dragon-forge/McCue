package com.zeitheron.mccue.custom.animators;

import com.zeitheron.mccue.api.RgbTask;
import com.zeitheron.mccue.api.base.RgbAnimator;
import net.minecraft.nbt.NBTTagCompound;

public class RgbFlashAnimator
		extends RgbAnimator
{
	long start;

	public RgbFlashAnimator(RgbTask task)
	{
		super(task);
	}

	@Override
	public void update()
	{
		if(this.start == 0L) this.start = System.currentTimeMillis();
	}

	@Override
	public boolean isAlive()
	{
		if(this.start == 0L) this.start = System.currentTimeMillis();
		long timeMS = this.task.getTaskData().getLong("A_time");
		return System.currentTimeMillis() - this.start <= timeMS;
	}

	@Override
	public float supplyBrightness()
	{
		NBTTagCompound nbt;
		if(this.start == 0L) this.start = System.currentTimeMillis();
		float amplitude = (nbt = this.task.getTaskData()).hasKey("A_amplitude", 5) ? nbt.getFloat("A_amplitude") / 100.0f : 1.0f;
		long timeMS = nbt.getLong("A_time");
		boolean sine = nbt.getBoolean("A_sine");
		float lprog = (float) ((double) (System.currentTimeMillis() - this.start) / (double) timeMS) * 2.0f;
		if(sine) return (float) Math.sin(Math.toRadians(lprog * 90.0f)) * amplitude;
		return (lprog > 1.0f ? 2.0f - lprog : lprog) * amplitude;
	}
}