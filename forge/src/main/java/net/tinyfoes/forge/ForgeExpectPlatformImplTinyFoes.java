package net.tinyfoes.forge;

import net.minecraftforge.fml.loading.FMLPaths;
import net.tinyfoes.common.CommonExpectPlatformTinyFoes;

import java.nio.file.Path;

public class ForgeExpectPlatformImplTinyFoes {
	/**
	 * This is our actual method to {@link CommonExpectPlatformTinyFoes#getConfigDirectory()}.
	 */
	public static Path getConfigDirectory() {
		return FMLPaths.CONFIGDIR.get();
	}
}
