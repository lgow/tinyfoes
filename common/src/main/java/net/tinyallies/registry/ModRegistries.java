package net.tinyallies.registry;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.tinyallies.TinyAlliesCommon;
import net.tinyallies.entity.ModEntities;
import net.tinyallies.items.ModItems;

public class ModRegistries {




	public static void register() {
		ModEffects.register();
		ModEntities.register();
		ModItems.register();
	}
}
