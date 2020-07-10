package com.zeitheron.mccue.api.ctx.cells;

import com.zeitheron.mccue.api.ctx.GuiCell;
import com.zeitheron.mccue.api.ctx.ObjectContextFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

@SideOnly(value = Side.CLIENT)
public class GuiSelColCell
		extends GuiCell
{
	int color;
	Rectangle select = new Rectangle();
	Thread cthread;

	public GuiSelColCell(ObjectContextFactory ctx, String nbtName)
	{
		super(ctx, nbtName);
	}

	@Override
	public void loadCell(NBTBase value)
	{
		this.color = 0xFFFFFF;
		if(value instanceof NBTTagInt) this.color = ((NBTTagInt) value).getInt();
	}

	@Override
	public void initCell(int screenWidth, int screenHeigth)
	{
		super.initCell(screenWidth, screenHeigth);
		this.ySize = 22;
		this.select.setBounds(screenWidth / 2 + 4, 1, screenWidth / 2 - 8, 20);
	}

	@Override
	public void render(int mouseX, int mouseY)
	{
		super.render(mouseX, mouseY);
		Minecraft mc = Minecraft.getMinecraft();
		GuiUtils.drawGradientRect(0, this.select.x, this.select.y, this.select.x + this.select.width, this.select.y + this.select.height, 0xFF000000 | this.color, 0xFF000000 | this.color);
		String txt = this.getLabel();
		mc.fontRenderer.drawString(txt, (this.screenWidth / 2 - mc.fontRenderer.getStringWidth(txt)) / 2, (this.ySize - mc.fontRenderer.FONT_HEIGHT) / 2, -1);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) throws IOException
	{
		if(button == 0 && this.select.contains(mouseX, mouseY) && (this.cthread == null || !this.cthread.isAlive()))
		{
			Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
			this.cthread = new Thread(() ->
			{
				Color chosen = JColorChooser.showDialog(null, I18n.format("gui.select"), new Color(this.color));
				if(chosen != null)
				{
					Minecraft.getMinecraft().addScheduledTask(() ->
					{
						this.color = chosen.getRGB();
						this.parsed.put(this.field, new NBTTagInt(chosen.getRGB()));
						this.callSave();
					});
				}
				this.cthread = null;
			}, "iCueColorPicker");
			this.cthread.start();
		}
	}
}