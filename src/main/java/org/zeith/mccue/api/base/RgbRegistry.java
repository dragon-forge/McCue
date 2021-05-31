package org.zeith.mccue.api.base;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.zeith.mccue.McCue;
import org.zeith.mccue.api.event.RegisterRgbTasksEvent;
import org.zeith.mccue.api.event.UnregisterRgbTasksEvent;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RgbRegistry
{
	static final List<ITickable> TICKABLES = new ArrayList<ITickable>();
	static final List<RgbTrigger> TRIGGERS = new ArrayList<RgbTrigger>();
	static final BiMap<ResourceLocation, RgbTrigger> TRIGGER_REGISTRY = HashBiMap.create();
	static final List<RgbAnimatorEntry> ANIMATORS = new ArrayList<RgbAnimatorEntry>();
	static final BiMap<ResourceLocation, RgbAnimatorEntry> ANIMATOR_REGISTRY = HashBiMap.create();
	static final List<RgbShaderEntry> SHADERS = new ArrayList<RgbShaderEntry>();
	static final BiMap<ResourceLocation, RgbShaderEntry> SHADER_REGISTRY = HashBiMap.create();
	public static final List<RgbTrigger> ALL_TRIGGERS = Collections.unmodifiableList(TRIGGERS);
	public static final List<RgbAnimatorEntry> ALL_ANIMATORS = Collections.unmodifiableList(ANIMATORS);
	public static final List<RgbShaderEntry> ALL_SHADERS = Collections.unmodifiableList(SHADERS);
	public static final List<RgbTaskEntry> RGB_TASKS = new ArrayList<RgbTaskEntry>();

	public static void masterReload()
	{
		McCue.proxy.d();
		RgbRegistry.reloadRgbTasks();
	}

	public static void reloadRgbTasks()
	{
		RGB_TASKS.clear();
		MinecraftForge.EVENT_BUS.post(new RegisterRgbTasksEvent());
	}

	public static void removeRgbTask(RgbTaskEntry entry)
	{
		RGB_TASKS.remove(entry);
		MinecraftForge.EVENT_BUS.post(new UnregisterRgbTasksEvent(entry));
	}

	public static RgbTrigger getTriggerById(ResourceLocation id)
	{
		return TRIGGER_REGISTRY.get(id);
	}

	public static RgbAnimatorEntry getAnimatorById(ResourceLocation id)
	{
		return ANIMATOR_REGISTRY.get(id);
	}

	public static RgbShaderEntry getShaderById(ResourceLocation id)
	{
		return SHADER_REGISTRY.get(id);
	}

	public static void globalTick()
	{
		TICKABLES.forEach(ITickable::update);
	}
}