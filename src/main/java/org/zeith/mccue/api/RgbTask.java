package org.zeith.mccue.api;

import org.zeith.mccue.api.base.*;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class RgbTask
{
	private static final ThreadLocal<int[]> outputDataFactory = ThreadLocal.withInitial(() -> new int[3]);
	public final RgbTrigger trigger;
	public RgbAnimator animator;
	public RgbShader shader;
	private NBTTagCompound taskData;
	public final Optional<Consumer<int[]>> colorSink;

	public RgbTask(@Nonnull RgbTrigger trigger, @Nonnull RgbAnimatorEntry animator, @Nonnull RgbShaderEntry shader, Optional<Consumer<int[]>> colorSink, @Nullable NBTTagCompound taskData)
	{
		this.taskData = taskData;
		this.trigger = trigger;
		this.colorSink = colorSink;
		this.animator = animator.createAnimator(this);
		this.shader = shader.createShader(this);
	}

	public void update()
	{
		this.animator.update();
	}

	public boolean isAlive()
	{
		return this.animator.isAlive();
	}

	@Nonnull
	public NBTTagCompound getTaskData()
	{
		if(this.taskData == null)
			this.taskData = new NBTTagCompound();
		return this.taskData;
	}

	public void updateColor()
	{
		int[] outData = outputDataFactory.get();
		this.shader.shadeColor(Math.max(0.0f, Math.min(1.0f, this.animator.supplyBrightness())), outData);
		this.colorSink.ifPresent(c -> c.accept(outData));
	}

	public void resetColor()
	{
		this.colorSink.ifPresent(c -> c.accept(new int[0]));
	}
}