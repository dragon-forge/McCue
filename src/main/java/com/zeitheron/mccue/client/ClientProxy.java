package com.zeitheron.mccue.client;

import com.zeitheron.hammercore.client.utils.UV;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.utils.color.ColorHelper;
import com.zeitheron.mccue.BaseProxy;
import com.zeitheron.mccue.McCue;
import com.zeitheron.mccue.api.base.RgbRegistry;
import com.zeitheron.mccue.api.base.RgbTaskEntry;
import com.zeitheron.mccue.api.base.RgbTrigger;
import com.zeitheron.mccue.api.ctx.GuiCell;
import com.zeitheron.mccue.api.ctx.cells.GuiLogicalCell;
import com.zeitheron.mccue.api.ctx.cells.GuiSelColCell;
import com.zeitheron.mccue.api.event.RegisterRgbTasksEvent;
import com.zeitheron.mccue.api.event.UnregisterRgbTasksEvent;
import com.zeitheron.mccue.api.sdk.IBaseSDK;
import com.zeitheron.mccue.api.sdk.ICalibrations;
import com.zeitheron.mccue.api.sdk.RgbSdkRegistry;
import com.zeitheron.mccue.client.gui.GuiActionSetup;
import com.zeitheron.mccue.client.gui.GuiCallibration;
import com.zeitheron.mccue.client.sdk.corsair.icue.CueSDK;
import com.zeitheron.mccue.client.sdk.corsair.icue.DeviceInfo;
import com.zeitheron.mccue.init.TriggersMC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

