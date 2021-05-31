package org.zeith.mccue.client.gui;

import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.utils.color.ColorHelper;
import org.zeith.mccue.api.sdk.ICalibrations;
import org.zeith.mccue.client.sdk.corsair.icue.DeviceInfo;
import org.zeith.mccue.client.sdk.corsair.icue.jna.CorsairDeviceType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
import java.util.List;

public class GuiCallibration
		extends GuiScreen
{
	private static final ResourceLocation[][] TEXTURES = new ResourceLocation[][]{
			{
					new ResourceLocation("mccue", "textures/peripherals/unknown.png"),
					new ResourceLocation("mccue", "textures/peripherals/mouse.png"),
					new ResourceLocation("mccue", "textures/peripherals/keyboard.png"),
					new ResourceLocation("mccue", "textures/peripherals/headset.png"),
					new ResourceLocation("mccue", "textures/peripherals/mousepad.png"),
					new ResourceLocation("mccue", "textures/peripherals/headsetstand.png"),
					new ResourceLocation("mccue", "textures/peripherals/commanderpro.png"),
					new ResourceLocation("mccue", "textures/peripherals/lightingnodepro.png"),
					new ResourceLocation("mccue", "textures/peripherals/dram.png"),
					new ResourceLocation("mccue", "textures/peripherals/cooler.png"),
					new ResourceLocation("mccue", "textures/peripherals/motherboard.png"),
					new ResourceLocation("mccue", "textures/peripherals/gpu.png")
			},
			{
					new ResourceLocation("mccue", "textures/peripherals/unknown2.png"),
					new ResourceLocation("mccue", "textures/peripherals/mouse2.png"),
					new ResourceLocation("mccue", "textures/peripherals/keyboard2.png"),
					new ResourceLocation("mccue", "textures/peripherals/headset2.png"),
					new ResourceLocation("mccue", "textures/peripherals/mousepad2.png"),
					new ResourceLocation("mccue", "textures/peripherals/headsetstand2.png"),
					new ResourceLocation("mccue", "textures/peripherals/commanderpro2.png"),
					new ResourceLocation("mccue", "textures/peripherals/lightingnodepro2.png"),
					new ResourceLocation("mccue", "textures/peripherals/dram2.png"),
					new ResourceLocation("mccue", "textures/peripherals/cooler2.png"),
					new ResourceLocation("mccue", "textures/peripherals/motherboard2.png"),
					new ResourceLocation("mccue", "textures/peripherals/gpu2.png")
			}
	};
	public DeviceInfo prevDevice;
	public ICalibrations.ICalibrationInstance current;
	ICalibrations calib;
	GuiScreen parent;

	public static ResourceLocation tex(CorsairDeviceType type, int layer)
	{
		return TEXTURES[layer][type.ordinal()];
	}

	public GuiCallibration(GuiScreen parent, ICalibrations uncalibrated)
	{
		this.parent = parent;
		this.calib = uncalibrated;
	}

	public ICalibrations.ICalibrationInstance calib()
	{
		if(this.current == null || this.current.hasApplied()) this.current = this.calib.nextCalibration();
		return this.current;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int h = this.fontRenderer.FONT_HEIGHT;
		int totHeight = 128 + (h + 2) * 7 + 22 + h;
		int endY = (this.height + totHeight) / 2;
		this.addButton(new GuiButton(0, this.width / 2 + 2, endY - 20, 80, 20, "Yes"));
		this.addButton(new GuiButton(1, this.width / 2 - 82, endY - 20, 80, 20, "No"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		GlStateManager.enableBlend();
		ICalibrations.ICalibrationInstance i = this.calib();
		if(i == null)
		{
			this.mc.displayGuiScreen(this.parent);
			if(this.mc.currentScreen == null)
			{
				this.mc.setIngameFocus();
			}
		} else
		{
			int h = this.fontRenderer.FONT_HEIGHT;
			List<String> text = i.text();
			int totHeight = 128 + (h + 2) * (text.size() + 3) + 20 + h;
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0f, (float) ((this.height - totHeight) / 2), 0.0f);
			int color = MathHelper.hsvToRGB((float) (System.currentTimeMillis() % 30000L) / 30000.0f, 1.0f, 1.0f);
			float strength = 0.5F + (float) (1.0 + Math.sin(Math.toRadians((float) (System.currentTimeMillis() % 3600L) / 10.0f))) / 4.0f;
			int dcolor = ColorHelper.multiply(color, strength);
			i.setColor(dcolor);
			for(int j = 0; j < text.size(); ++j)
			{
				int yo = (h + 2) * j;
				if(j == text.size() - 1)
				{
					this.drawCenteredString(this.fontRenderer, text.get(j), this.width / 2, totHeight - 22 - h, 16777215);
					continue;
				}
				this.drawCenteredString(this.fontRenderer, text.get(j), this.width / 2, 129 + yo, 16777215);
			}
			RenderUtil.drawGradientRect(0.0, 132 + h * 3, this.width, h * 2 + 4, 0, -16777216 | color);
			RenderUtil.drawGradientRect(0.0, 132 + h * 5 + 4, this.width, h * 2 + 4, -16777216 | color, 0);
			GlStateManager.popMatrix();
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		ICalibrations.ICalibrationInstance i;
		if(button.id == 0)
		{
			ICalibrations.ICalibrationInstance i2 = this.calib();
			if(i2 != null)
			{
				i2.apply();
				i2.resetLed();
			}
		} else if(button.id == 1 && (i = this.calib()) != null)
		{
			i.deny();
			i.resetLed();
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == 1)
		{
			if(this.current != null)
			{
				this.current.resetLed();
			}
			this.mc.displayGuiScreen(this.parent);
			if(this.mc.currentScreen == null)
			{
				this.mc.setIngameFocus();
			}
		}
	}
}