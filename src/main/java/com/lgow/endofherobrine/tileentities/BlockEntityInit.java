package com.lgow.endofherobrine.tileentities;

import com.lgow.endofherobrine.block.BlockInit;
import com.lgow.endofherobrine.registries.ModRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

import static com.lgow.endofherobrine.block.BlockInit.*;

public class BlockEntityInit {
	public static final RegistryObject<BlockEntityType<ModSkullBlockEntity>> SKULL = ModRegistries.MOD_TILE_ENTITIES.register(
			"skull",
			() -> BlockEntityType.Builder.of(ModSkullBlockEntity::new, BlockInit.CURSED_SKULL.get(), CURSED_WALL_SKULL.get(),
					HEROBRINE_SKULL.get(), HEROBRINE_WALL_SKULL.get()).build(null));

	public static void register() { }
}
