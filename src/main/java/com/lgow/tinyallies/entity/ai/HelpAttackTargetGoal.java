package com.lgow.tinyallies.entity.ai;

import com.lgow.tinyallies.entity.BabyMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class HelpAttackTargetGoal extends TargetGoal {
	private BabyMonster baby;

	private LivingEntity ownerLastHurt;

	private int timestamp;

	public HelpAttackTargetGoal(PathfinderMob pTameAnimal) {
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
				this.ownerLastHurt = livingentity.getLastHurtMob();
				int i = livingentity.getLastHurtMobTimestamp();
				return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT)
						&& this.baby.wantsToAttack(this.ownerLastHurt, livingentity);
			}
		}
		else {
			return false;
		}
	}

	public void start() {
		this.mob.setTarget(this.ownerLastHurt);
		LivingEntity livingentity = this.baby.getOwner();
		if (livingentity != null) {
			this.timestamp = livingentity.getLastHurtMobTimestamp();
		}
		super.start();
	}
}