package com.zeitheron.mccue.client.gui;

import com.google.common.base.Predicates;
import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.mccue.api.RgbTask;
import com.zeitheron.mccue.api.base.*;
import com.zeitheron.mccue.api.ctx.NbtBasedContext;
import com.zeitheron.mccue.api.ctx.ObjectContextFactory;
import com.zeitheron.mccue.api.sdk.IBaseSDK;
import com.zeitheron.mccue.api.sdk.RgbSdkRegistry;
import com.zeitheron.mccue.client.ClientProxy;
import com.zeitheron.mccue.client.gui.base.*;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class GuiCreateTask
		extends GuiScreen
{
	GuiScreen parent;
	GuiButton done;
	GuiButton back;
	public GuiScrollingListBoundaried overlay;
	public GuiLedList ledList;
	public RgbTrigger trigger;
	public RgbAnimatorEntry animator;
	public RgbShaderEntry shader;
	public NBTTagCompound properties = new NBTTagCompound();
	Integer setIndex;
	public String sdk;
	public String led;
	GuiScrollingTextList description;
	final List<String> desc = new ArrayList<String>();
	public final BooleanSupplier contextFilled = () ->
	{
		Stream.Builder<ObjectContextFactory> factories = Stream.builder();
		if(this.trigger != null)
		{
			factories.add(this.trigger.getContext());
		}
		if(this.animator != null)
		{
			factories.add(this.animator.getContext());
		}
		if(this.shader != null)
		{
			factories.add(this.shader.getContext());
		}
		return NbtBasedContext.isContextFullfilled(this.properties, factories.build().filter(Predicates.notNull()));
	};
	final Rectangle rect = new Rectangle();
	int hmode;
	Runnable applyAnimation;

	public GuiCreateTask(GuiScreen parent)
	{
		this.parent = parent;
	}

	public GuiCreateTask(GuiScreen parent, int index)
	{
		this(parent);
		this.setIndex = index;
		RgbTaskEntry entry = RgbRegistry.RGB_TASKS.get(index);
		this.trigger = entry.getTrigger();
		this.animator = entry.getAnimator();
		this.shader = entry.getShader();
		this.properties = entry.getTaskData().copy();
		this.sdk = entry.getSdk().getSdkId().toString();
		this.led = entry.getSdkLed();
	}

	private void setNBT(NBTTagCompound props)
	{
		if(props == null)
		{
			throw new NullPointerException();
		}
		this.properties = props;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int twidth = this.fontRenderer.FONT_HEIGHT * 3;
		twidth += this.fontRenderer.getStringWidth(I18n.format("gui.mccue:ctr.if") + " ");
		twidth += this.fontRenderer.getStringWidth(" " + I18n.format("gui.mccue:ctr.then") + " ");
		float scale = (float) this.width / ((float) (twidth += this.fontRenderer.getStringWidth(" " + I18n.format("gui.mccue:ctr.with") + " ")) + 32.0f);
		int start = (int) ((float) (this.fontRenderer.FONT_HEIGHT + 3) * scale);
		this.ledList = new GuiLedList(this.mc, 120, this.height - start, start, this.height - 46, 2, 21, this.width, this.height, this);
		this.description = new GuiScrollingTextList(this.mc, this.width - 130, this.height - 48, start, this.height - 46, 128, this.fontRenderer, this.width, this.height, this.desc);
		this.back = new GuiButton(0, 128 + (this.width - 136) / 2 - 100, this.height - 44, I18n.format("gui.back"));
		this.addButton(this.back);
		this.done = new GuiButton(1, 128 + (this.width - 136) / 2 - 100, this.height - 22, I18n.format("gui.done"));
		this.addButton(this.done);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if(this.applyAnimation != null)
		{
			this.applyAnimation.run();
		}
		this.drawDefaultBackground();
		this.ledList.drawScreen(mouseX, mouseY, partialTicks);
		this.description.drawScreen(mouseX, mouseY, partialTicks);
		this.hmode = -1;
		int twidth = this.fontRenderer.FONT_HEIGHT * 3;
		twidth += this.fontRenderer.getStringWidth(I18n.format("gui.mccue:ctr.if") + " ");
		twidth += this.fontRenderer.getStringWidth(" " + I18n.format("gui.mccue:ctr.then") + " ");
		float scale = (float) this.width / ((float) (twidth += this.fontRenderer.getStringWidth(" " + I18n.format("gui.mccue:ctr.with") + " ")) + 32.0f);
		float x = ((float) this.width - (float) twidth * scale) / 2.0f;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, 8.0f, 0.0f);
		GlStateManager.scale(scale, scale, scale);
		this.fontRenderer.drawString(I18n.format("gui.mccue:ctr.if") + " ", 0, 0, 16777215);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x += (float) this.fontRenderer.getStringWidth(I18n.format("gui.mccue:ctr.if") + " ") * scale, 8.0f, 0.0f);
		this.rect.setBounds((int) x, 8, (int) ((float) this.fontRenderer.FONT_HEIGHT * scale), (int) ((float) this.fontRenderer.FONT_HEIGHT * scale));
		GlStateManager.scale(scale, scale, scale);
		if(this.trigger != null)
		{
			this.trigger.bindTexture();
		} else
		{
			UtilsFX.bindTexture("mccue", "textures/rgb/null.png");
		}
		RenderUtil.drawFullTexturedModalRect(0.0, -1.0, this.fontRenderer.FONT_HEIGHT, this.fontRenderer.FONT_HEIGHT);
		GlStateManager.popMatrix();
		if(this.rect.contains(mouseX, mouseY))
		{
			this.hmode = 0;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(x += (float) this.fontRenderer.FONT_HEIGHT * scale, 8.0f, 0.0f);
		GlStateManager.scale(scale, scale, scale);
		this.fontRenderer.drawString(" " + I18n.format("gui.mccue:ctr.then") + " ", 0, 0, 16777215);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x += (float) this.fontRenderer.getStringWidth(" " + I18n.format("gui.mccue:ctr.then") + " ") * scale, 8.0f, 0.0f);
		this.rect.setBounds((int) x, 8, (int) ((float) this.fontRenderer.FONT_HEIGHT * scale), (int) ((float) this.fontRenderer.FONT_HEIGHT * scale));
		GlStateManager.scale(scale, scale, scale);
		if(this.animator != null)
		{
			this.animator.bindTexture();
		} else
		{
			UtilsFX.bindTexture("mccue", "textures/rgb/null.png");
		}
		RenderUtil.drawFullTexturedModalRect(0.0, -1.0, this.fontRenderer.FONT_HEIGHT, this.fontRenderer.FONT_HEIGHT);
		GlStateManager.popMatrix();
		if(this.rect.contains(mouseX, mouseY))
		{
			this.hmode = 1;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(x += (float) this.fontRenderer.FONT_HEIGHT * scale, 8.0f, 0.0f);
		GlStateManager.scale(scale, scale, scale);
		this.fontRenderer.drawString(" " + I18n.format("gui.mccue:ctr.with") + " ", 0, 0, 16777215);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x += (float) this.fontRenderer.getStringWidth(" " + I18n.format("gui.mccue:ctr.with") + " ") * scale, 8.0f, 0.0f);
		this.rect.setBounds((int) x, 8, (int) ((float) this.fontRenderer.FONT_HEIGHT * scale), (int) ((float) this.fontRenderer.FONT_HEIGHT * scale));
		GlStateManager.scale(scale, scale, scale);
		if(this.shader != null)
		{
			this.shader.bindTexture();
		} else
		{
			UtilsFX.bindTexture("mccue", "textures/rgb/null.png");
		}
		RenderUtil.drawFullTexturedModalRect(0.0, -1.0, this.fontRenderer.FONT_HEIGHT, this.fontRenderer.FONT_HEIGHT);
		GlStateManager.popMatrix();
		if(this.rect.contains(mouseX, mouseY))
		{
			this.hmode = 2;
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		if(this.overlay != null)
		{
			this.overlay.drawScreen(mouseX, mouseY, partialTicks);
			if(this.overlay != null)
			{
				int selected = this.overlay.func_27256_c(mouseX, mouseY);
				if(selected != -1 && this.overlay.isHovering(mouseX, mouseY))
				{
					if(this.overlay instanceof GuiCreateTaskSelectTrigger)
					{
						this.drawHoveringText(RgbRegistry.ALL_TRIGGERS.get(selected).getTriggerNameIf(), mouseX, mouseY);
					}
					if(this.overlay instanceof GuiCreateTaskSelectAnimator)
					{
						this.drawHoveringText(RgbRegistry.ALL_ANIMATORS.get(selected).getAnimatorNameThen(), mouseX, mouseY);
					}
					if(this.overlay instanceof GuiCreateTaskSelectShader)
					{
						this.drawHoveringText(RgbRegistry.ALL_SHADERS.get(selected).getShaderNameWith(null), mouseX, mouseY);
					}
				}
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				RenderHelper.disableStandardItemLighting();
			}
		}
		if(this.hmode != -1)
		{
			ArrayList<String> tooltip = new ArrayList<String>();
			if(this.hmode == 0)
			{
				if(this.trigger != null)
				{
					tooltip.add(this.trigger.getTriggerNameIf());
					if(!this.trigger.getContext().isEmpty())
					{
						tooltip.add("");
						tooltip.add(I18n.format("gui.mccue:rclick"));
					}
				} else
				{
					tooltip.add(I18n.format("gui.mccue:trigger.name"));
					tooltip.add("");
					tooltip.add(I18n.format("gui.mccue:lclick"));
				}
			}
			if(this.hmode == 1)
			{
				if(this.animator != null)
				{
					tooltip.add(this.animator.getAnimatorNameThen());
					if(!this.animator.getContext().isEmpty())
					{
						tooltip.add("");
						tooltip.add(I18n.format("gui.mccue:rclick"));
					}
				} else
				{
					tooltip.add(I18n.format("gui.mccue:animator.name"));
					tooltip.add("");
					tooltip.add(I18n.format("gui.mccue:lclick"));
				}
			}
			if(this.hmode == 2)
			{
				if(this.shader != null)
				{
					tooltip.add(this.shader.getShaderNameWith(null));
					if(!this.shader.getContext().isEmpty())
					{
						tooltip.add("");
						tooltip.add(I18n.format("gui.mccue:rclick"));
					}
				} else
				{
					tooltip.add(I18n.format("gui.mccue:shader.name"));
					tooltip.add("");
					tooltip.add(I18n.format("gui.mccue:lclick"));
				}
			}
			if(tooltip != null)
			{
				this.drawHoveringText(tooltip, mouseX, mouseY);
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(this.hmode != -1 && this.overlay == null && mouseButton == 0)
		{
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
			int h = this.height - mouseY - 24;
			if(this.hmode == 0)
			{
				this.overlay = new GuiCreateTaskSelectTrigger(this.mc, 60, h, mouseY - 8, mouseY + h, Math.min(mouseX + 8, this.width - 62), 39, this.width, this.height, this);
			}
			if(this.hmode == 1)
			{
				this.overlay = new GuiCreateTaskSelectAnimator(this.mc, 60, h, mouseY - 8, mouseY + h, Math.min(mouseX + 8, this.width - 62), 39, this.width, this.height, this);
			}
			if(this.hmode == 2)
			{
				this.overlay = new GuiCreateTaskSelectShader(this.mc, 60, h, mouseY - 8, mouseY + h, Math.min(mouseX + 8, this.width - 62), 39, this.width, this.height, this);
			}
			return;
		}
		if(this.hmode != -1 && mouseButton == 1)
		{
			if(this.hmode == 0 && this.trigger != null && !this.trigger.getContext().isEmpty())
			{
				this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.125f));
				this.mc.displayGuiScreen(new GuiConfigureNbtContext(this.trigger.getContext(), this.properties, this::setNBT, this, "logic." + this.trigger.getId().getNamespace() + ":trigger." + this.trigger.getId().getPath()));
			}
			if(this.hmode == 1 && this.animator != null && !this.animator.getContext().isEmpty())
			{
				this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.125f));
				this.mc.displayGuiScreen(new GuiConfigureNbtContext(this.animator.getContext(), this.properties, this::setNBT, this, "logic." + this.animator.getId().getNamespace() + ":animator." + this.animator.getId().getPath()));
			}
			if(this.hmode == 2 && this.shader != null && !this.shader.getContext().isEmpty())
			{
				this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.125f));
				this.mc.displayGuiScreen(new GuiConfigureNbtContext(this.shader.getContext(), this.properties, this::setNBT, this, "logic." + this.shader.getId().getNamespace() + ":shader." + this.shader.getId().getPath()));
			}
		}
		if(this.overlay != null && !this.overlay.isHovering(mouseX, mouseY))
		{
			this.overlay = null;
		} else
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		this.ledList.handleMouseInput(mouseX, mouseY);
		if(this.overlay != null)
		{
			this.overlay.handleMouseInput(mouseX, mouseY);
		}
		this.description.handleMouseInput(mouseX, mouseY);
		super.handleMouseInput();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button == this.back)
		{
			this.mc.displayGuiScreen(this.parent);
			if(this.mc.currentScreen == null)
			{
				this.mc.setIngameFocus();
			}
		}
		if(button == this.done)
		{
			RgbTaskEntry entry = new RgbTaskEntry(this.trigger, this.animator, this.shader, this.properties, RgbSdkRegistry.getSdk(new ResourceLocation(this.sdk)), this.led);
			if(this.setIndex != null)
			{
				RgbRegistry.RGB_TASKS.set(this.setIndex, entry);
			} else
			{
				RgbRegistry.RGB_TASKS.add(entry);
			}
			ClientProxy.saveTasks();
			this.mc.displayGuiScreen(this.parent);
			if(this.mc.currentScreen == null)
			{
				this.mc.setIngameFocus();
			}
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

	@Override
	public void updateScreen()
	{
		IBaseSDK sdk;
		ObjectContextFactory ctx;
		this.done.enabled = this.trigger != null && this.animator != null && this.shader != null && this.contextFilled.getAsBoolean() && this.sdk != null && this.led != null;
		this.desc.clear();
		IBaseSDK iBaseSDK = sdk = this.sdk != null ? RgbSdkRegistry.getSdk(new ResourceLocation(this.sdk)) : null;
		if(this.trigger != null && this.animator != null && this.shader != null && sdk != null && this.led != null)
		{
			RgbTaskEntry task = new RgbTaskEntry(this.trigger, this.animator, this.shader, this.properties, sdk, this.led);
			this.desc.add(I18n.format("mccue.ifthenwith", this.trigger.getTriggerNameIf(),
					this.animator.getAnimatorNameThen(),
					this.shader.getShaderNameWith(task)));
		}
		if(sdk != null && this.led != null)
		{
			this.desc.add("");
			this.desc.add(sdk.sdkName() + ": " + sdk.getLedName(this.led));
		}
		if(this.trigger != null)
		{
			this.desc.add("");
			this.desc.add(I18n.format("gui.mccue:trigger", this.trigger.getTriggerDisplayName()));
			ctx = this.trigger.getContext();
			ObjectContextFactory finalCtx = ctx;
			ctx.keyStream().map(key ->
			{
				boolean req = finalCtx.getRequiredFields().contains(key);
				ResourceLocation id = this.trigger.getId();
				String name = I18n.format("logic." + id.getNamespace() + ":trigger." + id.getPath() + "." + key.toLowerCase()) + (req ? "*" : "");
				return " - " + name + " = " + finalCtx.toString(key, this.properties.getTag(key));
			}).sorted().forEach(this.desc::add);
		}
		if(this.animator != null)
		{
			this.desc.add("");
			this.desc.add(I18n.format("gui.mccue:animator", this.animator.getAnimatorDisplayName()));
			ctx = this.animator.getContext();
			ObjectContextFactory finalCtx1 = ctx;
			ctx.keyStream().map(key ->
			{
				boolean req = finalCtx1.getRequiredFields().contains(key);
				ResourceLocation id = this.animator.getId();
				String name = I18n.format("logic." + id.getNamespace() + ":animator." + id.getPath() + "." + key.toLowerCase()) + (req ? "*" : "");
				return " - " + name + " = " + finalCtx1.toString(key, this.properties.getTag(key));
			}).sorted().forEach(this.desc::add);
		}
		if(this.shader != null)
		{
			this.desc.add("");
			this.desc.add(I18n.format("gui.mccue:shader", this.shader.getShaderDisplayName()));
			ctx = this.shader.getContext();
			ObjectContextFactory finalCtx2 = ctx;
			ctx.keyStream().map(key ->
			{
				boolean req = finalCtx2.getRequiredFields().contains(key);
				ResourceLocation id = this.shader.getId();
				String name = I18n.format("logic." + id.getNamespace() + ":shader." + id.getPath() + "." + key.toLowerCase()) + (req ? "*" : "");
				return " - " + name + " = " + finalCtx2.toString(key, this.properties.getTag(key));
			}).sorted().forEach(this.desc::add);
		}
		this.description.splitText();
	}

	public void setLed(String led)
	{
		this.led = led;
	}

	public void selectLed(GuiLedList.SdkLedInfo info)
	{
		if(this.sdk != null && this.led != null)
		{
			RgbSdkRegistry.getSdk(new ResourceLocation(this.sdk)).createRgbSink(this.led).accept(new int[0]);
		}
		this.sdk = info.sdk.getSdkId().toString();
		this.led = info.led;
		Consumer<int[]> setter = info.sdk.createRgbSink(info.led);
		int[] rgb = new int[3];
		this.applyAnimation = () ->
		{
			if(this.trigger != null && this.animator != null && this.shader != null && this.contextFilled.getAsBoolean())
			{
				info.sdk.getDispatcher().addTask(new RgbTask(this.trigger, this.animator, this.shader, info.sdk.createRgbSink(this.led), this.properties.copy()));
			} else
			{
				long speed = 5000L;
				int color = MathHelper.hsvToRGB((float) (System.currentTimeMillis() % speed) / (float) speed, 1.0f, 1.0f);
				rgb[0] = color >> 16 & 255;
				rgb[1] = color >> 8 & 255;
				rgb[2] = color >> 0 & 255;
				setter.accept(rgb);
			}
		};
		this.properties.setString("sdk", this.sdk);
		this.properties.setString(info.sdk.dataLedAddress(), this.led);
	}
}

