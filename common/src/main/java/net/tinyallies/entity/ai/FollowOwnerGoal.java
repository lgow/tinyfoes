package net.tinyallies.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.tinyallies.entity.BabyMonster;
import net.tinyallies.entity.EnderBoy;

import java.util.EnumSet;

public class FollowOwnerGoal extends Goal {
	private final PathfinderMob mob;
	private final LevelReader level;
	private final double speedModifier;
	private final PathNavigation navigation;
	private final float stopDistance;
	private final float startDistance;
	private final boolean canFly;
	private BabyMonster baby;
	private LivingEntity owner;
	private int timeToRecalcPath;
	private float oldWaterCost;

	public FollowOwnerGoal(PathfinderMob pTamable, double pSpeedModifier, float pStartDistance, float pStopDistance, boolean pCanFly) {
		this.mob = pTamable;
		if (this.mob instanceof BabyMonster baby) {
			this.baby = baby;
		}
		this.level = pTamable.level;
		this.speedModifier = pSpeedModifier;
		this.navigation = pTamable.getNavigation();
		this.startDistance = pStartDistance;
		this.stopDistance = pStopDistance;
		this.canFly = pCanFly;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public boolean canUse() {
		LivingEntity owner1 = this.baby.getOwner();
		if (owner1 == null) {
			return false;
		}
		else if (owner1.isSpectator()) {
			return false;
		}
		else if (this.unableToMove()) {
			return false;
		}
		else if (this.baby.isUndead()) {
			LivingEntity lastHurtByMob = owner1.getLastHurtByMob();
			if (lastHurtByMob != null && lastHurtByMob.getLastHurtMob() == owner1 && lastHurtByMob.getCombatTracker()
					.isInCombat()) {
				return false;
			}
			LivingEntity lastHurtMob = owner1.getLastHurtMob();
			if (lastHurtMob != null && (lastHurtMob.isAlive() || lastHurtMob.distanceToSqr(owner1) > 14.0D)) {
				return false;
			}
		}
		owner = owner1;
		return this.mob.distanceToSqr(owner1) >= (double) (this.startDistance * this.startDistance) || !this.mob.hasLineOfSight(this.owner);
	}

	public boolean canContinueToUse() {
		if (this.navigation.isDone()) {
			return false;
		}
		else if (this.unableToMove()) {
			return false;
		}
		else {
			return this.mob.distanceToSqr(this.owner) > (double) (this.stopDistance * this.stopDistance);
		}
	}

	private boolean unableToMove() {
		return this.baby.isOrderedToSit() || this.mob.isPassenger() || this.mob.isLeashed();
	}

	public void start() {
		this.timeToRecalcPath = 0;
		this.oldWaterCost = this.mob.getPathfindingMalus(BlockPathTypes.WATER);
		this.mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
	}

	public void stop() {
		this.owner = null;
		this.navigation.stop();
		this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
	}

	public void tick() {
		this.mob.getLookControl().setLookAt(this.owner, 10.0F, (float) this.mob.getMaxHeadXRot());
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(40);
			if (this.mob.distanceToSqr(this.owner) >= 180.0D) {
				this.teleportToOwner();
				return;
			}
			else {
				if (this.mob instanceof EnderBoy && this.mob.level.isRaining()) {
					return;
				}
				if (this.baby.isUndead()) {
					ItemStack helmet = this.mob.getItemBySlot(EquipmentSlot.HEAD);
					if (helmet.isEmpty() && this.mob.level.isDay()) {
						return ;
					}
					else if (!helmet.isEmpty() && (helmet.getMaxDamage() - helmet.getDamageValue()) <= helmet.getMaxDamage() / 3) {
						return ;
					}
					LivingEntity lastHurtByMob = owner.getLastHurtByMob();
					if (lastHurtByMob != null && lastHurtByMob.getLastHurtMob() == owner
							&& lastHurtByMob.getCombatTracker().isInCombat()) {
						return;
					}
					LivingEntity lastHurtMob = owner.getLastHurtMob();
					if (lastHurtMob != null && (lastHurtMob.isAlive() || lastHurtMob.distanceToSqr(owner) > 14.0D)) {
						return;
					}
				}
			}
			this.navigation.moveTo(this.owner, this.speedModifier);
		}
	}

	private void teleportToOwner() {
		BlockPos blockpos = this.owner.blockPosition();
		for (int i = 0; i < 10; ++i) {
			int j = this.randomIntInclusive(-3, 3);
			int k = this.randomIntInclusive(-1, 1);
			int l = this.randomIntInclusive(-3, 3);
			boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
			if (flag) {
				return;
			}
		}
	}

	private boolean maybeTeleportTo(int pX, int pY, int pZ) {
		if (Math.abs((double) pX - this.owner.getX()) < 2.0D && Math.abs((double) pZ - this.owner.getZ()) < 2.0D) {
			return false;
		}
		else if (!this.canTeleportTo(new BlockPos(pX, pY, pZ))) {
			return false;
		}
		else {
			this.mob.moveTo((double) pX + 0.5D, pY, (double) pZ + 0.5D, this.mob.getYRot(), this.mob.getXRot());
			this.navigation.stop();
			return true;
		}
	}

	private boolean canTeleportTo(BlockPos pPos) {
		BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, pPos.mutable());
		BlockState blockstate = this.level.getBlockState(pPos.below());
		if (blockpathtypes != BlockPathTypes.WALKABLE) {
			return false;
		}
		else if (!this.canFly && blockstate.getBlock() instanceof LeavesBlock) {
			return false;
		}
		else if (this.mob instanceof EnderBoy && this.mob.level.isRainingAt(pPos)) {
			return false;
		}
		else if (this.baby.isUndead()) {
			ItemStack helmet = this.mob.getItemBySlot(EquipmentSlot.HEAD);
			if (!helmet.isEmpty() && (helmet.getMaxDamage() - helmet.getDamageValue()) <= helmet.getMaxDamage() / 3) {
				return false;
			}
			LivingEntity lastHurtByMob = owner.getLastHurtByMob();
			if (lastHurtByMob != null && lastHurtByMob.getLastHurtMob() == owner && lastHurtByMob.getCombatTracker()
					.isInCombat()) {
				return false;
			}
			LivingEntity lastHurtMob = owner.getLastHurtMob();
			if (lastHurtMob != null && (lastHurtMob.isAlive() || lastHurtMob.distanceToSqr(owner) > 14.0D)) {
				return false;
			}
		}
		BlockPos blockpos = pPos.subtract(this.mob.blockPosition());
		return this.level.noCollision(this.mob, this.mob.getBoundingBox().move(blockpos));
	}

	private int randomIntInclusive(int pMin, int pMax) {
		return this.mob.getRandom().nextInt(pMax - pMin + 1) + pMin;
	}
}