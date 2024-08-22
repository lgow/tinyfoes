package net.tinyfoes.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.tinyfoes.common.config.CommonConfigs;
import org.apache.commons.lang3.tuple.Pair;

public class ForgeConfigs {
	public static final Server SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;

	static {
		final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		SERVER_SPEC = specPair.getRight();
		SERVER = specPair.getLeft();
	}

	public static class Server {

		Server(ForgeConfigSpec.Builder builder) {
			builder.push("Server configs for Tiny Foes (only Hostiles)");
			CommonConfigs.Server.BABIES_DROP_LOOT = builder.comment("Should baby mobs drop loot?").define("babiesDropLoot", false);
			CommonConfigs.Server.BABY_HEALTH_MODIFIER = builder.comment("Baby health modifier?").define("babyHealthModifier", 0.5);
			CommonConfigs.Server.BABY_SPEED_MODIFIER = builder.comment("Baby health modifier?").define("babySpeedModifier", 1.5);
			CommonConfigs.Server.BABY_ATTACK_MODIFIER = builder.comment("Baby health modifier?").define("babyAttackModifier", 1.0);
			CommonConfigs.Server.BABY_XP_MODIFIER = builder.comment("Baby health modifier?").define("babyXpModifier", 2.0);
			builder.pop();
		}
	}
}
