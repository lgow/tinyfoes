package net.tinyallies.common.registry;

import net.tinyallies.common.entity.ModEntities;
import net.tinyallies.common.items.ModItems;

public class ModRegistries {

	public static void register() {
		ModEffects.register();
		ModEntities.register();
		ModItems.register();
	}

}
