package net.tinyfoes.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CommonConfigs {
	public static final Server SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;

	static {
		final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		SERVER_SPEC = specPair.getRight();
		SERVER = specPair.getLeft();
	}

	public static class Server {
		public static ForgeConfigSpec.ConfigValue<Double> BABY_HEALTH_MODIFIER, BABY_SPEED_MODIFIER, BABY_ATTACK_MODIFIER, BABY_XP_MODIFIER;
		public static ForgeConfigSpec.BooleanValue BABIES_DROP_LOOT;

		Server(ForgeConfigSpec.Builder builder) {
		}
	}
}
