package com.zeitheron.mccue.client.gui.base;

import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.mccue.api.base.RgbRegistry;
import com.zeitheron.mccue.api.base.RgbShaderEntry;
import com.zeitheron.mccue.client.gui.GuiCreateTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class GuiCreateTaskSelectShader
		extends GuiScrollingListBoundaried
{
	GuiCreateTask gui;
	RgbShaderEntry selected;

	public GuiCreateTaskSelectShader(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight, GuiCreateTask gui)
	{
		super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
		this.gui = gui;
		this.selected = gui.shader;
	}

	@Override
	protected int getSize()
	{
		return RgbRegistry.ALL_SHADERS.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick)
	{
		RgbShaderEntry psel = this.selected;
		this.selected = RgbRegistry.ALL_SHADERS.get(index);
		if(psel == this.selected)
		{
			this.gui.shader = RgbRegistry.ALL_SHADERS.get(index);
			this.gui.overlay = null;
		}
	}

	@Override
	protected boolean isSelected(int index)
	{
		return this.selected == RgbRegistry.ALL_SHADERS.get(index);
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
		RgbRegistry.ALL_SHADERS.get(slotIdx).bindTexture();
		RenderUtil.drawFullTexturedModalRect((float) entryRight - (float) (this.listWidth + 7) / 1.65f, slotTop, 32.0, 32.0);
	}
}