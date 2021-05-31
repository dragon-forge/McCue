package org.zeith.mccue.init;

import org.zeith.mccue.api.base.RgbAnimatorEntry;
import org.zeith.mccue.api.ctx.ObjectContextFactory;
import org.zeith.mccue.custom.animators.RgbDoubleFlashAnimator;
import org.zeith.mccue.custom.animators.RgbFlashAnimator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagFloat;

import java.util.function.Predicate;

public class AnimatorsMC
{
	public static final RgbAnimatorEntry FLASH = RgbAnimatorEntry.create("flash", RgbFlashAnimator::new).setContextFactory(() -> ObjectContextFactory.builder().require("A_time", Long.TYPE).optionally("A_sine", Boolean.TYPE, new NBTTagByte((byte) 1)).optionally("A_amplitude", Float.TYPE, new NBTTagFloat(100.0f)).restrict("A_amplitude", AnimatorsMC.numericalRange(0.0f, 100.0f, false)).build());
	public static final RgbAnimatorEntry DOUBLE_FLASH = RgbAnimatorEntry.create("double_flash", RgbDoubleFlashAnimator::new).setContextFactory(() -> ObjectContextFactory.builder().require("A_time1", Long.TYPE).optionally("A_sine1", Boolean.TYPE, new NBTTagByte((byte) 1)).require("A_time2", Long.TYPE).optionally("A_sine2", Boolean.TYPE, new NBTTagByte((byte) 1)).optionally("A_amplitude1", Float.TYPE, new NBTTagFloat(100.0f)).restrict("A_amplitude1", AnimatorsMC.numericalRange(0.0f, 100.0f, false)).optionally("A_amplitude2", Float.TYPE, new NBTTagFloat(100.0f)).restrict("A_amplitude2", AnimatorsMC.numericalRange(0.0f, 100.0f, false)).build());

	public static void init()
	{
	}

	public static final Predicate<NBTBase> numericalRange(float min, float max, boolean strict)
	{
		return tag ->
		{
			if(tag instanceof NBTPrimitive)
			{
				NBTPrimitive prim = (NBTPrimitive) tag;
				float val = prim.getFloat();
				return strict && val > min && val < max || !strict && val >= min && val <= max;
			}
			return false;
		};
	}
}