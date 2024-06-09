package net.tinyapi.common.items;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.tinyapi.common.CommonTinyFoes;
import net.tinyapi.common.util.TinyFoesResLoc;

public class ModItems {
	public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(CommonTinyFoes.MODID,
			Registry.ITEM_REGISTRY);
	public static final Registrar<Item> ITEM_REGISTRAR = MOD_ITEMS.getRegistrar();
	//
	public static final RegistrySupplier<Item> THE_BABYFIER = ITEM_REGISTRAR.register(new TinyFoesResLoc("babyfier"),
			() -> new BabyfierItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistrySupplier<Item> PACIFIER = ITEM_REGISTRAR.register(new TinyFoesResLoc("pacifier"),
			() -> new Item(new Item.Properties().stacksTo(1)));

	public static void register() {
		MOD_ITEMS.register();
	}
}
