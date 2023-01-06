package com.lgow.endofherobrine.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class StarePlayerGoal extends LookAtPlayerGoal {

    private int lookTime;
    private final boolean canAlwaysSeePlayer;

    public StarePlayerGoal(Mob goalOwner, float dist, boolean canAlwaysSeePlayer) {
        super(goalOwner, Player.class, dist, 1);
        this.canAlwaysSeePlayer = canAlwaysSeePlayer;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    public StarePlayerGoal(Mob goalOwner, float dist) {
        this(goalOwner, dist, false);
    }

    public void start() {
        this.lookTime = this.adjustedTickDelay(40 + this.mob.getRandom().nextInt(40));
    }


    public boolean canContinueToUse() {
        if (!this.lookAt.isAlive() && !canAlwaysSeePlayer) {
            return false;
        } else if (this.mob.distanceToSqr(this.lookAt) > (double)(this.lookDistance * this.lookDistance)) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    public void tick() {
        if (this.lookAt.isAlive() || canAlwaysSeePlayer) {
            this.mob.getLookControl().setLookAt(this.lookAt.getX(), this.lookAt.getEyeY(), this.lookAt.getZ());
            --this.lookTime;
        }
    }
}