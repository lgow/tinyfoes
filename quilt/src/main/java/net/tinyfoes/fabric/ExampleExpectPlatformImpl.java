package net.tinyfoes.fabric;

import net.tinyfoes.common.TinyFoesExpectPlatform;
import org.quiltmc.loader.api.QuiltLoader;

import java.nio.file.Path;

public class ExampleExpectPlatformImpl {
	/**
	 * This is our actual method to {@link TinyFoesExpectPlatform#getConfigDirectory()}.
	 */
	public static Path getConfigDirectory() {
		return QuiltLoader.getConfigDir();
	}
}
