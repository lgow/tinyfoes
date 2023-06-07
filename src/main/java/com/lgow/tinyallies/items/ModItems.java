package com.lgow.tinyallies.items;

import com.lgow.tinyallies.registry.ModRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
	public static final RegistryObject<Item> BABYFIER;

	static {
		BABYFIER = registerItem("babyfier");
	}

	private static RegistryObject<Item> registerItem(String name) {
		return ModRegistries.MOD_ITEMS.register(name, () -> new BabyfierItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	}

	public static void register() {
	}
}