package com.lgow.tinyallies.entity.ai;

import com.lgow.tinyallies.entity.BabyMonster;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class WildBabyTargeGoal <T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	private final PathfinderMob mob;

	private BabyMonster baby;

	public WildBabyTargeGoal(PathfinderMob mob, Class<T> pTargetType, boolean pMustSee) {
		super(mob, pTargetType, 10, pMustSee, false, null);
		this.mob = mob;
		if (this.mob instanceof BabyMonster baby) {
			this.baby = baby;
		}
	}

	public boolean canUse() {
		return !this.baby.isTamed() && this.baby.getMonsterParent() != null && super.canUse();
	}

	public boolean canContinueToUse() {
		return this.baby.getMonsterParent() != null && this.targetConditions.test(this.mob, this.target);
	}

	@Override
	public void stop() {
		super.stop();
		this.mob.level.getServer().sendSystemMessage(Component.literal( "stop"));
	}
}