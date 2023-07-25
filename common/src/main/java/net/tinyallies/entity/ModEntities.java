package net.tinyallies.entity;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.*;
import net.tinyallies.TinyAlliesCommon;
import net.tinyallies.entity.projectile.BabyfierBlob;
import net.tinyallies.util.TinyAlliesResLoc;

import java.util.function.Supplier;

import static net.minecraft.world.entity.MobCategory.MONSTER;

public class ModEntities {
	private static final Registrar<EntityType<?>> ENTITY_TYPES = TinyAlliesCommon.REGISTRIES.get(
			Registries.ENTITY_TYPE);
	public static final RegistrySupplier<EntityType<Creepy>> CREEPY = registerBaby("creeper", Creepy::new, MONSTER,
			0.33F, 0.85F);
	public static final RegistrySupplier<EntityType<Skelly>> SKELLY = registerBaby("skeleton", Skelly::new, MONSTER,
			0.33F, 1.05F);
	public static final RegistrySupplier<EntityType<EnderBoy>> ENDERBOY = registerBaby("enderman", EnderBoy::new,
			MONSTER, 0.33F, 1.4F);
	public static final RegistrySupplier<EntityType<Spidey>> SPIDEY = registerBaby("spider", Spidey::new, MONSTER, 0.9F,
			0.45F);
	public static final RegistrySupplier<EntityType<Zomby>> ZOMBY = registerBaby("zombie", Zomby::new, MONSTER, 0.33F,
			1.05F);
	public static final RegistrySupplier<EntityType<BabyfierBlob>> BLOB = create("blob",
			() -> EntityType.Builder.<BabyfierBlob> of(BabyfierBlob::new, MobCategory.MISC).clientTrackingRange(4)
					.updateInterval(20).sized(0.1F, 0.1F).build("blob"));

	public static <T extends Entity> RegistrySupplier<EntityType<T>> registerBaby(String name, EntityType.EntityFactory<T> pFactory, MobCategory pCategory, float width, float height) {
		return create(name, () -> EntityType.Builder.of(pFactory, pCategory).sized(width, height).build(name));
	}

	public static <T extends EntityType<?>> RegistrySupplier<T> create(final String path, final Supplier<T> type) {
		return ENTITY_TYPES.register(new TinyAlliesResLoc(path), type);
	}

	public static void register() {
		EntityAttributeRegistry.register(CREEPY, Creeper::createAttributes);
		EntityAttributeRegistry.register(SKELLY, Skeleton::createAttributes);
		EntityAttributeRegistry.register(ENDERBOY, EnderMan::createAttributes);
		EntityAttributeRegistry.register(SPIDEY, Spider::createAttributes);
		EntityAttributeRegistry.register(ZOMBY, Zombie::createAttributes);
	}
}
