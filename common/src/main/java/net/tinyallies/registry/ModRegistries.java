package net.tinyallies.registry;

import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.tinyallies.TinyAlliesCommon;
import net.tinyallies.entity.ModEntities;
import net.tinyallies.event.BabyConversionHandler;
import net.tinyallies.items.ModItems;

public class ModRegistries {
	public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(TinyAlliesCommon.MODID,
			Registries.ITEM);

	public static void register() {
		MOD_ITEMS.register();
		ModEntities.register();
		ModItems.register();
		EntityEvent.ADD.register(new BabyConversionHandler());
	}
}
