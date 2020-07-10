package com.zeitheron.mccue.api.event;

import com.zeitheron.mccue.api.base.RgbTaskEntry;
import net.minecraftforge.fml.common.eventhandler.Event;

public class UnregisterRgbTasksEvent
		extends Event
{
	public final RgbTaskEntry entry;

	public UnregisterRgbTasksEvent(RgbTaskEntry entry)
	{
		this.entry = entry;
	}
}