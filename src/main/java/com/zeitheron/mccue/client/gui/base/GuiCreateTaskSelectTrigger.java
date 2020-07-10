package com.zeitheron.mccue.client.gui.base;

import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.mccue.api.base.RgbRegistry;
import com.zeitheron.mccue.api.base.RgbTrigger;
import com.zeitheron.mccue.client.gui.GuiCreateTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class GuiCreateTaskSelectTrigger
		extends GuiScrollingListBoundaried
{
	GuiCreateTask gui;
	RgbTrigger selected;

	public GuiCreateTaskSelectTrigger(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight, GuiCreateTask gui)
	{
		super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
		this.gui = gui;
		this.selected = gui.trigger;
	}

	@Override
	protected int getSize()
	{
		return RgbRegistry.ALL_TRIGGERS.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick)
	{
		RgbTrigger psel = this.selected;
		this.selected = RgbRegistry.ALL_TRIGGERS.get(index);
		if(psel == this.selected)
		{
			this.gui.trigger = RgbRegistry.ALL_TRIGGERS.get(index);
			this.gui.overlay = null;
		}
	}

	@Override
	protected boolean isSelected(int index)
	{
		return this.selected == RgbRegistry.ALL_TRIGGERS.get(index);
	}

	@Override
	protected void drawBackground()
	{
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected int getContentHeight()
	{
		return this.getSize() * 39 + 1;
	}

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
	{
		RgbRegistry.ALL_TRIGGERS.get(slotIdx).bindTexture();
		RenderUtil.drawFullTexturedModalRect((float) entryRight - (float) (this.listWidth + 7) / 1.65f, slotTop, 32.0, 32.0);
	}
}