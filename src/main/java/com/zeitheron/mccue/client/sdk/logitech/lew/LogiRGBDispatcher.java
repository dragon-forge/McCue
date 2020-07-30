package com.zeitheron.mccue.client.sdk.logitech.lew;

import com.google.common.base.Predicates;
import com.zeitheron.hammercore.utils.color.ColorHelper;
import com.zeitheron.mccue.api.RgbTask;
import com.zeitheron.mccue.api.base.RgbRegistry;
import com.zeitheron.mccue.api.base.RgbTaskEntry;
import com.zeitheron.mccue.api.base.RgbTrigger;
import com.zeitheron.mccue.api.sdk.EnumSDKMode;
import com.zeitheron.mccue.api.sdk.IBaseSDK;
import com.zeitheron.mccue.api.sdk.IRGBPosition;
import com.zeitheron.mccue.api.sdk.IRgbDispatcher;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class LogiRGBDispatcher
		implements IRgbDispatcher
{
	private static final Map<String, RgbTask> ACTIVE_LED = new HashMap<>();
	private static final Map<String, RgbTask> QUEUE_LED = new HashMap<>();
	private final Set<String> removedLT = new HashSet<>();
	private final LogiLED sdk;
	private List<LogiRGBPos> rgbPositions;
	private EnumSDKMode mode;

	public LogiRGBDispatcher(LogiLED sdk)
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
		RgbRegistry.RGB_TASKS
				.stream()
				.filter(entry -> entry.getTrigger() == trigger && entry.getSdk() == this.sdk && entry.getTrigger().triggerMatches(entry, triggerInfo))
				.map(RgbTaskEntry::createTask)
				.filter(Predicates.notNull())
				.forEach(this::addTask);
	}

	@Override
	public void tick()
	{
		ACTIVE_LED.keySet().removeIf(id ->
		{
			RgbTask fx = ACTIVE_LED.get(id);
			if(fx == null)
			{
				this.removedLT.add(id);
				this.sdk.createRgbSink(id).ifPresent(c -> c.accept(new int[0]));
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
	}

	@Override
	public void addTask(RgbTask task)
	{
		// do not even accept tasks
		if(!mode.enableEvents()) return;

		task.colorSink.ifPresent(gsink ->
		{
			LogiLED.LogiLEDColorSink sink = (LogiLED.LogiLEDColorSink) gsink;
			QUEUE_LED.put(sink.sid, task);
		});
	}

	@Override
	public IBaseSDK getSdk()
	{
		return sdk;
	}

	@Override
	public List<? extends IRGBPosition> getAllRGBPositions()
	{
		if(rgbPositions == null)
		{
			List<String> pos = sdk.getAllLeds();
			rgbPositions = new ArrayList<>(pos.size());
			for(int i = 0; i < pos.size(); i++)
				rgbPositions.add(new LogiRGBPos(i));
			rgbPositions = Collections.unmodifiableList(rgbPositions);
		}
		return rgbPositions;
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

	public class LogiRGBPos
			implements IRGBPosition
	{
		final int id;
		final Consumer<int[]> sink;

		public LogiRGBPos(int id)
		{
			this.id = id;
			this.sink = sdk.createRgbSink(sdk.getAllLeds().get(id)).orElse(null);
		}

		@Override
		public int id()
		{
			return id;
		}

		@Override
		public boolean set(int rgb)
		{
			if(sink != null)
			{
				sink.accept(new int[]{
						(int) (ColorHelper.getRed(rgb) * 255),
						(int) (ColorHelper.getGreen(rgb) * 255),
						(int) (ColorHelper.getBlue(rgb) * 255)
				});
				return true;
			}
			return false;
		}
	}
}
