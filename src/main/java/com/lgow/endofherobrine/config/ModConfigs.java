package com.lgow.endofherobrine.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigs {
	public static final Client CLIENT;

	public static final Common COMMON;

	public static final ForgeConfigSpec CLIENT_SPEC, COMMON_SPEC;

	public static ForgeConfigSpec.IntValue REMAIN_POSSESSED, SPAWN_COOLDOWN, SPAWN_CHANCE;

	public static ForgeConfigSpec.BooleanValue EYE_GLOW, SHOW_NAMETAG, LEGACY_STRUCTURES, MOB_POSSESSION, CONVERT_BACK, SPAWN_BUILDER;

	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static class Client {
		Client(ForgeConfigSpec.Builder builder) {
			builder.push("Client configs for The End of Herobrine Mod");
			EYE_GLOW = builder.comment("Should Herobrine's and possessed mobs' eyes glow?").define("eyeGlow", true);
			SHOW_NAMETAG = builder.comment("Should show Herobrine's nametag?").define("showNametag", false);
			builder.pop();
		}
	}

	public static class Common {
		Common(ForgeConfigSpec.Builder builder) {
			builder.push("Common configs for The End of Herobrine Mod");
			LEGACY_STRUCTURES = builder.comment("Should Herobrine build 1.7.10 structures?").define(
					"legacyStructures", false);
			builder.comment("\n## Possession ###\n");
			MOB_POSSESSION = builder.comment("Should mobs get possessed?").define("mobPossession", true);
			CONVERT_BACK = builder.comment("Should possessed animals convert back?").define("revertPossession", true);
			REMAIN_POSSESSED = builder.comment("Time in ticks mobs will remain possessed #Default: 1/2 minecraft day")
					.defineInRange("remainPossessedTicks", 12000, 0, Integer.MAX_VALUE);
			builder.comment("\n## Herobrine Spawn Rate ###\n");
			
			SPAWN_COOLDOWN = builder.comment("Time in ticks herobrine will wait to try to spawn again").defineInRange(
					"spawnCooldown", 3600, 0, Integer.MAX_VALUE);
			SPAWN_CHANCE = builder.comment("The chance out of a hundred that herobrine will spawn").defineInRange(
					"spawnChance", 10, 0, 100);
			SPAWN_BUILDER = builder.comment("Should spawn herobrine builder?").define("spawnBuilder", false);
			builder.pop();
		}
	}
}
