package net.tinyallies.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.tinyallies.entity.BabyMonster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LookForParentGoal extends Goal {
	private final Mob mob;
	private final double speedModifier;
	private final Class<? extends PathfinderMob> parentClass;
	private BabyMonster baby;
	@Nullable private Mob parent;
	private int timeToRecalcPath;

	public LookForParentGoal(Mob pMob, double pSpeedModifier, Class<? extends PathfinderMob> pParentClass) {
		if (pMob instanceof BabyMonster babyMonster) {
			this.baby = babyMonster;
		}
		this.mob = pMob;
		this.parentClass = pParentClass;
		this.speedModifier = pSpeedModifier;
	}

	public boolean canUse() {
		List<? extends Mob> list = this.mob.level.getEntitiesOfClass(parentClass,
				this.mob.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
		Mob newParent = null;
		double d0 = Double.MAX_VALUE;
		for (Mob animal1 : list) {
			if (!animal1.isDeadOrDying() && !animal1.isBaby()) {
				double d1 = this.mob.distanceToSqr(animal1);
				if (!(d1 > d0)) {
					d0 = d1;
					newParent = animal1;
				}
			}
		}
		if (newParent == null) {
			return false;
		}
		else if (d0 < 9.0D) {
			return false;
		}
		else {
			this.parent = newParent;
			if (this.baby.getParent() == null) {
				this.baby.setParent(newParent);
				this.baby.reassessTameGoals();
			}
			return true;
		}
	}

	public boolean canContinueToUse() {
		if (this.baby.isTamed() || !this.parent.isAlive()) {
			return false;
		}
		else {
			double d0 = this.mob.distanceToSqr(this.parent);
			return !(d0 < 9.0D) && !(d0 > 256.0D);
		}
	}

	public void start() {
		this.timeToRecalcPath = 0;
	}

	public void stop() {
		this.parent = null;
	}

	public void tick() {
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			this.mob.getNavigation().moveTo(this.parent, this.speedModifier);
		}
	}
}