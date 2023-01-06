package com.lgow.endofherobrine.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigs {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Server SERVER;
    public static ForgeConfigSpec.ConfigValue<Boolean> EYE_GLOW;
    public static ForgeConfigSpec.ConfigValue<Boolean> SHOW_NAMETAG;
    public static ForgeConfigSpec.BooleanValue LEGACY_STRUCTURES;
    public static ForgeConfigSpec.BooleanValue CONVERT_BACK;
    public static ForgeConfigSpec.IntValue REMAIN_POSSESSED;
    public static ForgeConfigSpec.IntValue TICK_DELAY;
    public static ForgeConfigSpec.IntValue SPAWN_DELAY;
    public static ForgeConfigSpec.IntValue SPAWN_CHANCE;


    public static class Client {

        Client(ForgeConfigSpec.Builder builder) {

            builder.push("Client configs for The End of Herobrine Mod");

            EYE_GLOW = builder.comment("Should Herobrine's and possessed creatures' eyes glow?")
                    .define("eyeGlow", true);
            SHOW_NAMETAG = builder.comment("Should show Herobrine's nametag?")
                    .define("showNametag", false);

            builder.pop();
        }
    }

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Common {

        Common (ForgeConfigSpec.Builder builder) {

            builder.push("Common configs for The End of Herobrine Mod");

            EYE_GLOW = builder.comment("Should Herobrine's and possessed creatures' eyes glow?")
                    .define("eyeGlow", true);
            builder.comment("");
            SHOW_NAMETAG = builder.comment("Should show Herobrine's nametag?")
                    .define("showNametag", false);
            LEGACY_STRUCTURES = builder.comment("Should the Builder build the 1.7.10 structures?")
                    .define("legacyStructures", false);
            CONVERT_BACK = builder.comment("Should possessed animals convert back?")
                    .define("convertBack", true);
            REMAIN_POSSESSED = builder.comment("Time in ticks mobs will remain possessed #Default: 1/2 minecraft day")
                    .defineInRange("remainPossessed", 12000, 0, Integer.MAX_VALUE);
            TICK_DELAY = builder.comment("Minimum time in ticks the mod will wait to try to execute herobrine spawn logic")
                    .defineInRange("tickDelay", 600, 0, Integer.MAX_VALUE);
            SPAWN_DELAY = builder.comment("Time in ticks herobrine will wait to try to spawn again")
                    .defineInRange("spawnDelay", 3600, 0, Integer.MAX_VALUE);
            SPAWN_CHANCE = builder.comment("The chance out of a hundred that herobrine will spawn")
                    .defineInRange("spawnChance", 40, 1, 100);

            builder.pop();
        }
    }

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Server {

        Server(ForgeConfigSpec.Builder builder) {

            builder.push("Server configs for The End of Herobrine Mod");

            LEGACY_STRUCTURES = builder.comment("Should the Builder build the 1.7.10 structures?")
                    .define("legacyStructures", false);
            CONVERT_BACK = builder.comment("Should possessed animals convert back?")
                    .define("convertBack", true);
            REMAIN_POSSESSED = builder.comment("Time in ticks mobs will remain possessed #Default: 1/2 minecraft day")
                    .defineInRange("remainPossessed", 12000, 0, Integer.MAX_VALUE);
            TICK_DELAY = builder.comment("Minimum time in ticks the mod will wait to try to execute herobrine spawn logic")
                    .defineInRange("tickDelay", 600, 0, Integer.MAX_VALUE);
            SPAWN_DELAY = builder.comment("Time in ticks herobrine will wait to try to spawn again")
                    .defineInRange("spawnDelay", 3600, 0, Integer.MAX_VALUE);
            SPAWN_CHANCE = builder.comment("The chance out of one hundred that a herobrine variant will spawn")
                    .defineInRange("spawnChance", 40, 1, 100);

            builder.pop();
        }
    }

    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }
}
