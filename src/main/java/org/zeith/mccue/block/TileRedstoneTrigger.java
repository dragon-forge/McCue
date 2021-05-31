package org.zeith.mccue.block;

import com.zeitheron.hammercore.client.gui.impl.container.ContainerEmpty;
import com.zeitheron.hammercore.tile.TileSyncable;
import org.zeith.mccue.client.gui.GuiRedstoneTrigger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class TileRedstoneTrigger
		extends TileSyncable
{
	public String key = "";
	public UUID owner = UUID.randomUUID();

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	@Override
	public boolean hasGui()
	{
		return true;
	}

	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiRedstoneTrigger(new ContainerEmpty(), this);
	}

	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerEmpty();
	}

	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setString("Key", this.key);
		nbt.setUniqueId("OwnerUUID", this.owner);
	}

	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		this.key = nbt.getString("Key");
		this.owner = nbt.getUniqueId("OwnerUUID");
	}
}