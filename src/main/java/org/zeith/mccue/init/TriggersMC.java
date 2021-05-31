package org.zeith.mccue.init;

import org.zeith.mccue.api.base.RgbTaskEntry;
import org.zeith.mccue.api.base.RgbTrigger;
import org.zeith.mccue.api.ctx.ObjectContextFactory;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class TriggersMC
{
	public static final RgbTrigger PLAYER_HURT = new RgbTrigger("hurt");
	public static final RgbTrigger BLOCK_DESTROY = new RgbTrigger("block_destroy");
	public static final RgbTrigger BLOCK_HARVEST = new RgbTrigger("block_harvest");
	public static final RgbTrigger PLAYER_ATTACK = new RgbTrigger("attack");
	public static final RgbTrigger LOAD_COMPLETE = new RgbTrigger("load_complete");
	public static final RgbTrigger ACTION_PERFORMED = new RgbTrigger("action_performed");
	public static final RgbTrigger BLOCK_PLACED = new RgbTrigger("block_placed");

	public static final RgbTrigger REDSTONE_TRIGGER = new RgbTrigger("redstone_trigger")
	{
		@Override
		public boolean triggerMatches(RgbTaskEntry task, NBTTagCompound triggerInfo)
		{
			return triggerInfo != null && Objects.equals(triggerInfo.getString("T_key"), task.getTaskData().getString("T_key"));
		}
	}.setContextFactory(() -> ObjectContextFactory.builder().require("T_key", String.class).build());

	public static void init()
	{
	}
}