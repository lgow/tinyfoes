package com.lgow.endofherobrine.entity.ai.neutral;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class PosAnimalTargetGoal <T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	public PosAnimalTargetGoal(Mob entityIn, Class<T> classTarget) {
		super(entityIn, classTarget, true);
	}

	@Override
	public boolean canUse() {
		float f = this.mob.getLightLevelDependentMagicValue();
		return ! (f >= 0.5F) && super.canUse();
	}
}
