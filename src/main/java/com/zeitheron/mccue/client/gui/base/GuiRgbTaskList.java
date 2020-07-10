package com.zeitheron.mccue.client.gui.base;

import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.utils.color.ColorHelper;
import com.zeitheron.mccue.api.base.RgbAnimatorEntry;
import com.zeitheron.mccue.api.base.RgbShaderEntry;
import com.zeitheron.mccue.api.base.RgbTaskEntry;
import com.zeitheron.mccue.api.base.RgbTrigger;
import com.zeitheron.mccue.client.gui.GuiActionSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class GuiRgbTaskList
		extends GuiScrollingListBoundaried
{
	public List<RgbTaskEntry> tasks;
	GuiActionSetup gui;

	public GuiRgbTaskList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight, GuiActionSetup gui, List<RgbTaskEntry> triggers)
	{
		super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
		this.gui = gui;
		this.tasks = triggers;
	}

	@Override
	protected int getSize()
	{
		return this.tasks.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick)
	{
		this.gui.selectedTask = this.tasks.get(index);
		this.gui.onEntrySelect(this.gui.selectedTask);
	}

	@Override
	protected boolean isSelected(int index)
	{
		return this.tasks.get(index) == this.gui.selectedTask;
	}

	@Override
	protected void drawBackground()
	{
	}

	@Override
	protected int getContentHeight()
	{
		return this.getSize() * 39 + 1;
	}

	@Override
	protected void drawSlot(int idx, int right, int top, int height, Tessellator tess)
	{
		GlStateManager.enableBlend();
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		RgbTaskEntry task = this.tasks.get(idx);
		NBTTagCompound data = task.getTaskData();
		String name = data != null && data.hasKey("DisplayName", 8) ? data.getString("DisplayName") : null;
		name = name != null ? name : I18n.format("mccue.ifthenwith", task.getTrigger().getTriggerNameIf(),
				task.getAnimator().getAnimatorNameThen(),
				task.getShader().getShaderNameWith(task));
		String trimmed = font.trimStringToWidth(name, this.listWidth - 12).trim();
		if(!trimmed.equals(name)) trimmed = trimmed + "...";
		font.drawString(trimmed, 4, top, -1);
		RgbTrigger trigger = task.getTrigger();
		RgbAnimatorEntry animator = task.getAnimator();
		RgbShaderEntry shader = task.getShader();
		float cy = top + font.FONT_HEIGHT + 1;
		trigger.bindTexture();
		RenderUtil.drawFullTexturedModalRect(4.0, cy, 24.0, 24.0);
		animator.bindTexture();
		RenderUtil.drawFullTexturedModalRect(46.0, cy, 24.0, 24.0);
		shader.bindTexture();
		RenderUtil.drawFullTexturedModalRect(88.0, cy, 24.0, 24.0);
		GlStateManager.pushMatrix();
		UtilsFX.bindTexture("hammercore", "textures/gui/def_widgets.png");
		GlStateManager.translate(24.0f, cy + 4.5f, 0.0f);
		ColorHelper.glColor1i(9145227);
		RenderUtil.drawTexturedModalRect(4.0, 0.0, 4.0, 14.0, 18.0, 16.0);
		RenderUtil.drawTexturedModalRect(46.0, 0.0, 4.0, 14.0, 18.0, 16.0);
		GlStateManager.popMatrix();
	}
}