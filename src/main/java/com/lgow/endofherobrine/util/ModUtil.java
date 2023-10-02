package com.lgow.endofherobrine.util;

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

import java.util.*;

import static com.lgow.endofherobrine.config.ModConfigs.*;
import static com.lgow.endofherobrine.entity.EntityInit.*;
import static net.minecraft.world.entity.EntityType.*;

public class ModUtil {
	public static Random random = new Random();
	private static final Map<EntityType<? extends Mob>, EntityType<? extends Mob>> animalPossessionList = Map.of(
			CHICKEN, P_CHICKEN.get(), COW, P_COW.get(), PIG, P_PIG.get(), RABBIT, P_RABBIT.get(), SHEEP, P_SHEEP.get(),
			VILLAGER, P_VILlAGER.get());
	private static final Map<EntityType<? extends Monster>, EntityType<? extends Monster>> monsterPossessionList = Map.of(
			HUSK, P_HUSK.get(), SILVERFISH, P_SILVERFISH.get(), SKELETON, P_SKELETON.get(), STRAY, P_STRAY.get(),
			ZOMBIE, P_ZOMBIE.get(), ZOMBIE_VILLAGER, P_ZOMBIE_VILLAGER.get());
	private static final Map<EntityType<? extends Mob>, EntityType<? extends Mob>> reversionList = new HashMap<>();

	static {
		reversionList.put(P_CHICKEN.get(), CHICKEN);
		reversionList.put(P_COW.get(), COW);
		reversionList.put(P_PIG.get(), PIG);
		reversionList.put(P_RABBIT.get(), RABBIT);
		reversionList.put(P_SHEEP.get(), SHEEP);
		reversionList.put(P_VILlAGER.get(), VILLAGER);
		reversionList.put(P_HUSK.get(), HUSK);
		reversionList.put(P_SILVERFISH.get(), SILVERFISH);
		reversionList.put(P_SKELETON.get(), SKELETON);
		reversionList.put(P_STRAY.get(), STRAY);
		reversionList.put(P_ZOMBIE.get(), ZOMBIE);
		reversionList.put(P_ZOMBIE_VILLAGER.get(), ZOMBIE_VILLAGER);
	}

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
		if (shouldDoMobPossession() && !mob.getType().getTags().toList().contains(DONT_POSSESS)) {
			if (animalPossessionList.containsKey(type)) {
				Mob posMob = mob.convertTo(animalPossessionList.get(mob.getType()), true);
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
		if (affectMonsters && monsterPossessionList.containsKey(mob.getType())) {
			Mob posMob = mob.convertTo(monsterPossessionList.get(type), true);
			if (type.equals(ZOMBIE_VILLAGER)) {
				((PosZombieVillager) posMob).setVillagerData(((ZombieVillager) mob).getVillagerData());
				if (((ZombieVillager) mob).tradeOffers != null) {
					((PosZombieVillager) posMob).setTradeOffers(((ZombieVillager) mob).tradeOffers);
				}
			}
		}
	}

	public static void revertPossession(Mob posMob, boolean canConvert) {
		EntityType<?> posMobType = posMob.getType();
		EntityType<? extends Mob> type = reversionList.get(posMob.getType());
		if (posMob.isAlive() && reversionList.containsKey(posMobType) && (!shouldDoMobPossession()
				|| shouldPreventPossession(type) || (shouldRevertPossession(canConvert)))) {
			Mob mob = posMob.convertTo(type, true);
			if (posMobType.equals(P_PIG.get())) {
				if (((PosPig) posMob).isSaddled()) {
					((Pig) posMob).equipSaddle(null);
				}
			}
			else if (posMobType.equals(P_RABBIT.get())) {
				((Rabbit) mob).setVariant(((PosRabbit) posMob).getVariant());
			}
			else if (posMobType.equals(P_SHEEP.get())) {
				((Sheep) mob).setColor(((PosSheep) posMob).getColor());
				((Sheep) mob).setSheared(((PosSheep) posMob).isSheared());
			}
			else if (posMobType.equals(P_VILlAGER.get())) {
				((Villager) mob).setVillagerData(((PosVillager) posMob).getVillagerData());
				((Villager) mob).setOffers(((PosVillager) posMob).getOffers());
				((Villager) mob).setVillagerXp(((PosVillager) posMob).getVillagerXp());
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
