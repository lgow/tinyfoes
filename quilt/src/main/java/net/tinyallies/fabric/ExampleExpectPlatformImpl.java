package net.tinyallies.fabric;

import net.tinyallies.TinyAlliesExpectPlatform;
import org.quiltmc.loader.api.QuiltLoader;

import java.nio.file.Path;

public class ExampleExpectPlatformImpl {
	/**
	 * This is our actual method to {@link TinyAlliesExpectPlatform#getConfigDirectory()}.
	 */
	public static Path getConfigDirectory() {
		return QuiltLoader.getConfigDir();
	}
}
