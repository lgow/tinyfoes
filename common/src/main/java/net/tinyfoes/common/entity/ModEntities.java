package net.tinyfoes.common.entity;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.tinyfoes.common.CommonTinyFoes;
import net.tinyfoes.common.entity.projectile.BabificationRay;
import net.tinyfoes.common.util.TinyFoesResLoc;

import java.util.function.Supplier;

public class ModEntities {
	private static final Registrar<EntityType<?>> ENTITY_TYPES = CommonTinyFoes.REGISTRIES.get(Registries.ENTITY_TYPE);
	public static final RegistrySupplier<EntityType<BabificationRay>> BABYFICATION_RAY = create("babyfication_ray",
			() -> EntityType.Builder.<BabificationRay> of(BabificationRay::new, MobCategory.MISC).clientTrackingRange(4)
					.updateInterval(20).sized(0.5F, 0.5F).build("babyfication_ray"));

	public static <T extends EntityType<?>> RegistrySupplier<T> create(final String path, final Supplier<T> type) {
		return ENTITY_TYPES.register(new TinyFoesResLoc(path), type);
	}

	public static void register() {
	}
}
