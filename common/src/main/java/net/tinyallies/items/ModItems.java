package net.tinyallies.items;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.tinyallies.registry.ModRegistries;
import net.tinyallies.util.TinyAlliesResLoc;

import java.util.function.Supplier;

public class ModItems {
	public static final Registrar<Item> ITEM_REGISTRAR = ModRegistries.MOD_ITEMS.getRegistrar();

	public static final RegistrySupplier<Item> BABYFIER = registerItem("babyfier",
			() -> new BabyfierItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

	private static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> item) {
		final ResourceLocation id = new TinyAlliesResLoc(path);
		return ITEM_REGISTRAR.register(id, item);
	}

	public static void register() { }
}
