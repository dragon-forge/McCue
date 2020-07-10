package com.zeitheron.mccue.api.ctx;

import com.zeitheron.mccue.api.ctx.cells.GuiStringCell;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@SideOnly(value = Side.CLIENT)
public class GuiCell
{
	private static final Map<Class<?>, BiFunction<ObjectContextFactory, String, GuiCell>> FACTORY_REGISTRY = new HashMap();
	private ICellSafe safe;
	private String labelI18n;
	protected final ObjectContextFactory ctx;
	protected final String field;
	protected int screenWidth;
	protected int ySize;
	protected boolean required;
	protected final Map<String, NBTBase> parsed = new HashMap<String, NBTBase>();

	public static void register(Class<?> type, BiFunction<ObjectContextFactory, String, GuiCell> factory)
	{
		FACTORY_REGISTRY.putIfAbsent(type, factory);
	}

	public static GuiCell create(ObjectContextFactory ctx, String field)
	{
		Class<?> type = ctx.getType(field);
		if(type != null && FACTORY_REGISTRY.containsKey(type))
		{
			return FACTORY_REGISTRY.get(type).apply(ctx, field);
		}
		return new GuiStringCell(ctx, field);
	}

	public GuiCell(ObjectContextFactory ctx, String nbtName)
	{
		this.ctx = ctx;
		this.field = nbtName;
	}

	public final String getField()
	{
		return this.field;
	}

	public String getLabel()
	{
		return I18n.format(this.labelI18n.toLowerCase()) + (this.required ? " *" : "");
	}

	public void setLabelI18n(String labelI18n, boolean required)
	{
		this.labelI18n = labelI18n;
		this.required = required;
	}

	public void loadCell(@Nullable NBTBase value)
	{
	}

	public void initCell(int screenWidth, int screenHeigth)
	{
		this.screenWidth = screenWidth;
	}

	public void render(int mouseX, int mouseY)
	{
		GuiUtils.drawGradientRect(0, 0, 0, this.screenWidth, this.ySize / 8, -16777216, 855638016);
		GuiUtils.drawGradientRect(0, 0, this.ySize / 8, this.screenWidth, 7 * this.ySize / 8, 855638016, 855638016);
		GuiUtils.drawGradientRect(0, 0, 7 * this.ySize / 8, this.screenWidth, this.ySize, 855638016, -16777216);
	}

	public void mouseClicked(int mouseX, int mouseY, int button) throws IOException
	{
	}

	public void handleKeyboardInput() throws IOException
	{
	}

	public void actionPerformed(GuiButton button) throws IOException
	{
	}

	public int getHeight()
	{
		return this.ySize;
	}

	protected boolean parse(String value)
	{
		try
		{
			ObjectContextFactory.ContextParseResult<? extends NBTBase> base = this.ctx.parseOrDefault(this.field, value);
			if(base.value != null)
			{
				this.parsed.put(base.field, base.value);
			}
			return base.successful || StringUtils.isNullOrEmpty(value) && base.defaulted;
		} catch(ObjectContextFactory.ContextFieldParseException egor)
		{
			egor.printStackTrace();
			return false;
		}
	}

	public final GuiCell setSafe(ICellSafe safe)
	{
		this.safe = safe;
		return this;
	}

	protected void callSave()
	{
		if(this.safe != null)
		{
			this.safe.save(this);
		}
	}

	public void apply(NBTTagCompound nbt)
	{
		this.parsed.entrySet().forEach(e -> nbt.setTag(e.getKey(), e.getValue()));
	}
}