package com.zeitheron.mccue.api.sdk;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public interface IBaseSDK
{
	ResourceLocation getSdkId();

	String sdkName();

	IRgbDispatcher getDispatcher();

	String dataLedAddress();

	Consumer<int[]> createRgbSink(String var1);

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
}