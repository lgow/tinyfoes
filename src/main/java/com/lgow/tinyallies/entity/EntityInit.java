package com.lgow.tinyallies.entity;

import com.lgow.tinyallies.entity.projectile.BabyfierBlob;
import com.lgow.tinyallies.registry.ModRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.entity.MobCategory.MONSTER;

public class EntityInit {
	public static final RegistryObject<EntityType<BabyCreeper>> CREEPY = registerBaby("creeper", BabyCreeper::new,
			MONSTER);

	public static final RegistryObject<EntityType<BabySkeleton>> SKELLY = registerBaby("skeleton", BabySkeleton::new,
			MONSTER);

	public static final RegistryObject<EntityType<BabyEnderman>> ENDERBOY = registerBaby("enderman", BabyEnderman::new,
			MONSTER);

	public static final RegistryObject<EntityType<BabySpider>> SPIDEY = registerBaby("spider", BabySpider::new, MONSTER);

	public static final RegistryObject<EntityType<BabyZombie>> ZOMBY = registerBaby("zombie", BabyZombie::new, MONSTER);

	public static final RegistryObject<EntityType<BabyfierBlob>> BLOB = ModRegistries.MOD_ENTITIES.register("blob",
			() -> {
				return EntityType.Builder.<BabyfierBlob>of(BabyfierBlob::new, MobCategory.MISC).clientTrackingRange(4)
						.updateInterval(20).build("blob");
			});
	public static <T extends Entity> RegistryObject<EntityType<T>> registerBaby(String name, EntityType.EntityFactory<T> pFactory, MobCategory pCategory) {
		return ModRegistries.MOD_ENTITIES.register(name, () -> EntityType.Builder.of(pFactory, pCategory).build(name));
	}

	public static void register() { }
}
