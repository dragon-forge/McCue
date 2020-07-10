package com.zeitheron.mccue.api.event;

import com.zeitheron.mccue.api.base.RgbRegistry;
import com.zeitheron.mccue.api.base.RgbTaskEntry;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RegisterRgbTasksEvent
		extends Event
{
	public void registerEntry(RgbTaskEntry entry)
	{
		RgbRegistry.RGB_TASKS.add(entry);
	}
}