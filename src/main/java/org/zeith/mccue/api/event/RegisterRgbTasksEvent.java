package org.zeith.mccue.api.event;

import org.zeith.mccue.api.base.RgbRegistry;
import org.zeith.mccue.api.base.RgbTaskEntry;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RegisterRgbTasksEvent
		extends Event
{
	public void registerEntry(RgbTaskEntry entry)
	{
		RgbRegistry.RGB_TASKS.add(entry);
	}
}