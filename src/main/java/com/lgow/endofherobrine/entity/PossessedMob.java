package com.lgow.endofherobrine.entity;

import com.lgow.endofherobrine.entity.ai.StarePlayerGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

public interface PossessedMob {
	default void registerPosMobGoals(PathfinderMob mob, boolean hasCustomAttack) {
		registerPosMobGoals(mob, hasCustomAttack, 0.0);
	}

	default void registerPosMobGoals(PathfinderMob mob, boolean hasCustomAttack, double attackSpeed) {
		if (!hasCustomAttack) {
			mob.goalSelector.addGoal(0, new MeleeAttackGoal(mob, attackSpeed, true));
		}
		if(!mob.getType().equals(EntityInit.SILVERFISH.get())){
			mob.goalSelector.addGoal(1, new StarePlayerGoal(mob, 40.0F, false));
		}
		mob.goalSelector.addGoal(2, new FloatGoal(mob));
		mob.goalSelector.addGoal(4, new RandomLookAroundGoal(mob));
	}
}
