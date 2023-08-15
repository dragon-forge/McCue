package org.zeith.mccue;

import com.zeitheron.hammercore.internal.SimpleRegistration;
import net.minecraft.crash.CrashReport;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ReportedException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.*;
import org.zeith.mccue.api.McCueToggles;
import org.zeith.mccue.block.BlockRedstoneTrigger;
import org.zeith.mccue.init.*;

import java.io.File;

@Mod(modid = "mccue",
		name = "Mc Cue",
		version = "@VERSION@",
		certificateFingerprint = "9f5e2a811a8332a842b34f6967b7db0ac4f24856",
		dependencies = "required-after:hammercore",
		updateJSON = "https://api.modrinth.com/updates/WbZWY2NM/forge_updates.json"
)
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
		throw new ReportedException(new CrashReport("Mod has been modified! Re-download it from https://www.curseforge.com/projects/341357", new IllegalStateException(
				"Expected fingerprint is " + e.getExpectedFingerprint())));
	}
	
	@Mod.EventHandler
	public void pre(FMLPreInitializationEvent e)
	{
		File f = new File(e.getModConfigurationDirectory(), "mccue");
		if(!f.isDirectory()) f.mkdirs();
		MinecraftForge.EVENT_BUS.register(proxy);
		modCfgDir = f;
		proxy.b();
		if(!McCueToggles.mcCUEAsLibrary)
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