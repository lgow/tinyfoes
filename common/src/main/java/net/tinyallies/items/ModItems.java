package net.tinyallies.items;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.tinyallies.TinyAlliesCommon;
import net.tinyallies.registry.ModRegistries;
import net.tinyallies.util.TinyAlliesResLoc;

import java.util.function.Supplier;

public class ModItems {
	public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(TinyAlliesCommon.MODID,
			Registry.ITEM_REGISTRY);

	public static final Registrar<Item> ITEM_REGISTRAR = MOD_ITEMS.getRegistrar();


	private static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> item) {
		final ResourceLocation id = new TinyAlliesResLoc(path);
		return ITEM_REGISTRAR.register(id, item);
	}

	public static final RegistrySupplier<Item> BABYFIER = registerItem("babyfier",
			() -> new BabyfierItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

	public static void register() {
		MOD_ITEMS.register();
	}
}
