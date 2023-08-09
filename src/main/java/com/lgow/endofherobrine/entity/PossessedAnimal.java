package com.lgow.endofherobrine.entity;

import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.entity.ai.neutral.DefendPassiveMobsGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosAnimalPanicGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosAnimalTargetGoal;
import com.lgow.endofherobrine.entity.ai.neutral.PosAnimalWalkAroundGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

public interface PossessedAnimal extends PossessedMob {
	default Mob convertBack(Mob posMob, EntityType<? extends Mob> type, boolean canConvert) {
		if (posMob.isAlive() && ModConfigs.CONVERT_BACK.get() && posMob.tickCount > ModConfigs.REMAIN_POSSESSED.get()
				&& canConvert) {
			Mob mob = posMob.convertTo(type, true);
			mob.setHealth(posMob.getHealth());
			return mob;
		}
		else {
			return posMob;
		}
	}

	default void registerPosAnimalGoals(PathfinderMob mob, double speed) {
		this.registerPosAnimalGoals(mob, speed, speed);
	}

	default void registerPosAnimalGoals(PathfinderMob mob, double attackSpeed, double panicSpeed) {
		this.registerPosMobGoals(mob, false, attackSpeed);mob.goalSelector.addGoal(3, new PosAnimalPanicGoal(mob, panicSpeed));
		mob.goalSelector.addGoal(5, new PosAnimalWalkAroundGoal(mob, 1.0D));
		mob.targetSelector.addGoal(0, new DefendPassiveMobsGoal(mob));
		mob.targetSelector.addGoal(1, new PosAnimalTargetGoal<>(mob, Player.class));
		mob.targetSelector.addGoal(2, new HurtByTargetGoal(mob, Monster.class).setAlertOthers());
	}
}
