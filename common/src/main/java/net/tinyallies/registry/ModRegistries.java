package net.tinyallies.registry;

import net.tinyallies.entity.ModEntities;
import net.tinyallies.items.ModItems;

public class ModRegistries {
	public static void register() {
		ModEntities.register();
		ModItems.register();
	}
}
