package org.zeith.mccue;

import com.zeitheron.hammercore.internal.SimpleRegistration;
import org.zeith.mccue.block.BlockRedstoneTrigger;
import org.zeith.mccue.init.AnimatorsMC;
import org.zeith.mccue.init.ShadersMC;
import org.zeith.mccue.init.TriggersMC;
import net.minecraft.crash.CrashReport;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ReportedException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = "mccue", name = "Mc Cue", version = "1r", certificateFingerprint = "9f5e2a811a8332a842b34f6967b7db0ac4f24856", dependencies = "required-after:hammercore", updateJSON = "http://dccg.herokuapp.com/api/fmluc/341357")
public class McCue
{
	public static final Logger LOG = LogManager.getLogger("McCue");
	public static File modCfgDir;

	@SidedProxy(serverSide = "org.zeith.mccue.BaseProxy", clientSide = "org.zeith.mccue.client.ClientProxy")
	public static BaseProxy proxy;

	public static final BlockRedstoneTrigger REDSTONE_TRIGGER;

	@Mod.EventHandler
	public void constr(FMLConstructionEvent e)
	{
		TriggersMC.init();
		AnimatorsMC.init();
		ShadersMC.init();
		proxy.a();
	}

	@Mod.EventHandler
	public void finger(FMLFingerprintViolationEvent e)
	{
		if("@VERSION".contains("VERSION"))
			return;
		throw new ReportedException(new CrashReport("Mod has been modified! Re-download it from https://www.curseforge.com/projects/341357", new IllegalStateException("Expected fingerprint is " + e.getExpectedFingerprint())));
	}

	@Mod.EventHandler
	public void pre(FMLPreInitializationEvent e)
	{
		File f = e.getSuggestedConfigurationFile();
		if(!(f = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(46)))).isDirectory())
			f.mkdirs();
		MinecraftForge.EVENT_BUS.register(proxy);
		modCfgDir = f;
		proxy.b();
		SimpleRegistration.registerBlock(REDSTONE_TRIGGER, "mccue", CreativeTabs.REDSTONE);
	}

	@Mod.EventHandler
	public void complete(FMLLoadCompleteEvent e)
	{
		TriggersMC.LOAD_COMPLETE.trigger(null);
	}

	static
	{
		REDSTONE_TRIGGER = new BlockRedstoneTrigger();
	}
}