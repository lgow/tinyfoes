package com.lgow.endofherobrine.entity;

import com.lgow.endofherobrine.entity.herobrine.Builder;
import com.lgow.endofherobrine.entity.herobrine.Lurker;
import com.lgow.endofherobrine.entity.herobrine.boss.HerobrineBoss;
import com.lgow.endofherobrine.entity.possessed.*;
import com.lgow.endofherobrine.entity.possessed.animal.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;

import static com.lgow.endofherobrine.registries.ModRegistries.MOD_ENTITIES;
import static net.minecraft.world.entity.MobCategory.CREATURE;
import static net.minecraft.world.entity.MobCategory.MONSTER;

public class EntityInit {
	public static final MobCategory HEROBRINE = MobCategory.create("herobrine", "herobrine", 1, false, false, 256);

	public static final RegistryObject<EntityType<HerobrineBoss>> HEROBRINE_BOSS = registerHerobrine("herobrine",
			HerobrineBoss::new);

	public static final RegistryObject<EntityType<Builder>> BUILDER = registerHerobrine("builder", Builder::new);

	public static final RegistryObject<EntityType<Lurker>> LURKER = registerHerobrine("lurker", Lurker::new);

	public static final RegistryObject<EntityType<PosChicken>> CHICKEN = registerPossessed("chicken",
			PosChicken::new, CREATURE, 0.4F, 0.7F);

	public static final RegistryObject<EntityType<PosCow>> COW = registerPossessed("cow", PosCow::new, CREATURE,
			0.9F, 1.4F);

	public static final RegistryObject<EntityType<PosHusk>> HUSK = registerPossessed("husk", PosHusk::new, MONSTER,
			0.6F, 1.95F);

	public static final RegistryObject<EntityType<PosPig>> PIG = registerPossessed("pig", PosPig::new, CREATURE,
			0.9F, 0.9F);

	public static final RegistryObject<EntityType<PosPigman>> PIGMAN = registerPossessed("pigman", PosPigman::new,
			MONSTER, 0.6F, 1.95F);

	public static final RegistryObject<EntityType<PosRabbit>> RABBIT = registerPossessed("rabbit", PosRabbit::new,
			CREATURE, 0.4F, 0.5F);

	public static final RegistryObject<EntityType<PosSheep>> SHEEP = registerPossessed("sheep", PosSheep::new,
			CREATURE, 0.9F, 1.3F);

	public static final RegistryObject<EntityType<PosSilverfish>> SILVERFISH = registerPossessed("silverfish",
			PosSilverfish::new, MONSTER, 0.4F, 0.3F);

	public static final RegistryObject<EntityType<PosSkeleton>> SKELETON = registerPossessed("skeleton",
			PosSkeleton::new, MONSTER, 0.6F, 1.99F);

	public static final RegistryObject<EntityType<PosStray>> STRAY = registerPossessed("stray", PosStray::new,
			MONSTER, 0.6F, 1.99F);

	public static final RegistryObject<EntityType<PosVillager>> VILLAGER = registerPossessed("villager",
			PosVillager::new, CREATURE, 0.6F, 1.95F);

	public static final RegistryObject<EntityType<PosZombie>> ZOMBIE = registerPossessed("zombie", PosZombie::new,
			MONSTER, 0.6F, 1.95F);

	public static final RegistryObject<EntityType<PosZombieVillager>> ZOMBIE_VILLAGER = registerPossessed(
			"zombie_villager", PosZombieVillager::new, MONSTER, 0.6F, 1.95F);

	public static <T extends Entity> RegistryObject<EntityType<T>> registerPossessed(String name, EntityType.EntityFactory<T> pFactory, MobCategory pCategory, float width, float height) {
		return MOD_ENTITIES.register(name, () -> EntityType.Builder.of(pFactory, pCategory).sized(width, height)
				.build(name));
	}

	public static <T extends Entity> RegistryObject<EntityType<T>> registerHerobrine(String name, EntityType.EntityFactory<T> pFactory) {
		return MOD_ENTITIES.register(name, () -> EntityType.Builder.of(pFactory, HEROBRINE).sized(0.6F, 1.95F)
				.build(name));
	}

	public static void register() { }
}
