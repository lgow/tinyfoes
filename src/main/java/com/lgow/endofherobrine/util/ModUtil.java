package com.lgow.endofherobrine.util;

import com.lgow.endofherobrine.config.ModConfigs;
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

import static com.lgow.endofherobrine.entity.EntityInit.*;
import static net.minecraft.world.entity.EntityType.*;

public class ModUtil {
	public static Random random = new Random();
	private static final Map<EntityType<? extends Mob>, EntityType<? extends Mob>> mobList = Map.of(CHICKEN,
			P_CHICKEN.get(), COW, P_COW.get(), PIG, P_PIG.get(), RABBIT, P_RABBIT.get(), SHEEP, P_SHEEP.get(), VILLAGER,
			P_VILlAGER.get());
	private static final Map<EntityType<? extends Mob>, EntityType<? extends Mob>> invertedMobList = Map.of(
			P_CHICKEN.get(), CHICKEN, P_COW.get(), COW, P_PIG.get(), PIG, P_RABBIT.get(), RABBIT, P_SHEEP.get(), SHEEP,
			P_VILlAGER.get(), VILLAGER);
	private static final Map<EntityType<? extends Monster>, EntityType<? extends Monster>> monsterList = Map.of(HUSK,
			P_HUSK.get(), SILVERFISH, P_SILVERFISH.get(), SKELETON, P_SKELETON.get(), STRAY, P_STRAY.get(), ZOMBIE,
			P_ZOMBIE.get(), ZOMBIE_VILLAGER, P_ZOMBIE_VILLAGER.get());

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
		if (ModConfigs.shouldDoMobPossession() && !mob.getType().getTags().toList().contains(DONT_POSSESS)) {
			if (mobList.containsKey(type)) {
				Mob posMob = mob.convertTo(mobList.get(mob.getType()), true);
				if (type.equals(PIG)) {
					if (((Pig) mob).isSaddled()) {
						((PosPig) posMob).equipSaddle(null);
					}
				}
				else if (type.equals(RABBIT)) {
					((PosRabbit) posMob).setVariant(((Rabbit) mob).getVariant());
				}
				else if (type.equals(SHEEP)) {
					((PosSheep) posMob).setColor(((Sheep) mob).getColor());
					((PosSheep) posMob).setSheared(((Sheep) mob).isSheared());
				}
				else if (type.equals(VILLAGER)) {
					((PosVillager) posMob).setVillagerData(((Villager) mob).getVillagerData());
					((PosVillager) posMob).setOffers(((Villager) mob).getOffers());
					((PosVillager) posMob).setVillagerXp(((Villager) mob).getVillagerXp());
				}
				if (isAngry) {
					level.playSound(null, mob.blockPosition(), SoundEvents.GHAST_HURT, SoundSource.HOSTILE, 1.0F, 1.0F);
					mob.setLastHurtByMob(mob.getLastHurtByMob());
				}
			}
		}
		if (affectMonsters && monsterList.containsKey(mob.getType())) {
			Mob posMob = mob.convertTo(monsterList.get(type), true);
			if (type.equals(ZOMBIE_VILLAGER)) {
				((PosZombieVillager) posMob).setVillagerData(((ZombieVillager) mob).getVillagerData());
				if (((ZombieVillager) mob).tradeOffers != null) {
					((PosZombieVillager) posMob).setTradeOffers(((ZombieVillager) mob).tradeOffers);
				}
			}
		}
	}

	public static void revertPossession(Mob posMob, boolean canConvert) {
		EntityType<?> type = posMob.getType();
		if (invertedMobList.containsKey(type)) {
			if (posMob.isAlive() && ((ModConfigs.shouldRevertPossession()
					&& posMob.tickCount > ModConfigs.getRemainPossessedTicks().get() && canConvert))) {
				Mob mob = posMob.convertTo(invertedMobList.get(posMob.getType()), true);
				if (type.equals(P_PIG.get())) {
					if (((PosPig) posMob).isSaddled()) {
						((Pig) posMob).equipSaddle(null);
					}
				}
				else if (type.equals(P_RABBIT.get())) {
					((Rabbit) mob).setVariant(((PosRabbit) posMob).getVariant());
				}
				else if (type.equals(P_SHEEP.get())) {
					((Sheep) mob).setColor(((PosSheep) posMob).getColor());
					((Sheep) mob).setSheared(((PosSheep) posMob).isSheared());
				}
				else if (type.equals(P_VILlAGER.get())) {
					((Villager) mob).setVillagerData(((PosVillager) posMob).getVillagerData());
					((Villager) mob).setOffers(((PosVillager) posMob).getOffers());
					((Villager) mob).setVillagerXp(((PosVillager) posMob).getVillagerXp());
				}
			}
		}
	}

	public static boolean herobrineExists(Level level) {
		List<AbstractHerobrine> list = new ArrayList<>();
		for (ServerPlayer serverplayer : level.getServer().getPlayerList().getPlayers()) {
			list.addAll(level.getEntitiesOfClass(AbstractHerobrine.class, serverplayer.getBoundingBox().inflate(256)));
		}
		return !list.isEmpty();
	}
}
