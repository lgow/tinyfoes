package net.tinyallies;

import dev.architectury.registry.registries.Registries;
import net.tinyallies.registry.ModRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TinyAlliesCommon {
	public static final String MODID = "tinyallies";
	public static final Registries REGISTRIES = Registries.get(MODID);
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	//	public static final CreativeTabRegistry.TabSupplier TINY_TAB = CreativeTabRegistry.create(new TinyAlliesResLoc("tiny_tab"), () ->
	//			new ItemStack(ModItems.BABYFIER.get()));

	public static void init() {
		ModRegistries.register();
	}

	public static void commonSetup() {
	}
}