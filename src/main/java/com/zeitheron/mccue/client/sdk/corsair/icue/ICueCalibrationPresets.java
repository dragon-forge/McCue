package com.zeitheron.mccue.client.sdk.corsair.icue;

import com.zeitheron.mccue.McCue;
import com.zeitheron.mccue.api.sdk.ICalibrations;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ICueCalibrationPresets
		implements ICalibrations
{
	private static final ExecutorService IO = Executors.newFixedThreadPool(1);
	private final Map<String, List<LedId>> calibrated = new HashMap<>();
	private final List<Pair<DeviceInfo, LedId>> toCalibrate = new ArrayList<>();
	File lastFile;

	public void calibrate(DeviceInfo inf)
	{
		if(!this.calibrated.containsKey(inf.getModel()) && inf.hasCapability(DeviceCaps.CDC_Lighting))
			Arrays.stream(LedId.values()).filter(id -> id.type() == inf.getType()).map(id -> Pair.of(inf, id)).forEach(this.toCalibrate::add);
	}

	@Override
	public boolean hasMoreCalibrations()
	{
		return !this.toCalibrate.isEmpty();
	}

	@Override
	public ICalibrations.ICalibrationInstance nextCalibration()
	{
		return this.toCalibrate.isEmpty() ? null : new CalibrationInstance(this.toCalibrate, 0);
	}

	public List<String> leds()
	{
		return this.stream().collect(Collectors.toList());
	}

	public Stream<String> stream()
	{
		return this.calibrated.values().stream().flatMap(list -> list.stream()).map(ledid -> ledid.name());
	}

	public void save(File file) throws IOException
	{
		if(file == null)
			file = this.lastFile;
		if(file == null)
			return;
		this.lastFile = file;
		try
		{
			File f = file;
			IO.submit(() ->
			{
				try
				{
					try(ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(f))))
					{
						out.writeObject(this.calibrated);
						out.writeObject(this.toCalibrate);
					}
				} catch(Throwable err)
				{
					throw new RuntimeException(err);
				}
			}).get();
		} catch(InterruptedException | ExecutionException e)
		{
			if(e.getCause() instanceof RuntimeException && e.getCause().getCause() instanceof IOException)
			{
				throw (IOException) e.getCause().getCause();
			}
			if(e.getCause() != null)
			{
				throw new IOException(e.getCause());
			}
			throw new IOException(e);
		}
	}

	public void read(File file) throws IOException
	{
		this.lastFile = file;
		if(!file.isFile())
			return;
		this.calibrated.clear();
		this.toCalibrate.clear();
		try
		{
			try(ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file))))
			{
				this.calibrated.putAll((Map) in.readObject());
				this.toCalibrate.addAll((List) in.readObject());
			}

			System.out.println(calibrated);
			System.out.println(toCalibrate);
		} catch(ClassNotFoundException e)
		{
			throw new IOException("Something went horribly wrong! ", e);
		}
	}

	public class CalibrationInstance
			implements ICalibrations.ICalibrationInstance
	{
		public final DeviceInfo info;
		public final LedId ledId;
		public final Runnable accept;
		public final Runnable deny;
		private boolean hasCalled = false;

		private CalibrationInstance(List<Pair<DeviceInfo, LedId>> list, int index)
		{
			Pair<DeviceInfo, LedId> pair = list.get(index);
			this.info = pair.getKey();
			this.ledId = pair.getValue();
			this.accept = () ->
			{
				this.hasCalled = true;
				list.remove(pair);
				ArrayList<LedId> ids = (ArrayList<LedId>) ICueCalibrationPresets.this.calibrated.get(this.info.getModel());
				if(ids == null)
				{
					ids = new ArrayList<LedId>();
					ICueCalibrationPresets.this.calibrated.put(this.info.getModel(), ids);
				}
				if(!ids.contains(this.ledId))
				{
					ids.add(this.ledId);
				}
				try
				{
					ICueCalibrationPresets.this.save(new File(McCue.modCfgDir, "icue_callib.bin"));
				} catch(IOException e)
				{
					e.printStackTrace();
				}
			};
			this.deny = () ->
			{
				this.hasCalled = true;
				list.remove(pair);
				try
				{
					ICueCalibrationPresets.this.save(new File(McCue.modCfgDir, "icue_callib.bin"));
				} catch(IOException e)
				{
					e.printStackTrace();
				}
			};
		}

		@Override
		public boolean hasApplied()
		{
			return this.hasCalled;
		}

		@Override
		public void apply()
		{
			this.accept.run();
		}

		@Override
		public void deny()
		{
			this.deny.run();
		}

		@Override
		public List<String> text()
		{
			ArrayList<String> list = new ArrayList<String>();
			String perip = this.peripheralName();
			list.add("Calibrating " + perip + ": " + this.info.getModel());
			list.add("If your device supports LED <" + CueSDK.icueInstance.getLedName(this.ledId.name()) + ">");
			list.add("then it should light up with this color:");
			list.add("Do you see the color appear on your " + perip + "?");
			return list;
		}

		@Override
		public String peripheralName()
		{
			return I18n.format("corsair." + this.info.getType().name().substring(4).toLowerCase());
		}

		@Override
		public void setColor(int color)
		{
			CueSDK.icueInstance.setLedColor(new LedColor(this.ledId, color));
		}
	}
}