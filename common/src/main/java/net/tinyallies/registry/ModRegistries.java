package net.tinyallies.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.tinyallies.TinyAlliesCommon;
import net.tinyallies.entity.ModEntities;
import net.tinyallies.items.ModItems;

public class ModRegistries {
	public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(TinyAlliesCommon.MODID,
			Registry.ITEM_REGISTRY);



	public static void register() {
		MOD_ITEMS.register();
		ModEntities.register();
		ModItems.register();
	}
}
