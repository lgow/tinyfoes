package net.tinyallies.common.entity;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.tinyallies.common.TinyFoesCommon;
import net.tinyallies.common.entity.projectile.BabyfierBlob;
import net.tinyallies.common.util.TinyFoesResLoc;

import java.util.function.Supplier;

public class ModEntities {
	private static final Registrar<EntityType<?>> ENTITY_TYPES = TinyFoesCommon.REGISTRIES.get(Registry.ENTITY_TYPE);

	public static final RegistrySupplier<EntityType<BabyfierBlob>> BLOB = create("blob",
			() -> EntityType.Builder.<BabyfierBlob> of(BabyfierBlob::new, MobCategory.MISC).clientTrackingRange(4)
					.updateInterval(20).sized(0.1F, 0.1F).build("blob"));

	public static <T extends EntityType<?>> RegistrySupplier<T> create(final String path, final Supplier<T> type) {
		return ENTITY_TYPES.register(new TinyFoesResLoc(path), type);
	}

	public static void register() {
	}
}
