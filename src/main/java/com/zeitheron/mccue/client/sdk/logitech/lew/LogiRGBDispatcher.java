package com.zeitheron.mccue.client.sdk.logitech.lew;

import com.zeitheron.mccue.api.RgbTask;
import com.zeitheron.mccue.api.base.RgbTrigger;
import com.zeitheron.mccue.api.sdk.EnumSDKMode;
import com.zeitheron.mccue.api.sdk.IBaseSDK;
import com.zeitheron.mccue.api.sdk.IRGBPosition;
import com.zeitheron.mccue.api.sdk.IRgbDispatcher;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class LogiRGBDispatcher
		implements IRgbDispatcher
{
	public final LogiLED sdk;

	public LogiRGBDispatcher(LogiLED sdk)
	{
		this.sdk = sdk;
	}

	@Override
	public void reload()
	{
	}

	@Override
	public void onTrigger(RgbTrigger var1, @Nullable NBTTagCompound var2)
	{
	}

	@Override
	public void tick()
	{
	}

	@Override
	public void updateRgb()
	{
	}

	@Override
	public void addTask(RgbTask var1)
	{
	}

	@Override
	public IBaseSDK getSdk()
	{
		return sdk;
	}

	@Override
	public List<? extends IRGBPosition> getAllRGBPositions()
	{
		return Collections.emptyList();
	}

	EnumSDKMode mode;

	@Override
	public void setSDKMode(EnumSDKMode mode)
	{
		this.mode = mode;
	}

	@Override
	public EnumSDKMode getSDKMode()
	{
		return mode;
	}
}
