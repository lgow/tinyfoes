package net.tinyallies.forge;

import net.minecraftforge.fml.loading.FMLPaths;
import net.tinyallies.common.TinyFoesExpectPlatform;

import java.nio.file.Path;

public class ForgeTinyFoesExpectPlatformImpl {
	/**
	 * This is our actual method to {@link TinyFoesExpectPlatform#getConfigDirectory()}.
	 */
	public static Path getConfigDirectory() {
		return FMLPaths.CONFIGDIR.get();
	}
}
