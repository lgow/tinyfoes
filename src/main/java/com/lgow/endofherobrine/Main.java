package com.lgow.endofherobrine;

import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import com.lgow.endofherobrine.entity.herobrine.boss.HerobrineBoss;
import com.lgow.endofherobrine.entity.possessed.*;
import com.lgow.endofherobrine.entity.possessed.animal.*;
import com.lgow.endofherobrine.event.Events;
import com.lgow.endofherobrine.registries.ModRegistries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.lgow.endofherobrine.entity.EntityInit.*;
import static net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND;
import static net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;
import static net.minecraftforge.event.entity.SpawnPlacementRegisterEvent.Operation.AND;

@Mod(Main.MOD_ID)
public class Main {

    public static final String MOD_ID = "endofherobrine";

    public Main() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::attributes);
        bus.addListener(this::spawnPlacements);
        ModRegistries.register();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Events());
    }


    private void attributes(EntityAttributeCreationEvent event) {
        event.put(HEROBRINE_BOSS.get(), HerobrineBoss.createAttributes().build());
        event.put(BUILDER.get(), AbstractHerobrine.setCustomAttributes().build());
        event.put(LURKER.get(), AbstractHerobrine.setCustomAttributes().build());

        event.put(POS_CHICKEN.get(), PosChicken.setCustomAttributes().build());
        event.put(POS_COW.get(), PosCow.setCustomAttributes().build());
        event.put(POS_HUSK.get(), PosHusk.setCustomAttributes().build());
        event.put(POS_PIG.get(), PosPig.setCustomAttributes().build());
        event.put(POS_PIGMAN.get(), PosPigman.setCustomAttributes().build());
        event.put(POS_RABBIT.get(), PosRabbit.setCustomAttributes().build());
        event.put(POS_SHEEP.get(), PosSheep.setCustomAttributes().build());
        event.put(POS_SILVERFISH.get(), PosSilverfish.setCustomAttributes().build());
        event.put(POS_SKELETON.get(), PosSkeleton.setCustomAttributes().build());
        event.put(POS_STRAY.get(), PosStray.setCustomAttributes().build());
        event.put(POS_VILLAGER.get(), PosZombieVillager.setCustomAttributes().build());
        event.put(POS_ZOMBIE.get(), PosZombie.setCustomAttributes().build());
        event.put(POS_ZOMBIE_VILLAGER.get(), PosZombieVillager.setCustomAttributes().build());
    }

    private void spawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(POS_CHICKEN.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosChicken::checkPosAnimalSpawnRules, AND);
        event.register(POS_COW.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosCow::checkPosAnimalSpawnRules, AND);
        event.register(POS_HUSK.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosHusk::checkPosMonsterSpawnRules, AND);
        event.register(POS_PIG.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosPig::checkPosAnimalSpawnRules, AND);
        event.register(POS_RABBIT.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosRabbit::checkPosAnimalSpawnRules, AND);
        event.register(POS_SHEEP.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosSheep::checkPosAnimalSpawnRules, AND);
        event.register(POS_SILVERFISH.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosSilverfish::checkAnyLightMonsterSpawnRules, AND);
        event.register(POS_SKELETON.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosSkeleton::checkPosMonsterSpawnRules, AND);
        event.register(POS_STRAY.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosStray::checkPosMonsterSpawnRules, AND);
        event.register(POS_ZOMBIE.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosZombie::checkPosMonsterSpawnRules, AND);
        event.register(POS_ZOMBIE_VILLAGER.get(), ON_GROUND, MOTION_BLOCKING_NO_LEAVES, PosZombieVillager::checkPosMonsterSpawnRules, AND);
    }
}