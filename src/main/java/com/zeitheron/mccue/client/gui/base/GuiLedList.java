package com.zeitheron.mccue.client.gui.base;

import com.zeitheron.mccue.api.sdk.IBaseSDK;
import com.zeitheron.mccue.api.sdk.RgbSdkRegistry;
import com.zeitheron.mccue.client.gui.GuiCreateTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.List;
import java.util.stream.Collectors;

public class GuiLedList
		extends GuiScrollingList
{
	SdkLedInfo sel;
	public List<SdkLedInfo> leds;
	GuiCreateTask gui;

	public GuiLedList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight, GuiCreateTask gui)
	{
		super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
		this.gui = gui;
		this.leds = RgbSdkRegistry.SDKS.stream().flatMap(sdk -> sdk.getAllLeds().stream().map(led -> new SdkLedInfo(sdk, led))).collect(Collectors.toList());
		this.sel = this.leds.stream().filter(inf -> inf.sdk.getSdkId().toString().equals(gui.sdk) && inf.led.equals(gui.led)).findFirst().orElse(null);
	}

	@Override
	protected int getSize()
	{
		return this.leds.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick)
	{
		this.sel = this.leds.get(index);
		this.gui.selectLed(this.sel);
	}

	@Override
	protected boolean isSelected(int index)
	{
		return this.leds.get(index) == this.sel;
	}

	@Override
	protected void drawBackground()
	{
	}

	@Override
	protected int getContentHeight()
	{
		return this.getSize() * 21 + 1;
	}

	@Override
	protected void drawSlot(int idx, int right, int top, int height, Tessellator tess)
	{
		GlStateManager.enableBlend();
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		SdkLedInfo info = this.leds.get(idx);
		String perip = info.sdk.getPeripheralByLed(info.led);
		String led = info.sdk.getLedName(info.led);
		font.drawString(perip, 4, top, 16777215);
		font.drawString(led, 4, top + font.FONT_HEIGHT, 16777215);
	}

	public static class SdkLedInfo
	{
		public final IBaseSDK sdk;
		public final String led;

		public SdkLedInfo(IBaseSDK sdk, String btn)
		{
			this.sdk = sdk;
			this.led = btn;
		}
	}
}