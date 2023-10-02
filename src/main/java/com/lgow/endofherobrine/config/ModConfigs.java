package com.lgow.endofherobrine.config;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigs {
	public static final Client CLIENT;

	public static final Common COMMON;

	public static final ForgeConfigSpec CLIENT_SPEC, COMMON_SPEC;

	private static ForgeConfigSpec.IntValue REMAIN_POSSESSED_TICKS, SPAWN_COOLDOWN, SPAWN_CHANCE;

	private static ForgeConfigSpec.BooleanValue EYE_GLOW, SHOW_NAMETAG, LEGACY_STRUCTURES, MOB_POSSESSION, REVERT_POSSESSION, SPAWN_BUILDER;

	private static ForgeConfigSpec.ConfigValue<String> BLACKLIST_POSSESSION;

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
			REVERT_POSSESSION = builder.comment("Should possessed animals convert back?").define("revertPossession", true);
			REMAIN_POSSESSED_TICKS = builder.comment("Time in ticks mobs will remain possessed  #Default: 1/2 minecraft day")
					.defineInRange("remainPossessedTicks", 12000, 0, Integer.MAX_VALUE);
			BLACKLIST_POSSESSION = builder.comment("Blacklist mobs from being possessed #Example: \"husk, pig, villager\"").define("blacklistPossession", "");
			builder.comment("\n## Herobrine Spawn Rate ###\n");
			SPAWN_COOLDOWN = builder.comment("Time in ticks herobrine will wait to try to spawn again").defineInRange(
					"spawnCooldown", 3600, 0, Integer.MAX_VALUE);
			SPAWN_CHANCE = builder.comment("The chance out of a hundred that herobrine will spawn").defineInRange(
					"spawnChance", 10, 0, 100);
			SPAWN_BUILDER = builder.comment("Should spawn herobrine builder?").define("spawnBuilder", false);
			builder.pop();
		}
	}
	public static int getRemainPossessedTicks() {
		return REMAIN_POSSESSED_TICKS.get();
	}

	public static int getSpawnCooldown() {
		return SPAWN_COOLDOWN.get();
	}

	public static int getSpawnChance() {
		return SPAWN_CHANCE.get();
	}

	public static boolean shouldEyesGlow() {
		return EYE_GLOW.get();
	}

	public static boolean shouldShowHerobrineNametag() {
		return SHOW_NAMETAG.get();
	}

	public static boolean shouldBuildLegacyStructures() {
		return LEGACY_STRUCTURES.get();
	}

	public static boolean shouldDoMobPossession() {
		return MOB_POSSESSION.get();
	}

	public static boolean shouldPreventPossession(EntityType<? extends Mob> entity) {
		return BLACKLIST_POSSESSION.get().contains(entity.getDescriptionId().substring(17));
	}

	public static boolean shouldRevertPossession( boolean canConvert) {
		return REVERT_POSSESSION.get() && canConvert;
	}

	public static boolean shouldSpawnBuilder() {
		return SPAWN_BUILDER.get();
	}
}
