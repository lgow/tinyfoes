package net.tinyapi.common.registry;

import net.tinyapi.common.entity.ModEntities;
import net.tinyapi.common.items.ModItems;

public class ModRegistries {
	public static void register() {
		ModEffects.register();
		ModEntities.register();
		ModItems.register();
	}
}
