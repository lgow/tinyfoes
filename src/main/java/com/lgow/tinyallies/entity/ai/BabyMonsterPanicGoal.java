package com.lgow.tinyallies.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class BabyMonsterPanicGoal extends PanicGoal {
	public BabyMonsterPanicGoal(PathfinderMob mob, double pSpeedModifier) {
		super(mob, pSpeedModifier);
	}

	protected boolean shouldPanic() {
		return this.mob.isFreezing() || this.mob.isOnFire();
	}
}