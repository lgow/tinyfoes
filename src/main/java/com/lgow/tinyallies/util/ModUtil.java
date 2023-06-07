package com.lgow.tinyallies.util;

import com.lgow.tinyallies.entity.BabyCreeper;
import com.lgow.tinyallies.entity.BabyEnderman;
import com.lgow.tinyallies.entity.EntityInit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;

import java.util.HashMap;

import static net.minecraft.world.entity.EntityType.*;

public class ModUtil {
	private static final HashMap<EntityType, EntityType> animalList = new HashMap<>() {
		{
			put(CREEPER, EntityInit.CREEPY.get());
			put(SKELETON, EntityInit.SKELLY.get());
			put(ENDERMAN, EntityInit.ENDERBOY.get());
			put(SPIDER, EntityInit.SPIDEY.get());
			put(ZOMBIE, EntityInit.ZOMBY.get());
		}
	};

	public static void babifyMob(Mob entityIn) {
		if (animalList.containsKey(entityIn.getType())) {
			Mob baby = entityIn.convertTo(animalList.get(entityIn.getType()), true);
			baby.setHealth(entityIn.getHealth());
			if (baby instanceof BabyCreeper creeper) {
				creeper.setPowered(((Creeper)entityIn).isPowered());
				creeper.setSwellDir(((Creeper)entityIn).getSwellDir());
			} else if (baby instanceof BabyEnderman enderman){
				enderman.setCarriedBlock(((EnderMan)entityIn).getCarriedBlock());
				enderman.setTarget(entityIn.getTarget());

			}
		}else {
			entityIn.setBaby(true);
		}
	}
}
