package com.lgow.endofherobrine;

import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import com.lgow.endofherobrine.entity.herobrine.boss.HerobrineBoss;
import com.lgow.endofherobrine.entity.possessed.*;
import com.lgow.endofherobrine.entity.possessed.animal.*;
import com.lgow.endofherobrine.event.CapabilityEvents;
import com.lgow.endofherobrine.event.PossessionEvents;
import com.lgow.endofherobrine.event.RandomEvents;
import com.lgow.endofherobrine.event.WrathHandler;
import com.lgow.endofherobrine.registries.ModRegistries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.lgow.endofherobrine.entity.EntityInit.*;

@Mod(Main.MOD_ID)
public class Main {
	public static final String MOD_ID = "endofherobrine";

	public Main() {
		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::commonSetup);
		bus.addListener(this::attributes);
		ModRegistries.register();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new PossessionEvents());
		MinecraftForge.EVENT_BUS.register(new RandomEvents());
		MinecraftForge.EVENT_BUS.register(new WrathHandler());
		MinecraftForge.EVENT_BUS.register(new CapabilityEvents());

	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> { });
	}

	private void attributes(EntityAttributeCreationEvent event) {
		event.put(HEROBRINE_BOSS.get(), HerobrineBoss.createAttributes().build());
		event.put(BUILDER.get(), AbstractHerobrine.setCustomAttributes().build());
		event.put(LURKER.get(), AbstractHerobrine.setCustomAttributes().build());
		event.put(P_CHICKEN.get(), PosChicken.setCustomAttributes().build());
		event.put(P_COW.get(), PosCow.setCustomAttributes().build());
		event.put(P_HUSK.get(), PosHusk.setCustomAttributes().build());
		event.put(P_PIG.get(), PosPig.setCustomAttributes().build());
		event.put(PIGMAN.get(), PosPigman.setCustomAttributes().build());
		event.put(P_RABBIT.get(), PosRabbit.setCustomAttributes().build());
		event.put(P_SHEEP.get(), PosSheep.setCustomAttributes().build());
		event.put(P_SILVERFISH.get(), PosSilverfish.setCustomAttributes().build());
		event.put(P_SKELETON.get(), PosSkeleton.setCustomAttributes().build());
		event.put(P_STRAY.get(), PosStray.setCustomAttributes().build());
		event.put(P_VILlAGER.get(), PosZombieVillager.setCustomAttributes().build());
		event.put(P_ZOMBIE.get(), PosZombie.setCustomAttributes().build());
		event.put(P_ZOMBIE_VILLAGER.get(), PosZombieVillager.setCustomAttributes().build());
	}
}