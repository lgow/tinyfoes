package net.tinyfoes.common.items;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potion;
import net.tinyfoes.common.TinyFoesCommon;
import net.tinyfoes.common.registry.ModEffects;
import net.tinyfoes.common.util.TinyFoesResLoc;

public class ModItems {
	public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(TinyFoesCommon.MODID,
			Registry.ITEM_REGISTRY);
	public static final Registrar<Item> ITEM_REGISTRAR = MOD_ITEMS.getRegistrar();
	//
	public static final RegistrySupplier<Item> BABYFIER = ITEM_REGISTRAR.register(new TinyFoesResLoc("babyfier"),
			() -> new BabyfierItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistrySupplier<Item> TINY_TAB = ITEM_REGISTRAR.register(new TinyFoesResLoc("tiny_tab"),
			() -> new Item(new Item.Properties().stacksTo(1)));

	public static void register() {
		MOD_ITEMS.register();
	}
}
