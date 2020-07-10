package com.zeitheron.mccue.net;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.mccue.api.base.RgbRegistry;
import com.zeitheron.mccue.api.base.RgbTrigger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketActivateTrigger
		implements IPacket
{
	public ResourceLocation trigger;
	public NBTTagCompound triggerInfo;

	public PacketActivateTrigger fill(RgbTrigger trigger, NBTTagCompound triggerInfo)
	{
		return this.fill(trigger.getId(), triggerInfo);
	}

	public PacketActivateTrigger fill(ResourceLocation trigger, NBTTagCompound triggerInfo)
	{
		this.trigger = trigger;
		this.triggerInfo = triggerInfo;
		return this;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("id", this.trigger.toString());
		if(this.triggerInfo != null) nbt.setTag("tag", this.triggerInfo);
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		this.trigger = new ResourceLocation(nbt.getString("id"));
		if(nbt.hasKey("tag")) this.triggerInfo = nbt.getCompoundTag("tag");
	}

	@SideOnly(value = Side.CLIENT)
	public void executeOnClient2(PacketContext net)
	{
		RgbTrigger t = RgbRegistry.getTriggerById(this.trigger);
		if(t != null) t.trigger(this.triggerInfo);
	}
}