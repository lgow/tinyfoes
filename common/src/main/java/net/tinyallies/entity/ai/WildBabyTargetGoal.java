package net.tinyallies.entity.ai;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.tinyallies.entity.BabyMonster;

public class WildBabyTargetGoal <T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	private final PathfinderMob mob;
	private BabyMonster baby;

	public WildBabyTargetGoal(PathfinderMob mob, Class<T> pTargetType, boolean pMustSee) {
		super(mob, pTargetType, 10, pMustSee, false, null);
		this.mob = mob;
		if (this.mob instanceof BabyMonster baby) {
			this.baby = baby;
		}
	}

	public boolean canUse() {
		return !this.baby.isTamed() && this.baby.getParent() != null && super.canUse();
	}

	public boolean canContinueToUse() {
		return this.baby.getParent() != null && this.targetConditions.test(this.mob, this.target);
	}

	@Override
	public void stop() {
		super.stop();
		this.mob.level.getServer().sendSystemMessage(Component.literal("stop"));
	}
}