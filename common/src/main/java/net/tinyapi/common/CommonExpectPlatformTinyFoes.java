package net.tinyapi.common;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class CommonExpectPlatformTinyFoes {
	@ExpectPlatform
	public static Path getConfigDirectory() {
		throw new AssertionError();
	}
}
