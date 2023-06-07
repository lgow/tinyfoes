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
			MONSTER,0.33F, 0.85F);

	public static final RegistryObject<EntityType<BabySkeleton>> SKELLY = registerBaby("skeleton", BabySkeleton::new,
			MONSTER, 0.33F, 1.05F);

	public static final RegistryObject<EntityType<BabyEnderman>> ENDERBOY = registerBaby("enderman", BabyEnderman::new,
			MONSTER, 0.33F, 1.4F);

	public static final RegistryObject<EntityType<BabySpider>> SPIDEY = registerBaby("spider", BabySpider::new, MONSTER,
			0.9F, 0.45F);

	public static final RegistryObject<EntityType<BabyZombie>> ZOMBY = registerBaby("zombie", BabyZombie::new, MONSTER,
			0.33F, 1.05F);

	public static final RegistryObject<EntityType<BabyfierBlob>> BLOB = ModRegistries.MOD_ENTITIES.register("blob",
			() -> EntityType.Builder.<BabyfierBlob>of(BabyfierBlob::new, MobCategory.MISC).clientTrackingRange(4)
					.updateInterval(20).sized(0.1F, 0.1F).build("blob"));
	public static <T extends Entity> RegistryObject<EntityType<T>> registerBaby(String name, EntityType.EntityFactory<T> pFactory, MobCategory pCategory, float width, float height) {
		return ModRegistries.MOD_ENTITIES.register(name, () -> EntityType.Builder.of(pFactory, pCategory).sized(width, height).build(name));
	}

	public static void register() { }
}
