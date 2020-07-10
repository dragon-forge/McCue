package com.zeitheron.mccue.client.gui;

import com.zeitheron.hammercore.client.gui.GuiWTFMojang;
import com.zeitheron.hammercore.client.gui.impl.container.ContainerEmpty;
import com.zeitheron.hammercore.client.utils.texture.gui.DynGuiTex;
import com.zeitheron.hammercore.client.utils.texture.gui.GuiTexBakery;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.mccue.block.TileRedstoneTrigger;
import com.zeitheron.mccue.net.PacketSyncLKey;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(value = Side.CLIENT)
public class GuiRedstoneTrigger
		extends GuiWTFMojang<ContainerEmpty>
{
	public final TileRedstoneTrigger tile;
	public static String acceptKey;
	DynGuiTex tex;
	GuiTextField key;

	public GuiRedstoneTrigger(ContainerEmpty inventorySlotsIn, TileRedstoneTrigger tile)
	{
		super(inventorySlotsIn);
		this.tile = tile;
	}

	@Override
	public void initGui()
	{
		this.xSize = 116;
		this.ySize = this.fontRenderer.FONT_HEIGHT + 42;
		super.initGui();
		GuiTexBakery b = GuiTexBakery.start();
		b.body(0, 0, this.xSize, this.ySize);
		this.tex = b.bake();
		String txt = this.key != null ? this.key.getText() : acceptKey;
		acceptKey = null;
		this.key = new GuiTextField(0, this.fontRenderer, this.guiLeft + 8, this.guiTop + this.fontRenderer.FONT_HEIGHT + 12, 100, 20);
		if(txt != null)
		{
			this.key.setText(txt);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		if(acceptKey != null)
		{
			this.key.setText(acceptKey);
			acceptKey = null;
		}
		this.tex.render(this.guiLeft, this.guiTop);
		String txt = I18n.format("gui.mccue:trikey");
		this.fontRenderer.drawString(txt, this.guiLeft + (this.xSize - this.fontRenderer.getStringWidth(txt)) / 2, this.guiTop + 6, 4210752);
		this.key.drawTextBox();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(!this.key.mouseClicked(mouseX, mouseY, mouseButton))
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(!this.key.textboxKeyTyped(typedChar, keyCode))
		{
			super.keyTyped(typedChar, keyCode);
		} else
		{
			this.tile.key = this.key.getText();
			HCNet.INSTANCE.sendToServer(new PacketSyncLKey().fill(this.tile.getPos(), this.key.getText()));
		}
	}
}

