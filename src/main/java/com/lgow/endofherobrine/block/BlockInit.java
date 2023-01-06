package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.registries.ModRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {

    private static<T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier){
        return ModRegistries.MOD_BLOCKS.register(name, blockSupplier);
    }

    private static<T extends Block> RegistryObject<T> registerBlockItem(String name, Supplier<T> blockSupplier){
        RegistryObject<T> block = registerBlock(name, blockSupplier);
        ModRegistries.MOD_ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
        return block;
    }

    public static final RegistryObject<Block> MOSSY_COBBLESTONE = registerBlockItem("mossy_cobblestone",
            () -> new PosInfestedBlocks(Blocks.MOSSY_COBBLESTONE, BlockBehaviour.Properties.of(Material.CLAY)));

    public static final RegistryObject<Block> MOSSY_STONE_BRICKS = registerBlockItem("mossy_stone_bricks",
            () -> new PosInfestedBlocks(Blocks.MOSSY_STONE_BRICKS, BlockBehaviour.Properties.of(Material.GLASS)));

    public static final RegistryObject<Block> GLOWSTONE = registerBlockItem("glowstone",
            () -> new PosInfestedBlocks(Blocks.GLOWSTONE, BlockBehaviour.Properties.of(Material.CLAY).lightLevel((blockState) -> 15)));

    public static final RegistryObject<Block> GLOWING_OBSIDIAN = registerBlockItem("glowing_obsidian",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_RED).sound(SoundType.STONE)
                    .requiresCorrectToolForDrops().strength(20.0F, 450.0F).lightLevel((blockState) -> 12)));

    public static final RegistryObject<Block> NETHERRACK_TOTEM = registerBlockItem("netherrack_totem", () -> new TotemBlocks(Blocks.NETHERRACK));

    public static final RegistryObject<Block> BLACKSTONE_TOTEM = registerBlockItem("blackstone_totem", () -> new TotemBlocks(Blocks.BLACKSTONE));

    public static final RegistryObject<Block> CURSED_SKULL = registerBlock("cursed_head",
            () -> new ModSkullBlock(Types.CURSED, BlockBehaviour.Properties.copy(Blocks.PLAYER_HEAD)));

    public static final RegistryObject<Block> CURSED_WALL_SKULL = registerBlock("cursed_wall_head",
            () -> new SkullWallBlocks(Types.CURSED,  BlockBehaviour.Properties.copy(Blocks.PLAYER_HEAD).lootFrom(CURSED_SKULL)));

    public static final RegistryObject<Block> HEROBRINE_SKULL = registerBlock("herobrine_head",
            () -> new ModSkullBlock(Types.HEROBRINE, BlockBehaviour.Properties.copy(Blocks.PLAYER_HEAD)));

    public static final RegistryObject<Block> HEROBRINE_WALL_SKULL = registerBlock("herobrine_wall_head",
            () -> new SkullWallBlocks(Types.HEROBRINE, BlockBehaviour.Properties.copy(Blocks.PLAYER_HEAD).lootFrom(HEROBRINE_SKULL)));

    public static void registerBlockItem() { }

    public enum Types implements SkullBlock.Type {
        HEROBRINE,
        CURSED
    }
}