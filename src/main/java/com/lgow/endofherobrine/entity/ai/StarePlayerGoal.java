package com.lgow.endofherobrine.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

public class StarePlayerGoal extends LookAtPlayerGoal {
	private boolean isHerobrine;

	public StarePlayerGoal(Mob goalOwner, float dist, boolean isHerobrine) {
		super(goalOwner, Player.class, dist, 100);
		if (isHerobrine) {
			this.lookAtContext.ignoreLineOfSight();
			this.isHerobrine = true;
		}
	}

	public boolean canContinueToUse() {
		if (!this.lookAt.isAlive()) {
			return false;
		}
		return !(this.mob.distanceToSqr(this.lookAt) > (double) (this.lookDistance * this.lookDistance));
	}

	public void start() {
	}

	public void tick() {
		if (this.lookAt.isAlive()) {
			this.mob.getLookControl().setLookAt(this.lookAt.getX(), this.lookAt.getEyeY(), this.lookAt.getZ(),
					this.mob.getMaxHeadYRot(), this.mob.getMaxHeadXRot());
		}
	}
}