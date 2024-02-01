package net.tinyallies.common;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class TinyFoesExpectPlatform {
	@ExpectPlatform
	public static Path getConfigDirectory() {
		throw new AssertionError();
	}
}
