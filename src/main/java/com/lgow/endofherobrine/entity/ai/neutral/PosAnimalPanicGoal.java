package com.lgow.endofherobrine.entity.ai.neutral;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class PosAnimalPanicGoal extends PanicGoal {
	public PosAnimalPanicGoal(PathfinderMob pMob, double pSpeedModifier) { super(pMob, pSpeedModifier); }

	@Override
	protected boolean shouldPanic() { return this.mob.lastHurtByPlayer == null && super.shouldPanic(); }
}
