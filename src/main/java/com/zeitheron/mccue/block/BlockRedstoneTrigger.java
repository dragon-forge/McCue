package com.zeitheron.mccue.block;

import com.zeitheron.hammercore.internal.GuiManager;
import com.zeitheron.hammercore.internal.blocks.base.BlockTileHC;
import com.zeitheron.hammercore.internal.blocks.base.IBlockEnableable;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.utils.WorldUtil;
import com.zeitheron.mccue.init.TriggersMC;
import com.zeitheron.mccue.net.PacketSyncLKey;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class BlockRedstoneTrigger
		extends BlockTileHC<TileRedstoneTrigger>
{
	public BlockRedstoneTrigger()
	{
		super(Material.ROCK, TileRedstoneTrigger.class, "redstone_trigger");
		this.setHardness(2.0f);
		this.setHarvestLevel("pickaxe", 1);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, IBlockEnableable.ENABLED);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (Boolean) state.getValue((IProperty) IBlockEnableable.ENABLED) != false ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(IBlockEnableable.ENABLED, Boolean.valueOf(meta > 0));
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileRedstoneTrigger tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileRedstoneTrigger.class);
		if(tile == null)
		{
			tile = new TileRedstoneTrigger();
			worldIn.setTileEntity(pos, tile);
		}
		tile.setOwner(placer.getUniqueID());
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		String key = "";
		TileRedstoneTrigger t = WorldUtil.cast(worldIn.getTileEntity(pos), TileRedstoneTrigger.class);
		if(t == null)
		{
			return false;
		}
		key = t.key;
		if(Objects.equals(playerIn.getUniqueID(), t.owner))
		{
			if(!worldIn.isRemote && playerIn instanceof EntityPlayerMP)
			{
				HCNet.INSTANCE.sendTo(new PacketSyncLKey().fill(pos, key), (EntityPlayerMP) playerIn);
			}
			GuiManager.openGui(playerIn, t);
			return true;
		}
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		boolean isRedstone;
		if(worldIn.isRemote)
		{
			return;
		}
		TileRedstoneTrigger tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileRedstoneTrigger.class);
		if(tile == null)
		{
			return;
		}
		boolean redstone = worldIn.getRedstonePowerFromNeighbors(pos) > 0;
		if(redstone != (isRedstone = ((Boolean) state.getValue((IProperty) IBlockEnableable.ENABLED)).booleanValue()))
		{
			EntityPlayerMP owner;
			worldIn.setBlockState(pos, state.withProperty(IBlockEnableable.ENABLED, Boolean.valueOf(redstone)), 3);
			tile.validate();
			worldIn.setTileEntity(pos, tile);
			if(redstone && (owner = worldIn.getMinecraftServer().getPlayerList().getPlayerByUUID(tile.owner)) != null)
			{
				NBTTagCompound triggerInfo = new NBTTagCompound();
				triggerInfo.setString("T_key", tile.key);
				TriggersMC.REDSTONE_TRIGGER.trigger(owner, triggerInfo);
			}
		}
	}
}