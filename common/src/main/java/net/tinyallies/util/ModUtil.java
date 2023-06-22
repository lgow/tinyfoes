package net.tinyallies.util;

import net.tinyallies.entity.BabyCreeper;
import net.tinyallies.entity.BabyEnderman;
import net.tinyallies.entity.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;

import java.util.HashMap;

import static net.minecraft.world.entity.EntityType.*;

public class ModUtil {
	private static final HashMap<EntityType, EntityType> animalList = new HashMap<>() {
		{
			put(CREEPER, ModEntities.CREEPY.get());
			put(SKELETON, ModEntities.SKELLY.get());
			put(ENDERMAN, ModEntities.ENDERBOY.get());
			put(SPIDER, ModEntities.SPIDEY.get());
			put(ZOMBIE, ModEntities.ZOMBY.get());
		}
	};

	public static void babifyMob(Mob entityIn) {
		if (animalList.containsKey(entityIn.getType())) {
			Mob baby = entityIn.convertTo(animalList.get(entityIn.getType()), true);
			baby.setHealth(entityIn.getHealth());
			if (baby instanceof BabyCreeper creeper) {
//				creeper.setPowered(((Creeper) entityIn).isPowered());
				creeper.setSwellDir(((Creeper) entityIn).getSwellDir());
			}
			else if (baby instanceof BabyEnderman enderman) {
				enderman.setCarriedBlock(((EnderMan) entityIn).getCarriedBlock());
				enderman.setTarget(entityIn.getTarget());
			}
			entityIn.setBaby(true);
		}
		else {
			entityIn.setBaby(true);
		}
	}
}
