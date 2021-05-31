package org.zeith.mccue.api.event;

import org.zeith.mccue.api.base.RgbTrigger;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RgbTriggerEvent
		extends Event
{
	final RgbTrigger trigger;

	public RgbTriggerEvent(RgbTrigger trigger)
	{
		this.trigger = trigger;
	}

	public RgbTrigger getTrigger()
	{
		return this.trigger;
	}
}