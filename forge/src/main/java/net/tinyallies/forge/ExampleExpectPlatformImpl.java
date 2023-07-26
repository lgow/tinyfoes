package net.tinyallies.forge;

import net.minecraftforge.fml.loading.FMLPaths;
import net.tinyallies.TinyAlliesExpectPlatform;

import java.nio.file.Path;

public class ExampleExpectPlatformImpl {
	/**
	 * This is our actual method to {@link TinyAlliesExpectPlatform#getConfigDirectory()}.
	 */
	public static Path getConfigDirectory() {
		return FMLPaths.CONFIGDIR.get();
	}
}
