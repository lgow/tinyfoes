package com.lgow.endofherobrine.entity;

import com.lgow.endofherobrine.entity.ai.StarePlayerGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;

public interface PossessedMob {
	default void registerPosMobGoals(PathfinderMob mob) {
		mob.goalSelector.addGoal(1, new StarePlayerGoal(mob, 40.0F));
		mob.goalSelector.addGoal(2, new FloatGoal(mob));
		mob.goalSelector.addGoal(4, new RandomLookAroundGoal(mob));
	}
}
