package com.zeitheron.mccue.client.sdk.corsair.icue;

import com.zeitheron.mccue.api.sdk.IBaseSDK;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface ICueSDK
		extends IBaseSDK
{
	default boolean isActive()
	{
		return false;
	}

	default int getDeviceCount()
	{
		return 0;
	}

	default List<DeviceInfo> getDevices()
	{
		return Collections.emptyList();
	}

	default DeviceInfo getDeviceInfo(int deviceIndex)
	{
		return null;
	}

	default List<LedPosition> getLedPositions()
	{
		return Collections.emptyList();
	}

	default void setLedsColors(Collection<LedColor> ledColors)
	{
	}

	default void setLedColor(LedColor ledColor)
	{
	}

	default boolean setLedColorSafe(LedColor ledColor)
	{
		return false;
	}

	default void queueLed(LedColor color)
	{
	}

	default void flushLedQueue()
	{
	}
}