package com.zeitheron.mccue.api.base;

import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.mccue.api.RgbTask;
import com.zeitheron.mccue.api.ctx.ObjectContextFactory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class RgbShaderEntry
{
	private final ResourceLocation id;
	protected Supplier<ObjectContextFactory> ctxFactory = ObjectContextFactory.EMPTY_FACTORY;
	protected ObjectContextFactory context;

	public RgbShaderEntry(String id)
	{
		this(Loader.instance().activeModContainer().getModId(), id);
	}

	public RgbShaderEntry(String modid, String id)
	{
		this(new ResourceLocation(modid, id));
	}

	public RgbShaderEntry(ResourceLocation id)
	{
		this.id = id;
		if(RgbRegistry.SHADER_REGISTRY.containsKey(id))
			throw new IllegalArgumentException("Shader ID \"" + id + "\" is already in use!");
		RgbRegistry.SHADER_REGISTRY.put(id, this);
		RgbRegistry.SHADERS.add(this);
	}

	@SideOnly(value = Side.CLIENT)
	public void bindTexture()
	{
		UtilsFX.bindTexture(this.getId().getNamespace(), "textures/rgb/shader/" + this.getId().getPath() + ".png");
	}

	public final ResourceLocation getId()
	{
		return this.id;
	}

	@SideOnly(value = Side.CLIENT)
	public String getShaderDisplayName()
	{
		return I18n.format("mccue.shader." + this.id + ".name");
	}

	@SideOnly(value = Side.CLIENT)
	public String getShaderNameWith(@Nullable RgbTaskEntry task)
	{
		return I18n.format("mccue.shader." + this.id + ".with");
	}

	public abstract RgbShader createShader(RgbTask var1);

	public RgbShaderEntry setContextFactory(Supplier<ObjectContextFactory> factory)
	{
		if(factory == null)
		{
			throw new NullPointerException("factory == null");
		}
		if(this.ctxFactory != ObjectContextFactory.EMPTY_FACTORY)
		{
			throw new IllegalStateException("The context factory has already been assigned to shader '" + this.getId() + "\"");
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

	public static RgbShaderEntry create(String id, Function<RgbTask, RgbShader> factory)
	{
		return RgbShaderEntry.create(Loader.instance().activeModContainer().getModId(), id, factory);
	}

	public static RgbShaderEntry create(String modid, String id, Function<RgbTask, RgbShader> factory)
	{
		return RgbShaderEntry.create(new ResourceLocation(modid, id), factory);
	}

	public static RgbShaderEntry create(ResourceLocation id, final Function<RgbTask, RgbShader> factory)
	{
		return new RgbShaderEntry(id)
		{

			@Override
			public RgbShader createShader(RgbTask task)
			{
				return factory.apply(task);
			}
		};
	}
}