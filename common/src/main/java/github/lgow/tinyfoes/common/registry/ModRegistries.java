package github.lgow.tinyfoes.common.registry;

import github.lgow.tinyfoes.common.entity.ModEntities;
import github.lgow.tinyfoes.common.items.ModItems;

public class ModRegistries {
	public static void register() {
		ModEffects.register();
		ModEntities.register();
		ModItems.register();
	}
}
