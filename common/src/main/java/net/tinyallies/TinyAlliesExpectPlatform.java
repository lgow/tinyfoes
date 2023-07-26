package net.tinyallies;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class TinyAlliesExpectPlatform {
	@ExpectPlatform
	public static Path getConfigDirectory() {
		throw new AssertionError();
	}
}
