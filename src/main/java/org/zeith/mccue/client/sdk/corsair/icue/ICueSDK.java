package org.zeith.mccue.client.sdk.corsair.icue;

import org.zeith.mccue.api.sdk.IBaseSDK;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface ICueSDK
		extends IBaseSDK
{
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

	default List<LedPosition> getLedPositions4Device(int deviceIndex)
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