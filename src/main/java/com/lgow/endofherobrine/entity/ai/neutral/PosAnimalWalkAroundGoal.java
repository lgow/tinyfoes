package com.lgow.endofherobrine.entity.ai.neutral;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class PosAnimalWalkAroundGoal extends WaterAvoidingRandomStrollGoal {
	public PosAnimalWalkAroundGoal(PathfinderMob pMob, double pSpeedModifier) {
		super(pMob, pSpeedModifier);
	}

	@Override
	public boolean canUse() {
		return ! this.mob.level().hasNearbyAlivePlayer(this.mob.getX(), this.mob.getY(), this.mob.getZ(), 40D)
				&& super.canUse();
	}
}
