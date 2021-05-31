package org.zeith.mccue.client.sdk.corsair.icue;

import org.zeith.mccue.client.sdk.corsair.icue.jna.CorsairDeviceInfo;
import org.zeith.mccue.client.sdk.corsair.icue.jna.CorsairDeviceType;
import org.zeith.mccue.client.sdk.corsair.icue.jna.CorsairLogicalLayout;
import org.zeith.mccue.client.sdk.corsair.icue.jna.CorsairPhysicalLayout;

import java.io.Serializable;
import java.util.Objects;

public class DeviceInfo
		implements Serializable
{
	private int deviceIndex;
	private CorsairDeviceType type;
	private String model;
	private CorsairPhysicalLayout physicalLayout;
	private CorsairLogicalLayout logicalLayout;
	private int capsMask;

	public DeviceInfo()
	{
	}

	public DeviceInfo(int deviceIndex, CorsairDeviceInfo deviceInfo)
	{
		this.deviceIndex = deviceIndex;
		this.type = CorsairDeviceType.byOrdinal(deviceInfo.type);
		this.model = deviceInfo.model.getString(0L);
		this.physicalLayout = CorsairPhysicalLayout.byOrdinal(deviceInfo.physicalLayout);
		this.logicalLayout = CorsairLogicalLayout.byOrdinal(deviceInfo.logicalLayout);
		this.capsMask = deviceInfo.capsMask;
	}

	public int getDeviceIndex()
	{
		return deviceIndex;
	}

	public CorsairDeviceType getType()
	{
		return this.type;
	}

	public String getModel()
	{
		return this.model;
	}

	public CorsairPhysicalLayout getPhysicalLayout()
	{
		return this.physicalLayout;
	}

	public CorsairLogicalLayout getLogicalLayout()
	{
		return this.logicalLayout;
	}

	public int getCapsMask()
	{
		return this.capsMask;
	}

	public boolean hasCapability(DeviceCaps cap)
	{
		return (this.capsMask | cap.ordinal()) == cap.ordinal();
	}

	public boolean equals(Object obj)
	{
		if(obj instanceof DeviceInfo)
		{
			DeviceInfo i = (DeviceInfo) obj;
			return this.type == i.type && this.capsMask == i.capsMask && this.logicalLayout == i.logicalLayout && Objects.equals(this.model, i.model) && this.physicalLayout == i.physicalLayout;
		}
		return false;
	}
}