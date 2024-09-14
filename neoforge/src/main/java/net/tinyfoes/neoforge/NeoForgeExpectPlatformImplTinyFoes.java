package net.tinyfoes.neoforge;

import net.neoforged.fml.loading.FMLPaths;
import net.tinyfoes.common.CommonExpectPlatformTinyFoes;

import java.nio.file.Path;

public class NeoForgeExpectPlatformImplTinyFoes {
	/**
	 * This is our actual method to {@link CommonExpectPlatformTinyFoes#getConfigDirectory()}.
	 */
	public static Path getConfigDirectory() {
		return FMLPaths.CONFIGDIR.get();
	}
}
