package org.zeith.mccue.api.sdk;

import org.zeith.mccue.api.RgbTask;
import org.zeith.mccue.api.base.RgbTrigger;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public interface IRgbDispatcher
{
	void reload();

	void onTrigger(RgbTrigger var1, @Nullable NBTTagCompound var2);

	void tick();

	void updateRgb();

	void addTask(RgbTask var1);

	IBaseSDK getSdk();

	List<? extends IRGBPosition> getAllRGBPositions();

	void setSDKMode(EnumSDKMode mode);

	EnumSDKMode getSDKMode();
}