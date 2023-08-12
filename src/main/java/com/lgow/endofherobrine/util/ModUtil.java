package com.lgow.endofherobrine.util;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import com.lgow.endofherobrine.entity.possessed.PosZombieVillager;
import com.lgow.endofherobrine.entity.possessed.animal.PosPig;
import com.lgow.endofherobrine.entity.possessed.animal.PosRabbit;
import com.lgow.endofherobrine.entity.possessed.animal.PosSheep;
import com.lgow.endofherobrine.entity.possessed.animal.PosVillager;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.minecraft.world.entity.EntityType.HUSK;
import static net.minecraft.world.entity.EntityType.RABBIT;
import static net.minecraft.world.entity.EntityType.SHEEP;
import static net.minecraft.world.entity.EntityType.VILLAGER;
import static net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER;

public class ModUtil {
	public static Random random = new Random();
	private static final Map<EntityType<? extends Mob>, EntityType<? extends Mob>> mobList = Map.of(
			EntityType.CHICKEN, EntityInit.CHICKEN.get(), EntityType.COW, EntityInit.COW.get(), EntityType.PIG,
			EntityInit.PIG.get(), EntityType.RABBIT, EntityInit.RABBIT.get(), EntityType.SHEEP, EntityInit.SHEEP.get(),
			EntityType.VILLAGER, EntityInit.VILLAGER.get());

	private static final Map<EntityType<? extends Monster>, EntityType<? extends Monster>> monsterList = Map.of(EntityType.HUSK,
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

	 public static void possessMob(Mob mob, ServerLevel level, boolean isAngry, boolean affectMonsters) {
		 EntityType<?> type = mob.getType();
		if (mobList.containsKey(type)) {
			Mob posMob = mob.convertTo(mobList.get(mob.getType()), true);
			posMob.setHealth(mob.getHealth());
			if (type.equals(EntityType.PIG) && ((Pig) mob).isSaddled()) {
				((PosPig) posMob).equipSaddle(null);
			}
			else if (type.equals(RABBIT)) {
				((PosRabbit)posMob).setVariant(((Rabbit) mob).getVariant());
			}
			else if (type.equals(SHEEP)) {
				((PosSheep)posMob).setColor(((Sheep) mob).getColor());
				((PosSheep)posMob).setSheared(((Sheep) mob).isSheared());
			}
			else if (type.equals(VILLAGER)) {
				((PosVillager) posMob).setVillagerData(((Villager) mob).getVillagerData());
			}
			if (isAngry) {
				level.playSound(null, mob.blockPosition(), SoundEvents.GHAST_HURT, SoundSource.HOSTILE, 1.0F,
						1.0F);
				mob.setLastHurtByMob(mob.getLastHurtByMob());
			}
		}
		if (affectMonsters && monsterList.containsKey(mob.getType())) {
			Mob posMob = mob.convertTo(monsterList.get(type), true);
			if (type.equals(ZOMBIE_VILLAGER)) {
				((PosZombieVillager) posMob).setVillagerData(((ZombieVillager) mob).getVillagerData());
			}else if (type.equals(HUSK)){
				posMob.setNoAi(false);
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
