package com.lgow.endofherobrine.util;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import com.lgow.endofherobrine.entity.possessed.animal.PosVillager;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lgow.endofherobrine.entity.EntityInit.*;
import static net.minecraft.world.entity.EntityType.*;
import static net.minecraft.world.entity.EntityType.CHICKEN;
import static net.minecraft.world.entity.EntityType.COW;
import static net.minecraft.world.entity.EntityType.HUSK;
import static net.minecraft.world.entity.EntityType.PIG;
import static net.minecraft.world.entity.EntityType.RABBIT;
import static net.minecraft.world.entity.EntityType.SHEEP;
import static net.minecraft.world.entity.EntityType.SILVERFISH;
import static net.minecraft.world.entity.EntityType.SKELETON;
import static net.minecraft.world.entity.EntityType.STRAY;
import static net.minecraft.world.entity.EntityType.VILLAGER;
import static net.minecraft.world.entity.EntityType.ZOMBIE;
import static net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER;

public class ModUtil {
	private static final Map<EntityType<? extends Mob>, EntityType<? extends Mob>> animalList = Map.of(
			EntityType.CHICKEN, EntityInit.CHICKEN.get(), EntityType.COW, EntityInit.COW.get(), EntityType.PIG,
			EntityInit.PIG.get(), EntityType.RABBIT, EntityInit.RABBIT.get(), EntityType.SHEEP, EntityInit.SHEEP.get(),
			EntityType.VILLAGER, EntityInit.VILLAGER.get());

	private static final Map<EntityType<? extends Mob>, EntityType<? extends Mob>> monsterList = Map.of(EntityType.HUSK,
			EntityInit.HUSK.get(), EntityType.SILVERFISH, EntityInit.SILVERFISH.get(), EntityType.SKELETON,
			EntityInit.SKELETON.get(), EntityType.STRAY, EntityInit.STRAY.get(), EntityType.ZOMBIE,
			EntityInit.ZOMBIE.get(), EntityType.ZOMBIE_VILLAGER, EntityInit.ZOMBIE_VILLAGER.get());

	//Spawns herobrine relative to a position and direction
	public static void spawnHerobrine(AbstractHerobrine herobrine, ServerLevel server, Direction dir, Vec3 pos, double offs) {
		switch (dir) {
			case NORTH -> herobrine.setPos(pos.subtract(0, 0, offs));
			case SOUTH -> herobrine.setPos(pos.add(0, 0, offs));
			case EAST -> herobrine.setPos(pos.add(offs, 0, 0));
			case WEST -> herobrine.setPos(pos.subtract(offs, 0, 0));
		}
		server.addFreshEntity(herobrine);
	}

	 public static void possessMobs(LivingEntity entityIn, ServerLevel level, boolean isAngry, boolean affectMonsters) {
		if (animalList.containsKey(entityIn.getType())) {
			LivingEntity posMob = ((Mob) entityIn).convertTo(animalList.get(entityIn.getType()), true);
			posMob.setHealth(entityIn.getHealth());
			if (posMob instanceof Pig pig && ((Pig) entityIn).isSaddled()) {
				pig.equipSaddle(null);
			}
			else if (posMob instanceof Rabbit rabbit) {
				rabbit.setVariant(((Rabbit) entityIn).getVariant());
			}
			else if (posMob instanceof Sheep sheep) {
				sheep.setColor(((Sheep) entityIn).getColor());
				sheep.setSheared(((Sheep) entityIn).isSheared());
			}
			else if (entityIn instanceof Villager villager) {
				((PosVillager) posMob).setVillagerData(villager.getVillagerData());
			}
			if (isAngry) {
				level.playSound(null, entityIn.blockPosition(), SoundEvents.GHAST_HURT, SoundSource.HOSTILE, 1.0F,
						1.0F);
				entityIn.setLastHurtByMob(entityIn.getLastHurtByMob());
			}
		}
		if (affectMonsters && monsterList.containsKey(entityIn.getType())) {
			LivingEntity posMob = ((Mob) entityIn).convertTo(monsterList.get(entityIn.getType()), true);
			if (posMob instanceof ZombieVillager zVillager) {
				zVillager.setVillagerData(((ZombieVillager) entityIn).getVillagerData());
			}else if (posMob instanceof Husk){
				((Husk) posMob).setNoAi(false);
			}
		}
	}

	public static boolean noHerobrineExists(Level level) {
		List<AbstractHerobrine> list = new ArrayList<>();
		for (ServerPlayer serverplayer : level.getServer().getPlayerList().getPlayers()) {
			list.addAll(level.getEntitiesOfClass(AbstractHerobrine.class, serverplayer.getBoundingBox().inflate(256)));
		}
		return list.isEmpty();
	}
}
