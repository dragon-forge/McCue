package com.zeitheron.mccue.init;

import com.zeitheron.hammercore.utils.color.ColorNamePicker;
import com.zeitheron.mccue.api.RgbTask;
import com.zeitheron.mccue.api.base.RgbShader;
import com.zeitheron.mccue.api.base.RgbShaderEntry;
import com.zeitheron.mccue.api.base.RgbTaskEntry;
import com.zeitheron.mccue.api.ctx.ObjectContextFactory;
import com.zeitheron.mccue.custom.shaders.RgbColorShader;
import com.zeitheron.mccue.custom.shaders.RgbRainbowShader;
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