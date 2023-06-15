package com.lgow.endofherobrine.registries;

import com.lgow.endofherobrine.block.BlockInit;
import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.enchantment.EnchantmentInit;
import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.item.ItemInit;
import com.lgow.endofherobrine.item.ModTab;
import com.lgow.endofherobrine.tileentities.BlockEntityInit;
import com.lgow.endofherobrine.tileentities.PaintingInit;
import com.lgow.endofherobrine.world.spawner.SpawnEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import static com.lgow.endofherobrine.Main.MOD_ID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.*;

public class ModRegistries {
	public static final DeferredRegister<Block> MOD_BLOCKS = create(BLOCKS, MOD_ID);

	public static final DeferredRegister<Enchantment> MOD_ENCHANTMENTS = create(ENCHANTMENTS, MOD_ID);

	public static final DeferredRegister<EntityType<?>> MOD_ENTITIES = create(ENTITY_TYPES, MOD_ID);

	public static final DeferredRegister<Item> MOD_ITEMS = create(ITEMS, MOD_ID);

	public static final DeferredRegister<BlockEntityType<?>> MOD_TILE_ENTITIES = create(BLOCK_ENTITY_TYPES, MOD_ID);

	public static final DeferredRegister<PaintingVariant> MOD_PAINTINGS = create(PAINTING_VARIANTS, MOD_ID);

	public static void register() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		MOD_BLOCKS.register(modEventBus);
		MOD_ENCHANTMENTS.register(modEventBus);
		MOD_ENTITIES.register(modEventBus);
		MOD_ITEMS.register(modEventBus);
		MOD_TILE_ENTITIES.register(modEventBus);
		MOD_PAINTINGS.register(modEventBus);
		BlockInit.register();
		EnchantmentInit.register();
		EntityInit.register();
		BlockEntityInit.register();
		ItemInit.register();
		PaintingInit.register();
		ModTab.register();
		SpawnEvents.register();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT_SPEC,
				"endofherobrine-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC,
				"endofherobrine-common.toml");
	}
}
