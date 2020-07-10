package com.zeitheron.mccue.client.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;

import java.util.List;

public class GuiScrollingTextList
		extends GuiScrollingListBoundaried
{
	public FontRenderer font;
	private List<String> lines;
	public int color = 16777215;

	public GuiScrollingTextList(Minecraft client, int width, int height, int top, int bottom, int left, FontRenderer font, int screenWidth, int screenHeight, List<String> lines)
	{
		super(client, width, height, top, bottom, left, font.FONT_HEIGHT + 1, screenWidth, screenHeight);
		this.font = font;
		this.lines = lines;
	}

	public void splitText()
	{
		for(int i = 0; i < this.lines.size(); ++i)
		{
			List<String> subs;
			String ln = this.lines.get(i);
			if(this.font.getStringWidth(ln) <= this.listWidth - 12 || (subs = this.font.listFormattedStringToWidth(ln, this.listWidth - 12)).size() <= 0)
				continue;
			this.lines.set(i, subs.get(0));
			for(int j = 1; j < subs.size(); ++j) this.lines.add(i + 1, (String) subs.get(j));
		}
	}

	@Override
	protected int getSize()
	{
		return this.lines.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick)
	{
	}

	@Override
	protected boolean isSelected(int index)
	{
		return false;
	}

	@Override
	protected void drawBackground()
	{
	}

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
	{
		String txt = this.lines.get(slotIdx);
		this.font.drawString(txt, entryRight - this.listWidth + 10, slotTop, this.color);
	}
}