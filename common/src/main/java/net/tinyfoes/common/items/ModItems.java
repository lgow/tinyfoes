package net.tinyfoes.common.items;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.tinyfoes.common.CommonTinyFoes;
import net.tinyfoes.common.util.ModUtil;
import net.tinyfoes.common.util.TinyFoesResLoc;

public class ModItems {
	public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(CommonTinyFoes.MODID,
			Registries.ITEM);
	public static final DeferredRegister<CreativeModeTab> MOD_TABS = DeferredRegister.create(CommonTinyFoes.MODID,
			Registries.CREATIVE_MODE_TAB);
	public static final RegistrySupplier<CreativeModeTab> TINY_TAB = MOD_TABS.register("tiny_tab",
			() -> CreativeTabRegistry.create(Component.translatable("itemGroup.tinyfoes.tiny_tab"),
					() -> ModUtil.TINY_TAB_ICON));
	public static final Registrar<Item> ITEM_REGISTRAR = MOD_ITEMS.getRegistrar();
	//
	public static final RegistrySupplier<Item> BABYFIER = ITEM_REGISTRAR.register(new TinyFoesResLoc("babyfier"),
			() -> new BabyfierItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistrySupplier<Item> TINY_ICON = ITEM_REGISTRAR.register(new TinyFoesResLoc("tiny_tab"),
			() -> new Item(new Item.Properties().stacksTo(1)));

	public static void register() {
		MOD_ITEMS.register();
		MOD_TABS.register();
	}
}
