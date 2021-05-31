package org.zeith.mccue.api.ctx.cells;

import org.zeith.mccue.api.ctx.GuiCell;
import org.zeith.mccue.api.ctx.ObjectContextFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(value = Side.CLIENT)
public class GuiLogicalCell
		extends GuiCell
{
	boolean active;
	GuiButton toggle;

	public GuiLogicalCell(ObjectContextFactory ctx, String nbtName)
	{
		super(ctx, nbtName);
	}

	@Override
	public void loadCell(NBTBase value)
	{
		this.active = false;
		if(value instanceof NBTTagByte)
		{
			this.active = ((NBTTagByte) value).getByte() == 1;
		}
	}

	@Override
	public void initCell(int screenWidth, int screenHeigth)
	{
		super.initCell(screenWidth, screenHeigth);
		this.ySize = 22;
		this.toggle = new GuiButton(791950655, screenWidth / 2 + 4, 1, screenWidth / 2 - 8, 20, I18n.format("logic.mccue:" + this.active));
	}

	@Override
	public void render(int mouseX, int mouseY)
	{
		super.render(mouseX, mouseY);
		Minecraft mc = Minecraft.getMinecraft();
		this.toggle.drawButton(mc, mouseX, mouseY, mc.getRenderPartialTicks());
		String txt = this.getLabel();
		mc.fontRenderer.drawString(txt, (this.screenWidth / 2 - mc.fontRenderer.getStringWidth(txt)) / 2, (this.ySize - mc.fontRenderer.FONT_HEIGHT) / 2, -1);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) throws IOException
	{
		if(button == 0 && this.toggle.isMouseOver())
		{
			Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
			this.active = !this.active;
			this.toggle.displayString = I18n.format("logic.mccue:" + this.active);
			if(this.parse(Boolean.toString(this.active)))
			{
				this.callSave();
			} else
			{
				System.out.println("WTF");
			}
		}
	}
}

