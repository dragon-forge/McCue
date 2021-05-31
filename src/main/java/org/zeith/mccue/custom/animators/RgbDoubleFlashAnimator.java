package org.zeith.mccue.custom.animators;

import org.zeith.mccue.api.RgbTask;
import org.zeith.mccue.api.base.RgbAnimator;
import net.minecraft.nbt.NBTTagCompound;

public class RgbDoubleFlashAnimator
		extends RgbAnimator
{
	long start;

	public RgbDoubleFlashAnimator(RgbTask task)
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
		long time1MS = this.task.getTaskData().getLong("A_time1");
		long time2MS = this.task.getTaskData().getLong("A_time2");
		return System.currentTimeMillis() - this.start <= time1MS + time2MS;
	}

	@Override
	public float supplyBrightness()
	{
		if(this.start == 0L) this.start = System.currentTimeMillis();
		NBTTagCompound nbt = this.task.getTaskData();
		long time1MS = nbt.getLong("A_time1");
		if(System.currentTimeMillis() - this.start <= time1MS)
		{
			float amplitude = nbt.hasKey("A_amplitude1", 5) ? nbt.getFloat("A_amplitude1") / 100.0f : 1.0f;
			boolean sine1 = nbt.getBoolean("A_sine1");
			float lprog = (float) ((double) (System.currentTimeMillis() - this.start) / (double) time1MS) * 2.0f;
			if(sine1) return (float) Math.sin(Math.toRadians(lprog * 90.0f)) * amplitude;
			return (lprog > 1.0f ? 2.0f - lprog : lprog) * amplitude;
		}
		float amplitude = nbt.hasKey("A_amplitude2", 5) ? nbt.getFloat("A_amplitude2") / 100.0f : 1.0f;
		long time2MS = nbt.getLong("A_time2");
		boolean sine2 = nbt.getBoolean("A_sine2");
		float lprog = (float) ((double) (System.currentTimeMillis() - this.start - time1MS) / (double) time2MS) * 2.0f;
		if(sine2) return (float) Math.sin(Math.toRadians(lprog * 90.0f)) * amplitude;
		return (lprog > 1.0f ? 2.0f - lprog : lprog) * amplitude;
	}
}

