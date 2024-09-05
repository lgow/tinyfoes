package net.tinyfoes.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class TinyFoesConfigs {
	public static final ForgeConfigSpec SERVER_SPEC, CLIENT_SPEC;
	private static final Server SERVER;
	private static final Client CLIENT;
	public static ForgeConfigSpec.ConfigValue<Double> BABY_MAX_HEALTH_MODIFIER, BABY_SPEED_MODIFIER, SPAWN_AS_BABY_ODDS;
	public static ForgeConfigSpec.BooleanValue BABIES_DROP_LOOT, VILLAGER_HEAD_FIX, OVERSIZED_ITEMS;

	static {
		final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		SERVER_SPEC = specPair.getRight();
		SERVER = specPair.getLeft();
	}

	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static class Client {
		Client(ForgeConfigSpec.Builder builder) {
			builder.push("Client configs for Tiny Foes");
			TinyFoesConfigs.VILLAGER_HEAD_FIX = builder.comment("Should resize baby villager head?").define(
					"villagerHeadFix", true);
			TinyFoesConfigs.OVERSIZED_ITEMS = builder.comment("Should the baby held items be oversized?").define(
					"oversizedItems", true);
			builder.pop();
		}
	}

	public static class Server {
		Server(ForgeConfigSpec.Builder builder) {
			builder.push("Server configs for Tiny Foes (only Hostiles)");
			TinyFoesConfigs.BABIES_DROP_LOOT = builder.comment("Should baby mobs drop loot?").define("babiesDropLoot",
					false);
			TinyFoesConfigs.BABY_MAX_HEALTH_MODIFIER = builder.comment("Baby max health modifier").defineInRange(
					"babyMaxHealthModifier", 0.0, 1.0, 1024.0);
			TinyFoesConfigs.BABY_SPEED_MODIFIER = builder.comment("Baby speed modifier").defineInRange(
					"babySpeedModifier", 1.5, 0.0, 1024.0);
			TinyFoesConfigs.SPAWN_AS_BABY_ODDS = builder.comment(
							"The chance out of monsters spawning as babies. (5% = 0.05 | 25% = 0.25 |100% = 1.0)")
					.defineInRange("babySpawnChance", 0.05, 0.0, 1.0);
			builder.pop();
		}
	}
}
