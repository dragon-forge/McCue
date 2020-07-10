package com.zeitheron.mccue.api.base;

import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.mccue.api.RgbTask;
import com.zeitheron.mccue.api.ctx.ObjectContextFactory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class RgbAnimatorEntry
{
	private final ResourceLocation id;
	protected Supplier<ObjectContextFactory> ctxFactory = ObjectContextFactory.EMPTY_FACTORY;
	protected ObjectContextFactory context;

	public RgbAnimatorEntry(String id)
	{
		this(Loader.instance().activeModContainer().getModId(), id);
	}

	public RgbAnimatorEntry(String modid, String id)
	{
		this(new ResourceLocation(modid, id));
	}

	public RgbAnimatorEntry(ResourceLocation id)
	{
		this.id = id;
		if(RgbRegistry.ANIMATOR_REGISTRY.containsKey(id))
			throw new IllegalArgumentException("Animator ID \"" + id + "\" is already in use!");
		RgbRegistry.ANIMATOR_REGISTRY.put(id, this);
		RgbRegistry.ANIMATORS.add(this);
	}

	@SideOnly(value = Side.CLIENT)
	public void bindTexture()
	{
		UtilsFX.bindTexture(this.getId().getNamespace(), "textures/rgb/animator/" + this.getId().getPath() + ".png");
	}

	public final ResourceLocation getId()
	{
		return this.id;
	}

	@SideOnly(value = Side.CLIENT)
	public String getAnimatorDisplayName()
	{
		return I18n.format("mccue.animator." + this.id + ".name");
	}

	@SideOnly(value = Side.CLIENT)
	public String getAnimatorNameThen()
	{
		return I18n.format("mccue.animator." + this.id + ".then");
	}

	public abstract RgbAnimator createAnimator(RgbTask var1);

	public RgbAnimatorEntry setContextFactory(Supplier<ObjectContextFactory> factory)
	{
		if(factory == null)
		{
			throw new NullPointerException("factory == null");
		}
		if(this.ctxFactory != ObjectContextFactory.EMPTY_FACTORY)
		{
			throw new IllegalStateException("The context factory has already been assigned to animator '" + this.getId() + "\"");
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

	public static RgbAnimatorEntry create(String id, Function<RgbTask, RgbAnimator> factory)
	{
		return RgbAnimatorEntry.create(Loader.instance().activeModContainer().getModId(), id, factory);
	}

	public static RgbAnimatorEntry create(String modid, String id, Function<RgbTask, RgbAnimator> factory)
	{
		return RgbAnimatorEntry.create(new ResourceLocation(modid, id), factory);
	}

	public static RgbAnimatorEntry create(ResourceLocation id, final Function<RgbTask, RgbAnimator> factory)
	{
		return new RgbAnimatorEntry(id)
		{
			@Override
			public RgbAnimator createAnimator(RgbTask task)
			{
				return factory.apply(task);
			}
		};
	}
}