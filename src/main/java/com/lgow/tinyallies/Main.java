package com.lgow.tinyallies;

import com.lgow.tinyallies.entity.EntityInit;
import com.lgow.tinyallies.event.Events;
import com.lgow.tinyallies.registry.ModRegistries;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.monster.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "tinyallies";
    private static final Logger LOGGER = LogUtils.getLogger();
    public Main() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::attributes);
        ModRegistries.register();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Events());
    }

    private void attributes(EntityAttributeCreationEvent event) {
        event.put(EntityInit.CREEPY.get(), Creeper.createAttributes().build());
        event.put(EntityInit.SKELLY.get(), Skeleton.createAttributes().build());
        event.put(EntityInit.ENDERBOY.get(), EnderMan.createAttributes().build());
        event.put(EntityInit.SPIDEY.get(), Spider.createAttributes().build());
        event.put(EntityInit.ZOMBY.get(), Zombie.createAttributes().build());
    }
}
