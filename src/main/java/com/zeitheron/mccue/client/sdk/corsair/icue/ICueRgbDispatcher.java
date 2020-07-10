package com.zeitheron.mccue.client.sdk.corsair.icue;

import com.google.common.base.Predicates;
import com.zeitheron.mccue.api.RgbTask;
import com.zeitheron.mccue.api.base.RgbRegistry;
import com.zeitheron.mccue.api.base.RgbTaskEntry;
import com.zeitheron.mccue.api.base.RgbTrigger;
import com.zeitheron.mccue.api.sdk.EnumSDKMode;
import com.zeitheron.mccue.api.sdk.IRGBPosition;
import com.zeitheron.mccue.api.sdk.IRgbDispatcher;
import net.minecraft.nbt.NBTTagCompound;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.*;

public class ICueRgbDispatcher
		implements IRgbDispatcher
{
	public static final Map<LedId, RgbTask> ACTIVE_LED = new HashMap<LedId, RgbTask>();
	public static final Map<LedId, RgbTask> QUEUE_LED = new HashMap<LedId, RgbTask>();
	final CueSDK sdk;
	final Set<LedId> removedLT = new HashSet<LedId>();
	final List<? extends IRGBPosition> allIds = Arrays.asList(LedId.values());
	private EnumSDKMode mode = EnumSDKMode.EVENT_BASED;

	public ICueRgbDispatcher(CueSDK sdk)
	{
		this.sdk = sdk;
	}

	@Override
	public void reload()
	{
		ACTIVE_LED.values().forEach(RgbTask::resetColor);
		ACTIVE_LED.clear();
	}

	@Override
	public void onTrigger(RgbTrigger trigger, @Nullable NBTTagCompound triggerInfo)
	{
		RgbRegistry.RGB_TASKS.stream().filter(entry -> entry.getTrigger() == trigger && entry.getSdk() == this.sdk && entry.getTrigger().triggerMatches(entry, triggerInfo)).map(RgbTaskEntry::createTask).filter(Predicates.notNull()).forEach(this::addTask);
	}

	@Override
	public void tick()
	{
		if(!mode.enableEvents())
		{
			ACTIVE_LED.clear();
			QUEUE_LED.clear();
			return;
		}

		ACTIVE_LED.keySet().removeIf(id ->
		{
			RgbTask fx = ACTIVE_LED.get(id);
			if(fx == null)
			{
				this.removedLT.add(id);
				this.sdk.setLedColor(new LedColor(id, Color.BLACK));
				return true;
			}
			fx.update();
			if(!fx.isAlive())
			{
				fx.resetColor();
				this.removedLT.add(id);
				return true;
			}
			return false;
		});
		this.removedLT.clear();
		QUEUE_LED.keySet().stream().filter(id -> ACTIVE_LED.get(id) == null).forEach(id ->
		{
			ACTIVE_LED.put(id, QUEUE_LED.get(id));
			this.removedLT.add(id);
		});
		this.removedLT.forEach(QUEUE_LED::remove);
	}

	@Override
	public void updateRgb()
	{
		if(!mode.enableEvents())
		{
			ACTIVE_LED.clear();
			QUEUE_LED.clear();
			return;
		}

		if(ACTIVE_LED.isEmpty()) return;

		ACTIVE_LED.values().forEach(task ->
		{
			if(task != null)
			{
				task.updateColor();
			}
		});

		this.sdk.flushLedQueue();
	}

	@Override
	public void addTask(RgbTask task)
	{
		// do not even accept tasks
		if(!mode.enableEvents()) return;

		CueSDK.ICueSDKColorSink sink = (CueSDK.ICueSDKColorSink) task.colorSink;
		LedId id = sink.id;
		QUEUE_LED.put(id, task);
	}

	@Override
	public CueSDK getSdk()
	{
		return this.sdk;
	}

	@Override
	public List<? extends IRGBPosition> getAllRGBPositions()
	{
		return allIds;
	}

	@Override
	public void setSDKMode(EnumSDKMode mode)
	{
		this.mode = mode;
	}

	@Override
	public EnumSDKMode getSDKMode()
	{
		return mode;
	}
}