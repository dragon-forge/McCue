package org.zeith.mccue.api.base;

import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.net.HCNet;
import org.zeith.mccue.McCue;
import org.zeith.mccue.api.ctx.ObjectContextFactory;
import org.zeith.mccue.api.event.RgbTriggerEvent;
import org.zeith.mccue.net.PacketActivateTrigger;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class RgbTrigger
{
	private final ResourceLocation id;
	protected Supplier<ObjectContextFactory> ctxFactory = ObjectContextFactory.EMPTY_FACTORY;
	protected ObjectContextFactory context;

	public RgbTrigger(String id)
	{
		this(Loader.instance().activeModContainer().getModId(), id);
	}

	public RgbTrigger(String modid, String id)
	{
		this(new ResourceLocation(modid, id));
	}

	public RgbTrigger(ResourceLocation id)
	{
		this.id = id;
		if(RgbRegistry.TRIGGER_REGISTRY.containsKey(id))
			throw new IllegalArgumentException("Trigger ID \"" + id + "\" is already in use!");
		RgbRegistry.TRIGGER_REGISTRY.put(id, this);
		if(this instanceof ITickable)
		{
			RgbRegistry.TICKABLES.add((ITickable) this);
		}
		RgbRegistry.TRIGGERS.add(this);
	}

	@SideOnly(value = Side.CLIENT)
	public void bindTexture()
	{
		UtilsFX.bindTexture(this.getId().getNamespace(), "textures/rgb/trigger/" + this.getId().getPath() + ".png");
	}

	public final ResourceLocation getId()
	{
		return this.id;
	}

	public boolean triggerMatches(RgbTaskEntry task, NBTTagCompound triggerInfo)
	{
		return true;
	}

	public void trigger(@Nullable NBTTagCompound triggerInfo)
	{
		if(!MinecraftForge.EVENT_BUS.post(new RgbTriggerEvent(this)))
		{
			McCue.proxy.a(this, triggerInfo);
		}
	}

	public final void trigger(EntityPlayer player, @Nullable NBTTagCompound triggerInfo)
	{
		EntityPlayer c = McCue.proxy.c();
		if(c != null && c == player)
		{
			this.trigger(triggerInfo);
		} else if(player instanceof EntityPlayerMP && !player.world.isRemote)
		{
			HCNet.INSTANCE.sendTo(new PacketActivateTrigger().fill(this, triggerInfo), (EntityPlayerMP) player);
		}
	}

	@SideOnly(value = Side.CLIENT)
	public String getTriggerDisplayName()
	{
		return I18n.format("mccue.trigger." + this.id + ".name");
	}

	@SideOnly(value = Side.CLIENT)
	public String getTriggerNameIf()
	{
		return I18n.format("mccue.trigger." + this.id + ".if");
	}

	public RgbTrigger setContextFactory(Supplier<ObjectContextFactory> factory)
	{
		if(factory == null)
		{
			throw new NullPointerException("factory == null");
		}
		if(this.ctxFactory != ObjectContextFactory.EMPTY_FACTORY)
		{
			throw new IllegalStateException("The context factory has already been assigned to trigger '" + this.getId() + "\"");
		}
		this.ctxFactory = factory;
		return this;
	}

	public ObjectContextFactory getContext()
	{
		if(this.context == null)
		{
			this.context = this.ctxFactory.get();
			if(this.context == null)
			{
				throw new RuntimeException("Context factory not initialized!");
			}
		}
		return this.context;
	}
}