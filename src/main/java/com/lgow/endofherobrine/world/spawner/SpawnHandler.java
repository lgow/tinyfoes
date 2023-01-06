package com.lgow.endofherobrine.world.spawner;

import com.lgow.endofherobrine.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class SpawnHandler
{
    private static Map<ResourceLocation, HerobrineSpawner> spawners = new HashMap<>();

    @SubscribeEvent
    public static void onWorldLoad(ServerStartingEvent event)
    {
        MinecraftServer server = event.getServer();
        spawners.put(BuiltinDimensionTypes.OVERWORLD.location(), new HerobrineSpawner(server, "herobrineOverworld"));
        spawners.put(BuiltinDimensionTypes.NETHER.location(), new HerobrineSpawner(server, "herobrineNether"));
    }

    @SubscribeEvent
    public static void onServerStart(ServerStoppedEvent event)
    {
        spawners.clear();
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;
        if(event.side != LogicalSide.SERVER) return;

        HerobrineSpawner spawner = spawners.get(event.level.dimension().location());
        if(spawner != null) {
            spawner.tick((ServerLevel) event.level);
        }
    }

    public static void register() { }
}