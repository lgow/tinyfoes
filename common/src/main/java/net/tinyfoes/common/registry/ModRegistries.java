package net.tinyfoes.common.registry;

import net.tinyfoes.common.entity.ModEntities;
import net.tinyfoes.common.items.ModItems;

public class ModRegistries {

	public static void register() {
		ModEffects.register();
		ModEntities.register();
		ModItems.register();
	}
}
