package com.lgow.endofherobrine.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public interface Teleporter {
	default boolean checkSafePos(LivingEntity entity, BlockPos.MutableBlockPos mutablePos, boolean avoidWater) {
		BlockState blockstate = entity.level().getBlockState(mutablePos);
		boolean blocksMotion = blockstate.blocksMotion();
		boolean posSafe = avoidWater ? blocksMotion : (blocksMotion || blockstate.getFluidState().is(FluidTags.WATER));
		return mutablePos.getY() > entity.level().getMinBuildHeight() && !posSafe;
	}

	default boolean willHaveSightOfTarget(LivingEntity chaser, BlockPos.MutableBlockPos mutablePos, LivingEntity target) {
		Vec3 mVec = new Vec3(mutablePos.getX(), mutablePos.getY() + chaser.getEyeHeight(), mutablePos.getZ());
		Vec3 pVec = new Vec3(target.getX(), target.getEyeY(), target.getZ());
		if (mVec.distanceTo(pVec) <= 128.0D && !target.isSpectator()) {
			return chaser.level().clip(
					new ClipContext(mVec, pVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, chaser)).getType()
					== HitResult.Type.MISS;
		}
		return false;
	}

	default boolean attemptTeleport(LivingEntity teleporter, double x, double y, double z, LivingEntity target, boolean avoidWater) {
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(x, y, z);
		while (this.checkSafePos(teleporter,mutablePos, avoidWater)) {
			mutablePos.move(Direction.DOWN);
		}
		BlockState blockstate = teleporter.level().getBlockState(mutablePos);
		boolean safePos = blockstate.blocksMotion() ||(!avoidWater && blockstate.getFluidState().is(FluidTags.WATER));
		boolean isLurking = target == null || this.willHaveSightOfTarget(teleporter, mutablePos, target);
		boolean avoidFluid = avoidWater ? !blockstate.getFluidState().isEmpty() : blockstate.getFluidState().is(FluidTags.LAVA);
		if(safePos && !avoidFluid && isLurking) {
			boolean canTeleport = this.randomTeleport(teleporter, x, y, z, avoidWater);
			if (canTeleport && !teleporter.isSilent()) {
				teleporter.level().playSound(null, teleporter.xo, teleporter.yo, teleporter.zo, SoundEvents.ENDERMAN_TELEPORT,
						teleporter.getSoundSource(), 1.0F, 1.0F);
				teleporter.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}
			return canTeleport;
		}
		return false;
	}

	default boolean attemptTeleport(LivingEntity entity, double x, double y, double z) {
		return this.attemptTeleport(entity, x, y, z, null, false);
	}

	default boolean randomTeleport(LivingEntity entity, double pX, double pY, double pZ, boolean avoidWater) {
		double d0 = entity.getX();
		double d1 = entity.getY();
		double d2 = entity.getZ();
		double d3 = pY;
		boolean flag = false;
		BlockPos blockpos = new BlockPos.MutableBlockPos(pX, pY, pZ);
		Level level = entity.level();
		if (level.hasChunkAt(blockpos)) {
			boolean hasChunk = false;

			while(!hasChunk && blockpos.getY() > level.getMinBuildHeight()) {
				BlockPos below = blockpos.below();
				BlockState blockstate = level.getBlockState(below);
				if (blockstate.blocksMotion() || !avoidWater && blockstate.getFluidState().is(FluidTags.WATER)) {
					hasChunk = true;
				} else {
					--d3;
					blockpos = below;
				}
			}

			if (hasChunk) {
				entity.teleportTo(pX, d3, pZ);
				if (level.noCollision(entity)) {
					flag = true;
				}
			}
		}

		if (!flag) {
			entity.teleportTo(d0, d1, d2);
			return false;
		} else {
			level.broadcastEntityEvent(entity, (byte)46);

			if (entity instanceof PathfinderMob mob) {
				mob.getNavigation().stop();
			}
			return true;
		}
	}

	default boolean teleportInFrontOf(LivingEntity chaser, LivingEntity target) {
		if (!chaser.level().isClientSide && target != null) {
			Vec3 targetPos = target.position();
			Vec3 chaserPos = chaser.position();
			Vec3 dir = targetPos.subtract(chaserPos);
			dir = dir.normalize();
			dir = dir.multiply(new Vec3(-1.5D, -1.5D, -1.5D));
			dir = targetPos.add(dir);
			return this.attemptTeleport(chaser, dir.x, target.getY(), dir.z);
		}
		return false;
	}

	default boolean teleportBehindOf(LivingEntity chaser, LivingEntity target) {
		if (!chaser.level().isClientSide && target != null) {
			Vec3 targetPos = target.position();
			Vec3 chaserPos = chaser.position();
			Vec3 dir = targetPos.subtract(chaserPos);
			dir = dir.normalize();
			dir = dir.multiply(new Vec3(1.5D, 1, 1.5D));
			dir = targetPos.add(dir);
			return this.attemptTeleport(chaser, dir.x, target.getY(), dir.z);
		}
		return false;
	}
}
