package com.zeitheron.mccue.client.sdk.corsair.icue;

import com.zeitheron.mccue.client.sdk.corsair.icue.jna.CorsairLedColor;

import java.awt.*;

public class LedColor
{
	public int ledId;
	public int r;
	public int g;
	public int b;

	public LedColor(CorsairLedColor ledColor)
	{
		this(ledColor.ledId, ledColor.r, ledColor.g, ledColor.b);
	}

	public LedColor(LedId ledId, int r, int g, int b)
	{
		this(ledId.ordinal(), r, g, b);
	}

	public LedColor(LedId ledId, int rgb)
	{
		this(ledId.ordinal(), rgb >> 16 & 255, rgb >> 8 & 255, rgb >> 0 & 255);
	}

	public LedColor(LedId ledId, Color color)
	{
		this(ledId.ordinal(), color);
	}

	public LedColor(LedPosition ledPosition, int r, int g, int b)
	{
		this(ledPosition.getLedId(), r, g, b);
	}

	public LedColor(LedPosition ledPosition, Color color)
	{
		this(ledPosition.getLedId(), color);
	}

	public LedColor(int ledId, Color color)
	{
		this(ledId, color.getRed(), color.getGreen(), color.getBlue());
	}

	public LedColor(int ledId, int r, int g, int b)
	{
		this.ledId = ledId;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int getLedId()
	{
		return this.ledId;
	}

	public void setLedId(int ledId)
	{
		this.ledId = ledId;
	}

	public int getR()
	{
		return this.r;
	}

	public void setR(int r)
	{
		this.r = r;
	}

	public int getG()
	{
		return this.g;
	}

	public void setG(int g)
	{
		this.g = g;
	}

	public int getB()
	{
		return this.b;
	}

	public void setB(int b)
	{
		this.b = b;
	}
}