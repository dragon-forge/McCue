package com.zeitheron.mccue.api.base;

import com.zeitheron.mccue.api.RgbTask;
import com.zeitheron.mccue.api.sdk.IBaseSDK;
import com.zeitheron.mccue.api.sdk.RgbSdkRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RgbTaskEntry
		implements INBTSerializable<NBTTagCompound>
{
	public static final Map<String, Function<NBTTagCompound, ? extends RgbTaskEntry>> CUSTOM_FACTORY = new HashMap<String, Function<NBTTagCompound, ? extends RgbTaskEntry>>();
	protected String type;
	protected RgbTrigger trigger;
	protected RgbAnimatorEntry animator;
	protected RgbShaderEntry shader;
	protected NBTTagCompound taskData;
	protected IBaseSDK sdk;
	protected String sdkLed;

	public static RgbTaskEntry load(NBTTagCompound comp)
	{
		Function<NBTTagCompound, ? extends RgbTaskEntry> $;
		if(comp.hasKey("type", 8) && ($ = CUSTOM_FACTORY.get(comp.getString("type"))) != null)
		{
			return $.apply(comp);
		}
		try
		{
			return new RgbTaskEntry(comp);
		} catch(Throwable er)
		{
			er.printStackTrace();
			return null;
		}
	}

	protected RgbTaskEntry(NBTTagCompound nbt)
	{
		this.deserializeNBT(nbt);
	}

	public RgbTaskEntry(RgbTrigger trigger, RgbAnimatorEntry animator, RgbShaderEntry shader, @Nullable NBTTagCompound taskData, IBaseSDK sdk, String led)
	{
		this.trigger = trigger;
		this.animator = animator;
		this.shader = shader;
		this.taskData = taskData;
		this.sdk = sdk;
		this.sdkLed = led;
	}

	public RgbTask createTask()
	{
		return new RgbTask(this.trigger, this.animator, this.shader, this.sdk.createRgbSink(this.sdkLed), this.taskData != null ? this.taskData.copy() : null);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("trigger", this.trigger.getId().toString());
		nbt.setString("animator", this.animator.getId().toString());
		nbt.setString("shader", this.shader.getId().toString());
		nbt.setTag("data", this.taskData);
		nbt.setString("sdk", this.sdk.getSdkId().toString());
		nbt.setString(this.sdk.dataLedAddress(), this.sdkLed);
		if(this.type != null)
		{
			nbt.setString("type", this.type);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.trigger = RgbRegistry.getTriggerById(new ResourceLocation(nbt.getString("trigger")));
		this.animator = RgbRegistry.getAnimatorById(new ResourceLocation(nbt.getString("animator")));
		this.shader = RgbRegistry.getShaderById(new ResourceLocation(nbt.getString("shader")));
		this.sdk = RgbSdkRegistry.getSdk(new ResourceLocation(nbt.getString("sdk")));
		this.sdkLed = nbt.getString(this.sdk.dataLedAddress());
		if(nbt.hasKey("type", 8))
		{
			this.type = nbt.getString("type");
		}
		if(nbt.hasKey("data", 10))
		{
			this.taskData = nbt.getCompoundTag("data");
		}
	}

	public RgbAnimatorEntry getAnimator()
	{
		return this.animator;
	}

	public RgbShaderEntry getShader()
	{
		return this.shader;
	}

	public RgbTrigger getTrigger()
	{
		return this.trigger;
	}

	public NBTTagCompound getTaskData()
	{
		return this.taskData;
	}

	public String getType()
	{
		return this.type;
	}

	public IBaseSDK getSdk()
	{
		return this.sdk;
	}

	public String getSdkLed()
	{
		return this.sdkLed;
	}

	public boolean isValid()
	{
		return this.trigger != null && this.animator != null && this.shader != null;
	}
}