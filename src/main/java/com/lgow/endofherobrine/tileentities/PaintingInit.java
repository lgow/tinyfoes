package com.lgow.endofherobrine.tileentities;

import net.minecraft.world.entity.decoration.PaintingVariant;

import static com.lgow.endofherobrine.registries.ModRegistries.MOD_PAINTINGS;

public class PaintingInit {
	static {
		registerPainting("altar", 32, 32);
		registerPainting("blackstone", 16, 32);
		registerPainting("crying_blood", 64, 64);
		registerPainting("empty", 32, 16);
		registerPainting("fight", 64, 32);
		registerPainting("lurker", 32, 16);
		registerPainting("netherrack", 16, 32);
	}

	public static void registerPainting(String name, int width, int height) {
		MOD_PAINTINGS.register(name, () -> new PaintingVariant(width, height));
	}

	public static void register() {
	}
}
