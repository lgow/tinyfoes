package net.tinyallies.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.tinyallies.common.TinyFoesExpectPlatform;

import java.nio.file.Path;

public class FabricTinyFoesExpectPlatformImpl {
	/**
	 * This is our actual method to {@link TinyFoesExpectPlatform#getConfigDirectory()}.
	 */
	public static Path getConfigDirectory() {
		return FabricLoader.getInstance().getConfigDir();
	}
}
