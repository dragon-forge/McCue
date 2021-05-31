/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTTagCompound
 */
package org.zeith.mccue.api.ctx;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class NbtBasedContext
{
	public final NBTTagCompound compound;
	public final List<ObjectContextFactory> context;

	public NbtBasedContext(NBTTagCompound nbt, ObjectContextFactory ctx)
	{
		this.compound = nbt;
		this.context = Arrays.asList(ctx);
	}

	public NbtBasedContext(NBTTagCompound nbt, List<ObjectContextFactory> ctx)
	{
		this.compound = nbt;
		this.context = Collections.unmodifiableList(ctx);
	}

	public boolean isContextFullfilled()
	{
		return !this.context.stream().map(ObjectContextFactory::getRequiredFields).flatMap(Collection::stream).filter(key -> !this.compound.hasKey(key)).findAny().isPresent();
	}

	public static boolean isContextFullfilled(NBTTagCompound nbt, ObjectContextFactory... factories)
	{
		return NbtBasedContext.isContextFullfilled(nbt, Arrays.stream(factories));
	}

	public static boolean isContextFullfilled(NBTTagCompound nbt, Stream<ObjectContextFactory> factories)
	{
		return !factories.map(ObjectContextFactory::getRequiredFields).flatMap(Collection::stream).filter(key -> !nbt.hasKey(key)).findAny().isPresent();
	}
}

