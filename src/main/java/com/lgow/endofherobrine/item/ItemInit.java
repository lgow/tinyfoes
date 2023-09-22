package com.lgow.endofherobrine.item;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.lgow.endofherobrine.block.BlockInit.*;
import static com.lgow.endofherobrine.entity.EntityInit.*;
import static com.lgow.endofherobrine.registries.ModRegistries.MOD_ITEMS;

public class ItemInit {
	public static final RegistryObject<ForgeSpawnEggItem> BUILDER_SPAWN_EGG, LURKER_SPAWN_EGG, CHICKEN_SPAWN_EGG, COW_SPAWN_EGG, HUSK_SPAWN_EGG, PIG_SPAWN_EGG, PIGMAN_SPAWN_EGG, RABBIT_SPAWN_EGG, SHEEP_SPAWN_EGG, SILVERFISH_SPAWN_EGG, SKELETON_SPAWN_EGG, STRAY_SPAWN_EGG, VILLAGER_SPAWN_EGG, ZOMBIE_SPAWN_EGG, ZOMBIE_SPAWN_VILLAGER_EGG;

	public static final RegistryObject<StandingAndWallBlockItem> CURSED_HEAD_ITEM, HEROBRINE_HEAD_ITEM;
	static {
		CURSED_HEAD_ITEM = MOD_ITEMS.register("cursed_head",
				() -> new StandingAndWallBlockItem(CURSED_SKULL.get(), CURSED_WALL_SKULL.get(),
						new Item.Properties().stacksTo(1), Direction.DOWN));
		HEROBRINE_HEAD_ITEM = MOD_ITEMS.register("herobrine_head",
				() -> new StandingAndWallBlockItem(HEROBRINE_SKULL.get(), HEROBRINE_WALL_SKULL.get(),
						new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant(), Direction.DOWN));
		//SPAWN EGGS
		BUILDER_SPAWN_EGG = registerSpawnEgg("builder", BUILDER, 44975);
		LURKER_SPAWN_EGG = registerSpawnEgg("lurker", LURKER, 44975);
//		DOPPELGANGER_SPAWN_EGG = registerSpawnEgg("doppelganger", DOPPLEGANGER, 44975);
		CHICKEN_SPAWN_EGG = registerSpawnEgg("chicken", P_CHICKEN, 10489616);
		COW_SPAWN_EGG = registerSpawnEgg("cow", P_COW, 4470310);
		HUSK_SPAWN_EGG = registerSpawnEgg("husk", P_HUSK, 12691306);
		PIG_SPAWN_EGG = registerSpawnEgg("pig", P_PIG, 15771042);
		PIGMAN_SPAWN_EGG = registerSpawnEgg("pigman", PIGMAN, 15373203);
		RABBIT_SPAWN_EGG = registerSpawnEgg("rabbit", P_RABBIT, 10051392);
		SHEEP_SPAWN_EGG = registerSpawnEgg("sheep", P_SHEEP, 15198183);
		SILVERFISH_SPAWN_EGG = registerSpawnEgg("silverfish", P_SILVERFISH, 7237230);
		SKELETON_SPAWN_EGG = registerSpawnEgg("skeleton", P_SKELETON, 12698049);
		STRAY_SPAWN_EGG = registerSpawnEgg("stray", P_STRAY, 14543594);
		VILLAGER_SPAWN_EGG = registerSpawnEgg("villager", P_VILlAGER, 12422002);
		ZOMBIE_SPAWN_EGG = registerSpawnEgg("zombie", P_ZOMBIE, 3232308);
		ZOMBIE_SPAWN_VILLAGER_EGG = registerSpawnEgg("zombie_villager", P_ZOMBIE_VILLAGER, 7969893);
	}

	private static RegistryObject<ForgeSpawnEggItem> registerSpawnEgg(String name, Supplier<? extends EntityType<? extends Mob>> entityType, int color) {
		return MOD_ITEMS.register(name + "_spawn_egg",
				() -> new ForgeSpawnEggItem(entityType, color, 16777215, new Item.Properties()));
	}

	public static void register() { }
}