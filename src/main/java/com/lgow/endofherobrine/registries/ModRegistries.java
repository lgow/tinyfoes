package com.lgow.endofherobrine.registries;

import com.lgow.endofherobrine.block.BlockInit;
import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.enchantment.EnchantmentInit;
import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.event.ModSoundEvents;
import com.lgow.endofherobrine.item.ItemInit;
import com.lgow.endofherobrine.item.ModTab;
import com.lgow.endofherobrine.tileentities.ModPaintingInit;
import com.lgow.endofherobrine.tileentities.TileEntityInit;
import com.lgow.endofherobrine.world.spawner.SpawnHandler;
import net.minecraft.sounds.SoundEvent;
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

    public static final DeferredRegister<SoundEvent> MOD_SOUND_EVENTS = create(SOUND_EVENTS, MOD_ID);
    public static final DeferredRegister<Block> MOD_BLOCKS = create(BLOCKS, MOD_ID);
    public static final DeferredRegister<Enchantment> MOD_ENCHANTMENTS = create(ENCHANTMENTS, MOD_ID);
    public static final DeferredRegister<EntityType<?>> MOD_ENTITIES = create(ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<Item> MOD_ITEMS = create(ITEMS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> MOD_TILE_ENTITIES = create(BLOCK_ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<PaintingVariant> MOD_PAINTINGS = create(PAINTING_VARIANTS, MOD_ID);

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_SOUND_EVENTS.register(modEventBus);
        MOD_BLOCKS.register(modEventBus);
        MOD_ENCHANTMENTS.register(modEventBus);
        MOD_ENTITIES.register(modEventBus);
        MOD_ITEMS.register(modEventBus);
        MOD_TILE_ENTITIES.register(modEventBus);
        MOD_PAINTINGS.register(modEventBus);

        ModSoundEvents.register();
        BlockInit.registerBlockItem();
        EnchantmentInit.register();
        EntityInit.register();
        TileEntityInit.register();
        ItemInit.register();
        ModPaintingInit.register();
        ModTab.register();
        SpawnHandler.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT_SPEC,"endofherobrine-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC,"endofherobrine-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModConfigs.SERVER_SPEC,"endofherobrine-server.toml");
    }
}
