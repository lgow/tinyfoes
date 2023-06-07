package com.lgow.tinyallies.entity.ai;

import com.lgow.tinyallies.entity.BabyMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class DefendParentTargetGoal extends TargetGoal {
	private BabyMonster baby;

	private LivingEntity ownerLastHurtBy;

	private int timestamp;

	public DefendParentTargetGoal(PathfinderMob pTameAnimal) {
		super(pTameAnimal, false);
		if (pTameAnimal instanceof BabyMonster babyMonster) {
			this.baby = babyMonster;
		}
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	public boolean canUse() {
		if (this.baby.isTamed() && !this.baby.isOrderedToSit()) {
			LivingEntity livingentity = this.baby.getOwner();
			if (livingentity == null) {
				return false;
			}
			else {
				this.ownerLastHurtBy = livingentity.getLastHurtByMob();
				int i = livingentity.getLastHurtByMobTimestamp();
				return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT)
						&& this.baby.wantsToAttack(this.ownerLastHurtBy, livingentity);
			}
		}
		else {
			return false;
		}
	}

	public void start() {
		this.mob.setTarget(this.ownerLastHurtBy);
		LivingEntity livingentity = this.baby.getOwner();
		if (livingentity != null) {
			this.timestamp = livingentity.getLastHurtByMobTimestamp();
		}
		super.start();
	}
}