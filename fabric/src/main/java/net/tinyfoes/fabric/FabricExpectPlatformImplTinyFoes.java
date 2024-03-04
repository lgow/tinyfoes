package net.tinyfoes.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.tinyfoes.common.CommonExpectPlatformTinyFoes;

import java.nio.file.Path;

public class FabricExpectPlatformImplTinyFoes {
	/**
	 * This is our actual method to {@link CommonExpectPlatformTinyFoes#getConfigDirectory()}.
	 */
	public static Path getConfigDirectory() {
		return FabricLoader.getInstance().getConfigDir();
	}
}
