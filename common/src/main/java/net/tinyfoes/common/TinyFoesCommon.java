package net.tinyfoes.common;

import dev.architectury.registry.registries.Registries;
import net.tinyfoes.common.registry.ModRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TinyFoesCommon {
	public static final String MODID = "tinyfoes";
	public static final Registries REGISTRIES = Registries.get(MODID);
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static void init() {
		ModRegistries.register();
	}
}