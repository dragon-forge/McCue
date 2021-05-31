package org.zeith.mccue.api.sdk;

public enum EnumSDKMode
{
	EVENT_BASED,
	DISABLE_EVENT_BASE;

	public boolean enableEvents()
	{
		return this == EVENT_BASED;
	}
}