package com.lgow.endofherobrine.block;

import com.lgow.endofherobrine.registries.ModRegistries;
import com.lgow.endofherobrine.util.ModResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.lgow.endofherobrine.registries.ModRegistries.MOD_ITEMS;

public class BlockInit {
	public static final RegistryObject<Block> CURSED_SKULL, HEROBRINE_SKULL, CURSED_WALL_SKULL, HEROBRINE_WALL_SKULL, ARMOR_STAND_BASE;

	public static final RegistryObject<BlockItem> GLOWING_OBSIDIAN, NETHERRACK_TOTEM, BLACKSTONE_TOTEM, PATIMUSS_DOOR;

	public static final TagKey<Block> NO_OVERRIDE = BlockTags.create(
			new ModResourceLocation("prevent_builder_override"));

	static {
		//SkullBlocks
		CURSED_SKULL = registerSkullBlock("cursed_head", ModSkullBlock.Types.CURSED);
		HEROBRINE_SKULL = registerSkullBlock("herobrine_head", ModSkullBlock.Types.HEROBRINE);
		CURSED_WALL_SKULL = registerWallSkullBlock("cursed_wall_head", ModSkullBlock.Types.CURSED);
		HEROBRINE_WALL_SKULL = registerWallSkullBlock("herobrine_wall_head", ModSkullBlock.Types.HEROBRINE);
		//BlockItems
		GLOWING_OBSIDIAN = registerBlockItem("glowing_obsidian", GlowingObsidianBlock::new);
		NETHERRACK_TOTEM = registerBlockItem("netherrack_totem", () -> new TotemBlock(Blocks.NETHERRACK));
		BLACKSTONE_TOTEM = registerBlockItem("blackstone_totem", () -> new TotemBlock(Blocks.BLACKSTONE));
		PATIMUSS_DOOR = registerBlockItem("patimuss_door", ()-> new DoorBlock(BlockBehaviour.Properties.of().mapColor(
				DyeColor.CYAN).noOcclusion().pushReaction(PushReaction.DESTROY), BlockSetType.OAK));
		//InfestedBlocks
		registerInfestedBlock("cracked_stone_bricks", Blocks.CRACKED_STONE_BRICKS);
		registerInfestedBlock("mossy_cobblestone", Blocks.MOSSY_COBBLESTONE);
		registerInfestedBlock("mossy_stone_bricks", Blocks.MOSSY_STONE_BRICKS);
		registerBlock("glowstone", () -> new ModInfestedBlock(Blocks.GLOWSTONE, BlockBehaviour.Properties.copy(Blocks.PLAYER_HEAD).lightLevel((blockState) -> 15).sound(SoundType.GLASS)));
		ARMOR_STAND_BASE = registerBlock("armor_stand_base", ()-> new ArmorStandBaseBlock(BlockBehaviour.Properties.of().noCollission()));
	}

	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier) {
		return ModRegistries.MOD_BLOCKS.register(name, blockSupplier);
	}

	private static RegistryObject<Block> registerSkullBlock(String name, ModSkullBlock.Types type) {
		return registerBlock(name, () -> new ModSkullBlock(type, BlockBehaviour.Properties.copy(Blocks.PLAYER_HEAD)));
	}

	private static RegistryObject<Block> registerWallSkullBlock(String name, ModSkullBlock.Types type) {
		return registerBlock(name,
				() -> new ModWallSkullBlock(type, BlockBehaviour.Properties.copy(Blocks.PLAYER_HEAD)));
	}

	private static void registerInfestedBlock(String name, Block blockIn) {
		registerBlock(name, () -> new ModInfestedBlock(blockIn, BlockBehaviour.Properties.copy(Blocks.INFESTED_STONE)));
	}

	private static <T extends Block> RegistryObject<BlockItem> registerBlockItem(String name, Supplier<T> blockSupplier) {
		RegistryObject<T> block = registerBlock(name, blockSupplier);
		return MOD_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	public static void register() { }
}