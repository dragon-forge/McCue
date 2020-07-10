package com.zeitheron.mccue.api.sdk;

import net.minecraftforge.fml.common.ModContainer;

import java.util.List;

public final class SDKControlStack
		implements AutoCloseable
{
	public final ModContainer controllingMod;
	public final IBaseSDK sdk;
	public final EnumSDKMode mode;
	private boolean isClosed;

	SDKControlStack(ModContainer controllingMod, IBaseSDK sdk)
	{
		this.controllingMod = controllingMod;
		this.sdk = sdk;
		this.mode = sdk.getDispatcher().getSDKMode();
		sdk.setActiveStack(this);
		sdk.getDispatcher().setSDKMode(EnumSDKMode.DISABLE_EVENT_BASE);
	}

	public boolean isClosed()
	{
		return isClosed;
	}

	@Override
	public void close()
	{
		isClosed = true;
		sdk.setActiveStack(null);
		sdk.getDispatcher().setSDKMode(mode);
	}

	public IRgbDispatcher getDispatcher()
	{
		return sdk.getDispatcher();
	}

	public List<? extends IRGBPosition> getPositions()
	{
		return getDispatcher().getAllRGBPositions();
	}

	public IBaseSDK pop()
	{
		close();
		return sdk;
	}
}