@SideOnly(value = Side.CLIENT)
public class ClientProxy
		extends BaseProxy
{
	public static List<DeviceInfo> currentDevices;
	public static final CommandTreeBase modCommand;
	UV logo1x1 = new UV(new ResourceLocation("mccue", "textures/logo1x1.png"), 0.0, 0.0, 256.0, 256.0);
	GuiButton lbtn;

	@Override
	public void a()
	{
		RgbSdkRegistry.registerSDK(() ->
		{
			CueSDK sdk = new CueSDK(true);
			return sdk.isActive() ? sdk : null;
		});

		modCommand.addSubcommand(ISimpleCommand.create(0, "reload", (server, sender, args) ->
		{
			TextComponentString comp = new TextComponentString("Reloading RGB..");
			comp.getStyle().setColor(TextFormatting.GREEN);
			sender.sendMessage(comp);
			RgbRegistry.masterReload();
			comp = new TextComponentString("RGB Reload Complete!");
			comp.getStyle().setColor(TextFormatting.GREEN);
			sender.sendMessage(comp);
		}));
	}

	@Override
	public void b()
	{
		RgbRegistry.masterReload();

		Thread thread = new Thread(() ->
		{
			do
			{
				try
				{
					for(IBaseSDK sdk : RgbSdkRegistry.SDKS)
					{
						sdk.getDispatcher().updateRgb();
					}
				} catch(Throwable err)
				{
					if(err instanceof ConcurrentModificationException) continue;
					err.printStackTrace();
				}
				ClientProxy.$(15L);
			} while(true);
		}, "McCueRGBSync");

		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setDaemon(true);
		thread.start();

		ClientCommandHandler.instance.registerCommand(modCommand);

		GuiCell.register(Boolean.TYPE, GuiLogicalCell::new);
		GuiCell.register(Boolean.class, GuiLogicalCell::new);
		GuiCell.register(Color.class, GuiSelColCell::new);
	}

	@SubscribeEvent
	public void initGui(GuiScreenEvent.InitGuiEvent.Post e)
	{
		final GuiScreen g = e.getGui();
		if(g instanceof GuiOptions)
		{
			GuiButton mccue;
			GuiButton controls = e.getButtonList().stream().filter(btn -> btn.id == 100).findAny().orElse(null);
			if(controls == null)
			{
				return;
			}
			this.lbtn = mccue = new GuiButton(438963, controls.x + controls.width + 4, controls.y, 20, 20, "McCue")
			{
				@Override
				public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
				{
					if(this.visible)
					{
						FontRenderer fontrenderer = mc.fontRenderer;
						mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
						GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
						this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
						int i = this.getHoverState(this.hovered);
						GlStateManager.enableBlend();
						GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
						GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
						this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
						this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
						this.mouseDragged(mc, mouseX, mouseY);
						int j = 14737632;
						if(this.packedFGColour != 0)
						{
							j = this.packedFGColour;
						} else if(!this.enabled)
						{
							j = 10526880;
						} else if(this.hovered)
						{
							j = 16777120;
						}
						if(RgbSdkRegistry.SDKS.stream().map(IBaseSDK::calibrations).anyMatch(ICalibrations::hasMoreCalibrations))
						{
							GlStateManager.pushMatrix();
							GlStateManager.translate(0.0f, (float) Math.sin(Math.toRadians((float) (System.currentTimeMillis() % 1800L) / 10.0f)) * 4.0f, 0.0f);
							fontrenderer.drawString("!!!", this.x + this.width + 1, this.y + (this.height - fontrenderer.FONT_HEIGHT) / 2 - 1, ColorHelper.interpolate(j, 16711680, 0.5f));
							GlStateManager.popMatrix();
						}
						ColorHelper.glColor1i(j);
						ClientProxy.this.logo1x1.render(this.x + 2, this.y + 2, this.width - 4, this.height - 4);
						GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
						if(this.hovered && !this.enabled)
						{
							UtilsFX.drawCustomTooltip(g, mc.getRenderItem(), fontrenderer, Arrays.asList("The mod has failed.\nEnsure that you have iCue software installed,\nand try restarting Minecraft.".split("\n")), mouseX, mouseY, -13108);
						}
					}
				}
			};

			e.getButtonList().add(mccue);
			mccue.enabled = !RgbSdkRegistry.SDKS.isEmpty();
		}
	}

	@SubscribeEvent
	public void actionPerformed(GuiScreenEvent.ActionPerformedEvent e)
	{
		TriggersMC.ACTION_PERFORMED.trigger(null);
		if(this.lbtn == e.getButton())
		{
			ICalibrations uncalibrated = RgbSdkRegistry.SDKS.stream().map(sdk -> sdk.calibrations()).filter(ICalibrations::hasMoreCalibrations).findFirst().orElse(null);
			if(uncalibrated != null)
			{
				Minecraft.getMinecraft().displayGuiScreen(new GuiCallibration(e.getGui(), uncalibrated));
			} else
			{
				Minecraft.getMinecraft().displayGuiScreen(new GuiActionSetup(e.getGui()));
			}
		}
	}

	public static void saveTasks()
	{
		File taskFile = new File(McCue.modCfgDir, "tasks.dat");
		NBTTagCompound comp = new NBTTagCompound();
		NBTTagList tasks = new NBTTagList();
		RgbRegistry.RGB_TASKS.forEach(entry -> tasks.appendTag(entry.serializeNBT()));
		comp.setTag("TaskEntries", tasks);
		try
		{
			CompressedStreamTools.writeCompressed(comp, new FileOutputStream(taskFile));
		} catch(IOException iOException)
		{
			// empty catch block
		}
	}

	@SubscribeEvent
	public void registerRgb(RegisterRgbTasksEvent e)
	{
		File taskFile = new File(McCue.modCfgDir, "tasks.dat");
		try
		{
			NBTTagCompound comp = CompressedStreamTools.readCompressed(new FileInputStream(taskFile));
			NBTTagList tasks = comp.getTagList("TaskEntries", 10);
			for(int i = 0; i < tasks.tagCount(); ++i)
			{
				NBTTagCompound nbt = tasks.getCompoundTagAt(i);
				RgbTaskEntry entry = RgbTaskEntry.load(nbt);
				if(entry == null || !entry.isValid()) continue;
				e.registerEntry(entry);
			}
		} catch(IOException comp)
		{
			// empty catch block
		}
	}

	@SubscribeEvent
	public void saveRgb(UnregisterRgbTasksEvent e)
	{
		ClientProxy.saveTasks();
	}

	@Override
	public void a(RgbTrigger a, @Nullable NBTTagCompound b)
	{
		for(IBaseSDK sdk : RgbSdkRegistry.SDKS)
		{
			sdk.getDispatcher().onTrigger(a, b);
		}
	}

	public static void $(long ms)
	{
		try
		{
			Thread.sleep(ms);
		} catch(Throwable throwable)
		{
			// empty catch block
		}
	}

	@Override
	public void d()
	{
		for(IBaseSDK sdk : RgbSdkRegistry.SDKS)
		{
			sdk.getDispatcher().reload();
		}
	}

	static
	{
		modCommand = new CommandTreeBase()
		{
			@Override
			public String getUsage(ICommandSender sender)
			{
				return "mccue";
			}

			@Override
			public String getName()
			{
				return "mccue";
			}

			@Override
			public int getRequiredPermissionLevel()
			{
				return 0;
			}
		};
	}
}