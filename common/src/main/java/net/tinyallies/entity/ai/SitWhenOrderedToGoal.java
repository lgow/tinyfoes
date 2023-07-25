package net.tinyallies.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.tinyallies.entity.BabyMonster;

import java.util.EnumSet;

public class SitWhenOrderedToGoal extends Goal {
	private final PathfinderMob mob;
	private BabyMonster baby;

	public SitWhenOrderedToGoal(PathfinderMob pMob) {
		this.mob = pMob;
		if (this.mob instanceof BabyMonster baby) {
			this.baby = baby;
		}
		this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
	}

	public boolean canContinueToUse() {
		return this.baby.isOrderedToSit();
	}

	public boolean canUse() {
		if (!baby.isTamed()) {
			return false;
		}
		else if (this.mob.isInWaterOrBubble()) {
			return false;
		}
		else if (!this.mob.isOnGround()) {
			return false;
		}
		else {
			LivingEntity livingentity = baby.getOwner();
			if (livingentity == null) {
				return true;
			}
			else {
				return (!(this.mob.distanceToSqr(livingentity) < 144.0D) || livingentity.getLastHurtByMob() == null)
						&& baby.isOrderedToSit();
			}
		}
	}

	public void start() {
		this.mob.getNavigation().stop();
		this.baby.setInSittingPose(true);
	}

	public void stop() {
		this.baby.setInSittingPose(false);
	}
}
