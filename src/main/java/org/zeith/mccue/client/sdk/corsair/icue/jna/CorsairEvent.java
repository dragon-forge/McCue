package org.zeith.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Structure;
import com.sun.jna.Union;

import java.util.Arrays;
import java.util.List;

public class CorsairEvent
		extends Structure
{
	public int id;
	public field1_union field1;

	public CorsairEvent()
	{
	}

	@Override
	protected List<String> getFieldOrder()
	{
		return Arrays.asList("id", "field1");
	}

	public CorsairEvent(int id, field1_union field1)
	{
		this.id = id;
		this.field1 = field1;
	}

	public static class ByValue
			extends CorsairEvent
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairEvent
			implements Structure.ByReference
	{
	}

	public static class field1_union
			extends Union
	{
		public CorsairDeviceConnectionStatusChangedEvent.ByReference deviceConnectionStatusChangedEvent;
		public CorsairKeyEvent.ByReference keyEvent;

		public field1_union()
		{
		}

		public field1_union(CorsairDeviceConnectionStatusChangedEvent.ByReference deviceConnectionStatusChangedEvent)
		{
			this.deviceConnectionStatusChangedEvent = deviceConnectionStatusChangedEvent;
			this.setType(CorsairDeviceConnectionStatusChangedEvent.ByReference.class);
		}

		public field1_union(CorsairKeyEvent.ByReference keyEvent)
		{
			this.keyEvent = keyEvent;
			this.setType(CorsairKeyEvent.ByReference.class);
		}

		public static class ByValue
				extends field1_union
				implements Structure.ByValue
		{
		}

		public static class ByReference
				extends field1_union
				implements Structure.ByReference
		{
		}
	}
}