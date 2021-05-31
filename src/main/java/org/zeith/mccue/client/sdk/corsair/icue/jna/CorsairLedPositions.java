
package org.zeith.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CorsairLedPositions
		extends Structure
{
	public int numberOfLed;
	public CorsairLedPosition.ByReference pLedPosition;

	public CorsairLedPositions()
	{
	}

	@Override
	protected List<String> getFieldOrder()
	{
		return Arrays.asList("numberOfLed", "pLedPosition");
	}

	public CorsairLedPositions(int numberOfLed, CorsairLedPosition.ByReference pLedPosition)
	{
		this.numberOfLed = numberOfLed;
		this.pLedPosition = pLedPosition;
	}

	public static class ByValue
			extends CorsairLedPositions
			implements Structure.ByValue
	{
	}

	public static class ByReference
			extends CorsairLedPositions
			implements Structure.ByReference
	{
	}
}