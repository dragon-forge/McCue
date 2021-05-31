package org.zeith.mccue.init;

import com.zeitheron.hammercore.utils.color.ColorNamePicker;
import org.zeith.mccue.api.RgbTask;
import org.zeith.mccue.api.base.RgbShader;
import org.zeith.mccue.api.base.RgbShaderEntry;
import org.zeith.mccue.api.base.RgbTaskEntry;
import org.zeith.mccue.api.ctx.ObjectContextFactory;
import org.zeith.mccue.custom.shaders.RgbColorShader;
import org.zeith.mccue.custom.shaders.RgbRainbowShader;
import net.minecraft.client.resources.I18n;

import java.awt.*;

public class ShadersMC
{
	public static final RgbShaderEntry COLOR = new RgbShaderEntry("color")
	{
		@Override
		public RgbShader createShader(RgbTask task)
		{
			return new RgbColorShader(task);
		}

		@Override
		public String getShaderNameWith(RgbTaskEntry task)
		{
			if(task == null || task.getTaskData() == null || !task.getTaskData().hasKey("S_color", 99))
				return super.getShaderNameWith(task);
			String cl = ColorNamePicker.getColorNameFromHex(task.getTaskData().getInteger("S_color"));
			String i18cl = "color." + cl.toLowerCase().replaceAll(" ", "_");
			return I18n.format("mccue.shader." + getId() + ".with2", I18n.hasKey(i18cl) ? I18n.format(i18cl) : cl);
		}
	}.setContextFactory(() -> ObjectContextFactory.builder().require("S_color", Color.class).build());

	public static final RgbShaderEntry RAINBOW = RgbShaderEntry.create("rainbow", RgbRainbowShader::new).setContextFactory(() -> ObjectContextFactory.builder().require("S_speed", Long.TYPE).build());

	public static void init()
	{
	}
}