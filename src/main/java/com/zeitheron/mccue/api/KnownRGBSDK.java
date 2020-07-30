package com.zeitheron.mccue.api;

import com.zeitheron.mccue.api.sdk.IBaseSDK;
import com.zeitheron.mccue.api.sdk.RgbSdkRegistry;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public class KnownRGBSDK
{
	public static final ResourceLocation CORSAIR_CUE = new ResourceLocation("mccue", "icue");
	public static final ResourceLocation LOGITECH_LED = new ResourceLocation("mccue", "logiled");

	public static Optional<IBaseSDK> get(ResourceLocation id)
	{
		return Optional.ofNullable(RgbSdkRegistry.getSdk(id));
	}
}