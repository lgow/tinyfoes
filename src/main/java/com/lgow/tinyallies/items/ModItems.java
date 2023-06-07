package com.lgow.tinyallies.items;

import com.lgow.tinyallies.Main;
import com.lgow.tinyallies.registry.ModRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
	public static final RegistryObject<Item> BABYFIER = registerItem("babyfier");

	private static RegistryObject<Item> registerItem(String name) {
		return ModRegistries.MOD_ITEMS.register(name, () -> new BabyfierItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	}

	@SubscribeEvent
	public static void addBabyfierToTab(CreativeModeTabEvent.BuildContents event) {
		if (event.getTab() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(ModItems.BABYFIER.get());
		}
	}

	public static void register() {
	}
}