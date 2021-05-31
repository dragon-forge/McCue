package org.zeith.mccue.client.sdk.corsair.icue;

import org.zeith.mccue.client.sdk.corsair.icue.jna.CorsairLedId;
import org.zeith.mccue.client.sdk.corsair.icue.jna.CorsairLedPosition;

public class LedPosition
{
	private CorsairLedId ledId;
	private double top;
	private double left;
	private double height;
	private double width;

	public LedPosition(CorsairLedPosition ledPosition)
	{
		this.ledId = CorsairLedId.byOrdinal(ledPosition.ledId);
		this.top = ledPosition.top;
		this.left = ledPosition.left;
		this.height = ledPosition.height;
		this.width = ledPosition.width;
	}

	public CorsairLedId getLedId()
	{
		return this.ledId;
	}

	public void setLedId(CorsairLedId ledId)
	{
		this.ledId = ledId;
	}

	public double getTop()
	{
		return this.top;
	}

	public void setTop(double top)
	{
		this.top = top;
	}

	public double getLeft()
	{
		return this.left;
	}

	public void setLeft(double left)
	{
		this.left = left;
	}

	public double getHeight()
	{
		return this.height;
	}

	public void setHeight(double height)
	{
		this.height = height;
	}

	public double getWidth()
	{
		return this.width;
	}

	public void setWidth(double width)
	{
		this.width = width;
	}
}