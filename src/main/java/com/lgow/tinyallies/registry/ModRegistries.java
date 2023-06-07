package com.lgow.tinyallies.registry;

import com.lgow.tinyallies.entity.EntityInit;
import com.lgow.tinyallies.items.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import static com.lgow.tinyallies.Main.MODID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class ModRegistries {
	public static final DeferredRegister<EntityType<?>> MOD_ENTITIES = create(ENTITY_TYPES, MODID);

	public static final DeferredRegister<Item> MOD_ITEMS = create(ITEMS, MODID);

	public static void register() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		MOD_ENTITIES.register(modEventBus);
		MOD_ITEMS.register(modEventBus);
		EntityInit.register();
		ModItems.register();
	}
}
