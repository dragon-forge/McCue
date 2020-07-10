package com.zeitheron.mccue.api.ctx.cells;

import com.zeitheron.mccue.api.ctx.GuiCell;
import com.zeitheron.mccue.api.ctx.ObjectContextFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(value = Side.CLIENT)
public class GuiStringCell
		extends GuiCell
{
	NBTBase value;
	GuiTextField field;
	Boolean valid = null;

	public GuiStringCell(ObjectContextFactory ctx, String nbtName)
	{
		super(ctx, nbtName);
	}

	@Override
	public void loadCell(NBTBase value)
	{
		this.value = value;
		this.valid = value != null;
	}

	@Override
	public void initCell(int screenWidth, int screenHeigth)
	{
		String txt;
		super.initCell(screenWidth, screenHeigth);
		this.ySize = 22;
		String string = txt = this.field != null ? this.field.getText() : null;
		if(this.field != null)
		{
			if(this.field.getText().isEmpty())
			{
				this.valid = null;
				txt = null;
			} else
			{
				this.valid = this.parse(this.field.getText());
				if(this.valid.booleanValue())
				{
					this.callSave();
				}
			}
		}
		if(txt == null && this.value != null)
		{
			txt = this.value.toString();
			if(this.value instanceof NBTPrimitive)
			{
				if(this.value instanceof NBTTagByte)
				{
					txt = Byte.toString(((NBTPrimitive) this.value).getByte());
				}
				if(this.value instanceof NBTTagDouble)
				{
					txt = Double.toString(((NBTPrimitive) this.value).getDouble());
				}
				if(this.value instanceof NBTTagFloat)
				{
					txt = Float.toString(((NBTPrimitive) this.value).getFloat());
				}
				if(this.value instanceof NBTTagInt)
				{
					txt = Integer.toString(((NBTPrimitive) this.value).getInt());
				}
				if(this.value instanceof NBTTagLong)
				{
					txt = Long.toString(((NBTPrimitive) this.value).getLong());
				}
			} else if(this.value instanceof NBTTagString)
			{
				txt = ((NBTTagString) this.value).getString();
			}
		}
		this.field = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, screenWidth / 2 + 4, 2, screenWidth / 2 - 8, 18);
		if(txt != null)
		{
			this.field.setText(txt);
		}
	}

	@Override
	public void render(int mouseX, int mouseY)
	{
		super.render(mouseX, mouseY);
		Minecraft mc = Minecraft.getMinecraft();
		this.field.drawTextBox();
		String txt = this.getLabel();
		mc.fontRenderer.drawString(txt, (this.screenWidth / 2 - mc.fontRenderer.getStringWidth(txt)) / 2, (this.ySize - mc.fontRenderer.FONT_HEIGHT) / 2, this.valid == null && !this.required ? -1 : (this.valid != null && this.valid != false ? -14483678 : -56798));
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) throws IOException
	{
		if(this.field.mouseClicked(mouseX, mouseY, button))
		{
			return;
		}
		super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void handleKeyboardInput() throws IOException
	{
		char c = Keyboard.getEventCharacter();
		int k = Keyboard.getEventKey();
		if((k == 0 && c >= ' ' || Keyboard.getEventKeyState()) && this.field.textboxKeyTyped(c, k))
		{
			if(this.field.getText().isEmpty())
			{
				this.valid = null;
			} else
			{
				this.valid = this.parse(this.field.getText());
				if(this.valid.booleanValue())
				{
					this.callSave();
				}
			}
		}
	}
}