package com.zeitheron.mccue.client.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.GuiScrollingList;

public abstract class GuiScrollingListBoundaried
		extends GuiScrollingList
{
	public GuiScrollingListBoundaried(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight)
	{
		super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
	}

	public boolean isHovering(int mouseX, int mouseY)
	{
		return mouseX >= this.left && mouseX <= this.left + this.listWidth && mouseY >= this.top && mouseY <= this.bottom;
	}
}