package com.zeitheron.mccue.client.sdk.corsair.icue;

import com.sun.jna.Pointer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.mccue.McCue;
import com.zeitheron.mccue.api.KnownRGBSDK;
import com.zeitheron.mccue.api.sdk.ICalibrations;
import com.zeitheron.mccue.api.sdk.IRgbDispatcher;
import com.zeitheron.mccue.api.sdk.RgbSdkRegistry;
import com.zeitheron.mccue.api.sdk.SDKControlStack;
import com.zeitheron.mccue.client.sdk.corsair.icue.jna.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;

public class CueSDK
		implements ICueSDK
{
	public static CueSDK icueInstance;
	boolean active = true;
	public ICueCalibrationPresets presets;
	private final CUESDKLibrary instance = CUESDKLibrary.INSTANCE;
	private final ICueRgbDispatcher dispatcher = new ICueRgbDispatcher(this);
	public List<DeviceInfo> currentDevices;
	List<LedColor> ledQueue = new ArrayList<LedColor>();
	ResourceLocation id = KnownRGBSDK.CORSAIR_CUE;

	public CueSDK(Configuration cfg)
	{
		this(false, cfg);
	}

	public CueSDK(boolean exclusiveLightingControl, Configuration cfg)
	{
		if(!RgbSdkRegistry.enableSDK(cfg, this))
		{
			active = false;
			return;
		}

		CorsairProtocolDetails.ByValue protocolDetails = this.instance.CorsairPerformProtocolHandshake();
		if(protocolDetails.serverProtocolVersion == 0)
			this.handleError();
		if(protocolDetails.breakingChanges != 0)
		{
			String sdkVersion = protocolDetails.sdkVersion.getString(0L);
			String cueVersion = protocolDetails.serverVersion.getString(0L);
			System.err.println("Incompatible SDK (" + sdkVersion + ") and CUE " + cueVersion + " versions.");
			this.active = false;
		}
		if(exclusiveLightingControl && this.instance.CorsairRequestControl(0) != 1)
			this.handleError();
		CUESDKLibrary.INSTANCE.CorsairSubscribeForEvents((ctx, evt) ->
		{
			if(evt.id == 1)
			{
				List<DeviceInfo> newDevices = this.getDevices();
				if(newDevices.size() > this.currentDevices.size())
				{
					newDevices.removeAll(this.currentDevices);
					if(this.presets != null) newDevices.forEach(this.presets::calibrate);
				}
				this.currentDevices = this.getDevices();
			}
		}, new Pointer(0L));
		this.presets = new ICueCalibrationPresets();
		File calibration = new File(McCue.modCfgDir, "icue_callib.nbt");
		try
		{
			this.presets.read(calibration);
		} catch(IOException e1)
		{
			e1.printStackTrace();
		}
		this.getDevices().forEach(this.presets::calibrate);
		icueInstance = this;
	}

	@Override
	public int getDeviceCount()
	{
		return this.instance.CorsairGetDeviceCount();
	}

	@Override
	public List<DeviceInfo> getDevices()
	{
		ArrayList<DeviceInfo> devices = new ArrayList<DeviceInfo>();
		int c = this.getDeviceCount();
		for(int i = 0; i < c; ++i)
		{
			devices.add(this.getDeviceInfo(i));
		}
		return devices;
	}

	@Override
	public DeviceInfo getDeviceInfo(int deviceIndex)
	{
		return new DeviceInfo(this.instance.CorsairGetDeviceInfo(deviceIndex));
	}

	@Override
	public List<LedPosition> getLedPositions()
	{
		CorsairLedPositions corsairLedPositions = this.instance.CorsairGetLedPositions();
		ArrayList<LedPosition> ledPositions = new ArrayList<LedPosition>();
		if(corsairLedPositions != null && corsairLedPositions.numberOfLed > 0)
		{
			int count = corsairLedPositions.numberOfLed;
			CorsairLedPosition.ByReference pLedPosition = corsairLedPositions.pLedPosition;
			CorsairLedPosition[] nativeLedPositions = (CorsairLedPosition[]) pLedPosition.toArray(new CorsairLedPosition[count]);
			ledPositions.ensureCapacity(count);
			for(CorsairLedPosition nativeLedPosition : nativeLedPositions)
				ledPositions.add(new LedPosition(nativeLedPosition));
		}
		return ledPositions;
	}

	@Override
	public List<LedPosition> getLedPositions4Device(int deviceIndex)
	{
		CorsairLedPositions corsairLedPositions = this.instance.CorsairGetLedPositionsByDeviceIndex(deviceIndex);
		ArrayList<LedPosition> ledPositions = new ArrayList<LedPosition>();
		if(corsairLedPositions != null && corsairLedPositions.numberOfLed > 0)
		{
			int count = corsairLedPositions.numberOfLed;
			CorsairLedPosition.ByReference pLedPosition = corsairLedPositions.pLedPosition;
			CorsairLedPosition[] nativeLedPositions = (CorsairLedPosition[]) pLedPosition.toArray(new CorsairLedPosition[count]);
			ledPositions.ensureCapacity(count);
			for(CorsairLedPosition nativeLedPosition : nativeLedPositions)
				ledPositions.add(new LedPosition(nativeLedPosition));
		}
		return ledPositions;
	}

	@Override
	public void setLedsColors(Collection<LedColor> ledColors)
	{
		if(ledColors == null || ledColors.isEmpty())
		{
			return;
		}
		Iterator<LedColor> iterator = ledColors.iterator();
		LedColor ledColor = iterator.next();
		if(ledColors.size() == 1)
		{
			this.setLedColor(ledColor);
		} else
		{
			CorsairLedColor[] nativeLedColors = (CorsairLedColor[]) new CorsairLedColor().toArray(ledColors.size());
			int index = 0;
			this.copyCorsairLedColor(ledColor, nativeLedColors[index++]);
			while(iterator.hasNext())
			{
				ledColor = iterator.next();
				this.copyCorsairLedColor(ledColor, nativeLedColors[index++]);
			}
			byte ret = this.instance.CorsairSetLedsColors(nativeLedColors.length, nativeLedColors[0]);
			if(ret != 1)
			{
				this.handleError();
			}
		}
	}

	@Override
	public void setLedColor(LedColor ledColor)
	{
		if(ledColor == null)
		{
			return;
		}
		CorsairLedColor nativeLedColor = new CorsairLedColor();
		this.copyCorsairLedColor(ledColor, nativeLedColor);
		byte ret = this.instance.CorsairSetLedsColors(1, nativeLedColor);
		if(ret != 1)
		{
			this.handleError();
		}
	}

	@Override
	public boolean setLedColorSafe(LedColor ledColor)
	{
		if(ledColor == null)
		{
			return false;
		}
		CorsairLedColor nativeLedColor = new CorsairLedColor();
		this.copyCorsairLedColor(ledColor, nativeLedColor);
		return this.instance.CorsairSetLedsColors(1, nativeLedColor) == 1;
	}

	private void copyCorsairLedColor(LedColor src, CorsairLedColor dst)
	{
		dst.ledId = src.ledId;
		dst.r = src.r;
		dst.g = src.g;
		dst.b = src.b;
	}

	private void handleError()
	{
		int errorId = this.instance.CorsairGetLastError();
		CorsairError error = CorsairError.byOrdinal(errorId);
		if(error != CorsairError.CE_Success)
		{
			this.active = false;
			System.out.println(error);
		}
	}

	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public Optional<Consumer<int[]>> createRgbSink(String led)
	{
		try
		{
			return Optional.of(new ICueSDKColorSink(this, LedId.valueOf(led)));
		} catch(Throwable err)
		{
			return Optional.empty();
		}
	}

	@Override
	public void queueLed(LedColor color)
	{
		this.ledQueue.add(color);
	}

	@Override
	public void flushLedQueue()
	{
		this.setLedsColors(this.ledQueue);
		this.ledQueue.clear();
	}

	@Override
	public IRgbDispatcher getDispatcher()
	{
		return this.dispatcher;
	}

	@Override
	public String getPeripheralByLed(String led)
	{
		try
		{
			LedId id = LedId.valueOf(led);
			return I18n.format("corsair." + id.type().name().substring(4).toLowerCase());
		} catch(Throwable id)
		{
			return ICueSDK.super.getPeripheralByLed(led);
		}
	}

	@Override
	public String sdkName()
	{
		return "Corsair iCue";
	}

	@Override
	public List<String> getAllLeds()
	{
		return this.presets.leds();
	}

	@Override
	public String dataLedAddress()
	{
		return "ICUE_led";
	}

	@Override
	public ICalibrations calibrations()
	{
		return this.presets;
	}

	@Override
	public ResourceLocation getSdkId()
	{
		return this.id;
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

	public static class ICueSDKColorSink
			implements Consumer<int[]>
	{
		public final LedId id;
		public final LedColor led;
		public final CueSDK sdk;

		public ICueSDKColorSink(CueSDK sdk, LedId id)
		{
			this.sdk = sdk;
			this.id = id;
			this.led = new LedColor(id, Color.BLACK);
		}

		@Override
		public void accept(int[] color)
		{
			if(color.length == 0)
			{
				this.led.r = 0;
				this.led.g = 0;
				this.led.b = 0;
			} else if(color.length >= 3)
			{
				this.led.r = color[0];
				this.led.g = color[1];
				this.led.b = color[2];
			}
			this.sdk.setLedColorSafe(this.led);
		}
	}
}