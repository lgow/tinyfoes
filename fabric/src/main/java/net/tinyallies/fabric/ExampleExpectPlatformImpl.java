package net.tinyallies.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.tinyallies.TinyAlliesExpectPlatform;

import java.nio.file.Path;

public class ExampleExpectPlatformImpl {
	/**
	 * This is our actual method to {@link TinyAlliesExpectPlatform#getConfigDirectory()}.
	 */
	public static Path getConfigDirectory() {
		return FabricLoader.getInstance().getConfigDir();
	}
}
