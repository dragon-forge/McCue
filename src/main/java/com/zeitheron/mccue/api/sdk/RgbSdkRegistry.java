package com.zeitheron.mccue.api.sdk;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RgbSdkRegistry
{
	public static final List<IBaseSDK> SDKS = new ArrayList<>();
	private static final Map<ResourceLocation, IBaseSDK> SDKMap = new HashMap<ResourceLocation, IBaseSDK>();

	public static void registerSDK(Supplier<IBaseSDK> sdk)
	{
		IBaseSDK e = sdk.get();
		if(e != null)
		{
			SDKS.add(e);
			SDKMap.put(e.getSdkId(), e);
		}
	}

	public static IBaseSDK getSdk(ResourceLocation id)
	{
		return SDKMap.get(id);
	}
}