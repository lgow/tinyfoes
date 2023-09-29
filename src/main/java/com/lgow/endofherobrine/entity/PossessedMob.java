package com.lgow.endofherobrine.entity;

import com.lgow.endofherobrine.entity.ai.StarePlayerGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

import java.util.UUID;

public interface PossessedMob {
	default boolean canRevertPossession() {
		return true;
	}



	default void registerPosMobGoals(PathfinderMob mob, boolean hasCustomAttack) {
		registerPosMobGoals(mob, hasCustomAttack, 0.0);
	}

	default void registerPosMobGoals(PathfinderMob mob, boolean hasCustomAttack, double attackSpeed) {
		if (!hasCustomAttack) {
			mob.goalSelector.addGoal(0, new MeleeAttackGoal(mob, attackSpeed, true));
		}
		if (!mob.getType().equals(EntityInit.P_SILVERFISH.get())) {
			mob.goalSelector.addGoal(1, new StarePlayerGoal(mob, 40.0F, false));
		}
		mob.goalSelector.addGoal(2, new FloatGoal(mob));
		mob.goalSelector.addGoal(4, new RandomLookAroundGoal(mob));
	}
}
