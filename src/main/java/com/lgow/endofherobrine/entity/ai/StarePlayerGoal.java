package com.lgow.endofherobrine.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class StarePlayerGoal extends LookAtPlayerGoal {
	private int lookTime;

	private boolean onlyHorizontal;

	public StarePlayerGoal(Mob goalOwner, float dist) {
		super(goalOwner, Player.class, dist, 100);
		this.onlyHorizontal = false;
		this.setFlags(EnumSet.of(Flag.LOOK));
	}

	public boolean canContinueToUse() {
		if (!this.lookAt.isAlive()) {
			return false;
		} else if (this.mob.distanceToSqr(this.lookAt) > (double)(this.lookDistance * this.lookDistance)) {
			return false;
		} else {
			return this.lookTime > 0;
		}
	}

	public void start() {
		this.lookTime = this.adjustedTickDelay(40 + this.mob.getRandom().nextInt(40));
	}

	public void tick() {
		if (this.lookAt.isAlive()) {
			double d0 = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
			this.mob.getLookControl().setLookAt(this.lookAt.getX(), d0, this.lookAt.getZ());
			--this.lookTime;
		}
	}
}