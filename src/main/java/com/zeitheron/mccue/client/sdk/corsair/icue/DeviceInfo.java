package com.zeitheron.mccue.client.sdk.corsair.icue;

import com.zeitheron.mccue.client.sdk.corsair.icue.jna.CorsairDeviceInfo;

import java.io.Serializable;
import java.util.Objects;

public class DeviceInfo
		implements Serializable
{
	private DeviceType type;
	private String model;
	private PhysicalLayout physicalLayout;
	private LogicalLayout logicalLayout;
	private int capsMask;

	public DeviceInfo()
	{
	}

	public DeviceInfo(CorsairDeviceInfo deviceInfo)
	{
		this.type = DeviceType.byOrdinal(deviceInfo.type);
		this.model = deviceInfo.model.getString(0L);
		this.physicalLayout = PhysicalLayout.byOrdinal(deviceInfo.physicalLayout);
		this.logicalLayout = LogicalLayout.byOrdinal(deviceInfo.logicalLayout);
		this.capsMask = deviceInfo.capsMask;
	}

	public DeviceType getType()
	{
		return this.type;
	}

	public String getModel()
	{
		return this.model;
	}

	public PhysicalLayout getPhysicalLayout()
	{
		return this.physicalLayout;
	}

	public LogicalLayout getLogicalLayout()
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