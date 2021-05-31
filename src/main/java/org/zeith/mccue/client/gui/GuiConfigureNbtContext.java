package org.zeith.mccue.client.gui;

import org.zeith.mccue.api.ctx.GuiCell;
import org.zeith.mccue.api.ctx.ICellSafe;
import org.zeith.mccue.api.ctx.ObjectContextFactory;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuiConfigureNbtContext
		extends GuiScreen
		implements ICellSafe
{
	public final ObjectContextFactory factory;
	public final NBTTagCompound nbt;
	Consumer<NBTTagCompound> updator;
	GuiScreen parent;
	GuiButton done;
	public float scroll;
	final List<String> optionalKeys = new ArrayList<String>();
	final List<String> requiredKeys = new ArrayList<String>();
	final List<GuiCell> cells = new ArrayList<GuiCell>();
	final String i18n;

	public GuiConfigureNbtContext(ObjectContextFactory factory, NBTTagCompound nbt, Consumer<NBTTagCompound> updator, GuiScreen parent, String i18n)
	{
		this.factory = factory;
		this.nbt = nbt.copy();
		this.updator = updator;
		this.parent = parent;
		this.i18n = i18n;
	}

	public void refreshAllKeys()
	{
		this.cells.clear();
		this.optionalKeys.clear();
		this.requiredKeys.clear();
		this.requiredKeys.addAll(this.factory.getRequiredFields());
		this.optionalKeys.addAll(this.factory.getOptionalFields());
		this.factory.keyStream().map(field ->
		{
			GuiCell c = GuiCell.create(this.factory, field);
			c.setLabelI18n(this.i18n + "." + field, this.requiredKeys.contains(field));
			return c;
		}).forEach(this.cells::add);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.refreshAllKeys();
		this.cells.forEach(cell ->
		{
			NBTBase base = this.nbt.getTag(cell.getField());
			if(base == null)
			{
				base = this.factory.getDefault(cell.getField());
			}
			cell.setSafe(this);
			cell.loadCell(base);
			cell.initCell(this.width, this.height);
		});
		this.done = new GuiButton(0, this.width / 2 - 100, this.height - 22, 200, 20, I18n.format("gui.done"));
		this.addButton(this.done);
	}

	@Override
	public void save(GuiCell cell)
	{
		if(this.cells.contains(cell))
		{
			cell.apply(this.nbt);
			this.updator.accept(this.nbt);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		int y = -Math.round(this.scroll);
		for(int i = 0; i < this.cells.size(); ++i)
		{
			GuiCell cell = this.cells.get(i);
			int h = cell.getHeight();
			if(y + h <= 0)
			{
				y += h;
				continue;
			}
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0f, (float) y, 0.0f);
			cell.render(mouseX, mouseY - y);
			GlStateManager.popMatrix();
			y += h;
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
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

	@Override
	public void handleMouseInput() throws IOException
	{
		int dwheel = Mouse.getEventDWheel();
		if(dwheel != 0)
		{
			// empty if block
		}
		int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		int k = Mouse.getEventButton();
		if(Mouse.getEventButtonState())
		{
			int y = -Math.round(this.scroll);
			for(int o = 0; o < this.cells.size(); ++o)
			{
				GuiCell cell = this.cells.get(o);
				int h = cell.getHeight();
				if(y + h <= 0)
				{
					y += h;
					continue;
				}
				cell.mouseClicked(i, j - y, k);
				y += h;
			}
		}
		super.handleMouseInput();
	}

	@Override
	public void handleKeyboardInput() throws IOException
	{
		super.handleKeyboardInput();
		for(GuiCell c : this.cells)
		{
			c.handleKeyboardInput();
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
		for(GuiCell c : this.cells)
		{
			c.actionPerformed(button);
		}
	}
}