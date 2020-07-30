package com.zeitheron.mccue.api.sdk;

import java.util.List;

public interface ICalibrations
{
	ICalibrations DUMMY = new ICalibrations()
	{
		@Override
		public boolean hasMoreCalibrations()
		{
			return false;
		}

		@Override
		public ICalibrationInstance nextCalibration()
		{
			return null;
		}
	};

	boolean hasMoreCalibrations();

	ICalibrationInstance nextCalibration();

	interface ICalibrationInstance
	{
		void apply();

		void deny();

		List<String> text();

		String peripheralName();

		boolean hasApplied();

		default void setColor(int color)
		{
		}

		default void resetLed()
		{
			this.setColor(0);
		}
	}
}