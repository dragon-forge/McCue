package org.zeith.mccue.client.sdk.logitech.lew;

import com.zeitheron.hammercore.cfg.file1132.Configuration;
import org.zeith.mccue.McCue;
import org.zeith.mccue.api.KnownRGBSDK;
import org.zeith.mccue.api.sdk.*;
import org.zeith.mccue.client.sdk.logitech.lew.jna.KeyName;
import org.zeith.mccue.client.sdk.logitech.lew.jna.LogiLedLibrary;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class LogiLED
		implements IBaseSDK
{
	boolean active = true;
	private final List<String> leds;
	public final LogiRGBDispatcher dispatcher = new LogiRGBDispatcher(this);

	public LogiLED(Configuration cfg)
	{
		if(!RgbSdkRegistry.enableSDK(cfg, this))
		{
			active = false;
			this.leds = Collections.emptyList();
			return;
		}

		List<String> leds = new ArrayList<>();
		leds.add("2");
		for(int key : KeyName.values())
			leds.add("4_" + key);
		this.leds = Collections.unmodifiableList(leds);

		try
		{
			if(!LogiLedLibrary.INSTANCE.LogiLedInit())
				throw new RuntimeException("Failed to initialize LogitechGSDK.");
			if(!LogiLedLibrary.INSTANCE.LogiLedSetTargetDevice(6))
				throw new RuntimeException("Failed to detect any RGB device.");
			active = true;
		} catch(Throwable err)
		{
			active = false;
			McCue.LOG.error("Logitech RGB error: "  + err.getMessage());
		}
	}

	@Override
	public boolean isActive()
	{
		return active;
	}

	@Override
	public ResourceLocation getSdkId()
	{
		return KnownRGBSDK.LOGITECH_LED;
	}

	@Override
	public String sdkName()
	{
		return "LogitechG LED";
	}

	@Override
	public IRgbDispatcher getDispatcher()
	{
		return dispatcher;
	}

	@Override
	public String dataLedAddress()
	{
		return "LOGI_led";
	}

	@Override
	public List<String> getAllLeds()
	{
		return leds;
	}

	@Override
	public Optional<Consumer<int[]>> createRgbSink(String led)
	{
		try
		{
			return Optional.of(new LogiLEDColorSink(this, led));
		} catch(Throwable err)
		{
			return Optional.empty();
		}
	}

	@Override
	public ICalibrations calibrations()
	{
		return ICalibrations.DUMMY;
	}

	SDKControlStack theStack;

	@Override
	public SDKControlStack getActiveStack()
	{
		return theStack;
	}

	@Override
	public void setActiveStack(SDKControlStack stack)
	{
		if(theStack == null)
			theStack = stack;
		else if(stack == null && theStack.isClosed())
			theStack = null;
	}

	public static class LogiLEDColorSink
			implements Consumer<int[]>
	{
		public final LogiLED sdk;
		public final int targetDevice;
		public final int subidx;
		public final String sid;

		public LogiLEDColorSink(LogiLED sdk, String id)
		{
			this.sdk = sdk;
			this.sid = id;

			// for keyboards, a bit more complex handler
			if(id.startsWith("4_"))
			{
				subidx = Integer.parseInt(id.substring(2));
				id = "4";
			} else subidx = -1;

			this.targetDevice = Integer.parseInt(id);

			if(targetDevice != 4 && targetDevice != 2)
				throw new IllegalArgumentException("Invalid Logitech device pointer " + id);
		}

		@Override
		public void accept(int[] color)
		{
			if(LogiLedLibrary.INSTANCE.LogiLedSetTargetDevice(targetDevice))
			{
				int r = 100, g = 100, b = 100;

				if(color.length == 0)
				{
					r = 0;
					g = 0;
					b = 0;
				} else if(color.length >= 3)
				{
					r = color[0];
					g = color[1];
					b = color[2];
				}

				if(targetDevice == 2)
					LogiLedLibrary.INSTANCE.LogiLedSetLighting(r, g, b);
				else if(targetDevice == 4)
					LogiLedLibrary.INSTANCE.LogiLedSetLightingForKeyWithKeyName(subidx, r, g, b);
			}
		}
	}
}