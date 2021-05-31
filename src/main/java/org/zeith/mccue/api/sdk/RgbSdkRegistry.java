package org.zeith.mccue.api.sdk;

import com.zeitheron.hammercore.cfg.file1132.Configuration;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RgbSdkRegistry
{
	public static final List<IBaseSDK> SDKS = new ArrayList<>();
	private static final Map<ResourceLocation, IBaseSDK> SDKMap = new HashMap<ResourceLocation, IBaseSDK>();

	private static final List<Function<Configuration, IBaseSDK>> FACTORIES = new ArrayList<>();

	public static void registerSDK(Function<Configuration, IBaseSDK> sdk)
	{
		FACTORIES.add(sdk);
	}

	public static void init(Configuration cfg)
	{
		for(Function<Configuration, IBaseSDK> sdk : FACTORIES)
		{
			IBaseSDK e = sdk.apply(cfg);
			if(e != null)
			{
				SDKS.add(e);
				SDKMap.put(e.getSdkId(), e);
			}
		}

		FACTORIES.clear();
	}

	public static boolean enableSDK(Configuration cfg, IBaseSDK sdk)
	{
		return cfg.getCategory(sdk.sdkName()).getBooleanEntry("Enable", true).setDescription("Should support for " + sdk.sdkName() + " be active or not?").getValue();
	}

	public static IBaseSDK getSdk(ResourceLocation id)
	{
		return SDKMap.get(id);
	}
}