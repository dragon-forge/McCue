package org.zeith.mccue;

import org.zeith.mccue.api.base.RgbRegistry;
import org.zeith.mccue.api.sdk.IBaseSDK;
import org.zeith.mccue.api.sdk.RgbSdkRegistry;
import org.zeith.mccue.init.TriggersMC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class EventListener
{
	@SubscribeEvent
	public static void attack(LivingAttackEvent e)
	{
		if(e.getEntity().world.isRemote) return;
		DamageSource src = e.getSource();
		if(src != null && src.getTrueSource() instanceof EntityPlayer)
		{
			TriggersMC.PLAYER_ATTACK.trigger((EntityPlayer) src.getTrueSource(), null);
		}
		if(e.getEntity() instanceof EntityPlayer && !((EntityPlayer) e.getEntity()).capabilities.disableDamage)
		{
			TriggersMC.PLAYER_HURT.trigger((EntityPlayer) e.getEntity(), null);
		}
	}

	@SubscribeEvent
	public static void harvest(BlockEvent.BreakEvent e)
	{
		if(e.getPlayer() != null && !e.getPlayer().world.isRemote)
		{
			(e.getState().getBlock().canHarvestBlock(e.getWorld(), e.getPos(), e.getPlayer()) && !e.getPlayer().capabilities.isCreativeMode ? TriggersMC.BLOCK_HARVEST : TriggersMC.BLOCK_DESTROY).trigger(e.getPlayer(), null);
		}
	}

	@SubscribeEvent
	@SideOnly(value = Side.CLIENT)
	public static void place(BlockEvent.PlaceEvent e)
	{
		TriggersMC.BLOCK_PLACED.trigger(e.getPlayer(), null);
	}

	@SubscribeEvent
	@SideOnly(value = Side.CLIENT)
	public static void ctick(TickEvent.ClientTickEvent e)
	{
		if(e.phase == TickEvent.Phase.END)
		{
			RgbRegistry.globalTick();
			for(IBaseSDK sdk : RgbSdkRegistry.SDKS)
			{
				sdk.getDispatcher().tick();
			}
		}
	}
}