package com.zeitheron.mccue.client.gui;

import com.zeitheron.mccue.api.base.RgbRegistry;
import com.zeitheron.mccue.api.base.RgbTaskEntry;
import com.zeitheron.mccue.api.ctx.ObjectContextFactory;
import com.zeitheron.mccue.client.gui.base.GuiRgbTaskList;
import com.zeitheron.mccue.client.gui.base.GuiScrollingTextList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GuiActionSetup
		extends GuiScreen
{
	GuiScreen parent;
	public RgbTaskEntry selectedTask;
	public List<RgbTaskEntry> taskList;
	public GuiRgbTaskList taskSL;
	public GuiButton addTask;
	public GuiButton delTask;
	public GuiButton editTask;
	public GuiButton done;
	int triggerWidth;
	int actionWidth;
	GuiScrollingTextList description;
	final List<String> desc = new ArrayList<String>();

	public GuiActionSetup(GuiScreen parent)
	{
		this.parent = parent;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.taskList = new ArrayList<RgbTaskEntry>(RgbRegistry.RGB_TASKS);
		this.taskList.sort(Comparator.comparing(task -> task.getTrigger().getTriggerDisplayName()));
		this.triggerWidth = 120;
		this.taskSL = new GuiRgbTaskList(this.mc, this.triggerWidth, this.height - 46, 4, this.height - 46, 2, 39, this.width, this.height, this, this.taskList);
		this.description = new GuiScrollingTextList(this.mc, this.width - 136, this.height - 48, 4, this.height - 46, 128, this.fontRenderer, this.width, this.height, this.desc);
		this.addTask = new GuiButton(666, 2, this.height - 22, 120, 20, I18n.format("gui.mccue:add"));
		this.buttonList.add(this.addTask);
		this.delTask = new GuiButton(667, 2, this.height - 44, 120, 20, I18n.format("gui.mccue:delete"));
		this.delTask.enabled = false;
		this.buttonList.add(this.delTask);
		this.done = new GuiButton(668, 128 + (this.width - 136) / 2 - 100, this.height - 22, I18n.format("gui.done"));
		this.buttonList.add(this.done);
		this.editTask = new GuiButton(669, 128 + (this.width - 136) / 2 - 100, this.height - 44, I18n.format("gui.mccue:edit"));
		this.buttonList.add(this.editTask);
		this.editTask.enabled = false;
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		super.handleMouseInput();
		this.taskSL.handleMouseInput(mouseX, mouseY);
		this.description.handleMouseInput(mouseX, mouseY);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.delTask.enabled = this.selectedTask != null;
		this.editTask.enabled = this.delTask.enabled;
		this.drawDefaultBackground();
		this.taskSL.drawScreen(mouseX, mouseY, partialTicks);
		this.description.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
		int hov = this.taskSL.func_27256_c(mouseX, mouseY);
		if(hov > -1 && this.taskSL.isHovering(mouseX, mouseY))
		{
			RgbTaskEntry task = this.taskList.get(hov);
			this.drawHoveringText(Arrays.asList(I18n.format("mccue.ifthenwith", task.getTrigger().getTriggerNameIf(),
					task.getAnimator().getAnimatorNameThen(),
					task.getShader().getShaderNameWith(task)), "", I18n.format("gui.mccue:trigger", task.getTrigger().getTriggerDisplayName()), I18n.format("gui.mccue:animator", task.getAnimator().getAnimatorDisplayName()), I18n.format("gui.mccue:shader", task.getShader().getShaderDisplayName())), mouseX, mouseY);
		}
	}

	@Override
	public void updateScreen()
	{
		this.desc.clear();
		if(this.selectedTask != null)
		{
			RgbTaskEntry task = this.selectedTask;
			this.desc.add(I18n.format("mccue.ifthenwith", task.getTrigger().getTriggerNameIf(),
					task.getAnimator().getAnimatorNameThen(),
					task.getShader().getShaderNameWith(task)));
			this.desc.add("");
			this.desc.add(task.getSdk().sdkName() + ": " + task.getSdk().getLedName(task.getSdkLed()));
			this.desc.add("");
			this.desc.add(I18n.format("gui.mccue:trigger", task.getTrigger().getTriggerDisplayName()));
			final ObjectContextFactory ctx = task.getTrigger().getContext();
			ctx.keyStream().map(key ->
			{
				boolean req = ctx.getRequiredFields().contains(key);
				ResourceLocation id = task.getTrigger().getId();
				String name = I18n.format("logic." + id.getNamespace() + ":trigger." + id.getPath() + "." + key.toLowerCase()) + (req ? "*" : "");
				return " - " + name + " = " + ctx.toString(key, task.getTaskData().getTag(key));
			}).sorted().forEach(this.desc::add);
			this.desc.add("");
			this.desc.add(I18n.format("gui.mccue:animator", task.getAnimator().getAnimatorDisplayName()));
			final ObjectContextFactory ctx2 = task.getAnimator().getContext();
			ctx2.keyStream().map(key ->
			{
				boolean req = ctx2.getRequiredFields().contains(key);
				ResourceLocation id = task.getAnimator().getId();
				String name = I18n.format("logic." + id.getNamespace() + ":animator." + id.getPath() + "." + key.toLowerCase()) + (req ? "*" : "");
				return " - " + name + " = " + ctx2.toString(key, task.getTaskData().getTag(key));
			}).sorted().forEach(this.desc::add);
			this.desc.add("");
			this.desc.add(I18n.format("gui.mccue:shader", task.getShader().getShaderDisplayName()));
			final ObjectContextFactory ctx3 = task.getShader().getContext();
			ctx3.keyStream().map(key ->
			{
				boolean req = ctx3.getRequiredFields().contains(key);
				ResourceLocation id = task.getShader().getId();
				String name = I18n.format("logic." + id.getNamespace() + ":shader." + id.getPath() + "." + key.toLowerCase()) + (req ? "*" : "");
				return " - " + name + " = " + ctx3.toString(key, task.getTaskData().getTag(key));
			}).sorted().forEach(this.desc::add);
			this.description.splitText();
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(this.done == button)
		{
			this.mc.displayGuiScreen(this.parent);
			if(this.mc.currentScreen == null)
			{
				this.mc.setIngameFocus();
			}
			return;
		}
		if(this.addTask == button)
		{
			this.mc.displayGuiScreen(new GuiCreateTask(this));
		}
		int index = RgbRegistry.RGB_TASKS.indexOf(this.selectedTask);
		if(this.editTask == button && index >= 0)
		{
			this.mc.displayGuiScreen(new GuiCreateTask(this, index));
		}
		if(this.delTask == button && this.selectedTask != null)
		{
			RgbRegistry.removeRgbTask(this.selectedTask);
			this.selectedTask = null;
			this.taskList.clear();
			this.taskList.addAll(RgbRegistry.RGB_TASKS);
			this.taskList.sort(Comparator.comparing(task -> task.getTrigger().getTriggerDisplayName()));
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == 1)
		{
			this.mc.displayGuiScreen(this.parent);
			if(this.mc.currentScreen == null)
			{
				this.mc.setIngameFocus();
			}
		}
	}

	public void onEntrySelect(RgbTaskEntry entry)
	{
	}
}

