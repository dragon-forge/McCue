package com.zeitheron.mccue.api.sdk;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface IBaseSDK
{
	boolean isActive();

	ResourceLocation getSdkId();

	String sdkName();

	IRgbDispatcher getDispatcher();

	String dataLedAddress();

	Optional<Consumer<int[]>> createRgbSink(String var1);

	ICalibrations calibrations();

	default List<String> getAllLeds()
	{
		return Collections.emptyList();
	}

	default String getPeripheralByLed(String led)
	{
		return led;
	}

	@SideOnly(value = Side.CLIENT)
	default String getLedName(String led)
	{
		String unloc = "rgbsdk." + this.getSdkId() + ".led." + led;
		if(I18n.hasKey(unloc))
		{
			return I18n.format(unloc);
		}
		return led;
	}

	default Optional<SDKControlStack> pushStack(String modid)
	{
		SDKControlStack stack = null;
		ModContainer mc = Loader.instance().getIndexedModList().get(modid);
		if(mc != null && getActiveStack() == null || getActiveStack().isClosed())
			stack = new SDKControlStack(mc, this);
		return Optional.ofNullable(stack);
	}

	SDKControlStack getActiveStack();

	void setActiveStack(SDKControlStack stack);
}