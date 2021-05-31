package org.zeith.mccue.net;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.utils.base.Cast;
import org.zeith.mccue.block.TileRedstoneTrigger;
import org.zeith.mccue.client.gui.GuiRedstoneTrigger;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public class PacketSyncLKey
		implements IPacket
{
	public long pos;
	public String key;

	public PacketSyncLKey fill(BlockPos pos, String key)
	{
		this.key = key;
		this.pos = pos.toLong();
		return this;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("key", this.key);
		nbt.setLong("pos", this.pos);
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		this.key = nbt.getString("key");
		this.pos = nbt.getLong("pos");
	}

	@SideOnly(value = Side.CLIENT)
	public void executeOnClient2(PacketContext net)
	{
		GuiRedstoneTrigger.acceptKey = this.key;
	}

	public void executeOnServer2(PacketContext net)
	{
		TileRedstoneTrigger t;
		EntityPlayerMP sender = net.getSender();
		if(sender != null && (t = Cast.cast(sender.world.getTileEntity(BlockPos.fromLong(this.pos)), TileRedstoneTrigger.class)) != null && Objects.equals(t.owner, sender.getUniqueID()))
			t.key = this.key;
	}
}