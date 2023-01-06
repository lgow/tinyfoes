package com.lgow.endofherobrine.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public interface Teleporter {

    default boolean teleportConditions(LivingEntity entity, BlockPos.MutableBlockPos mutablePos){
        return mutablePos.getY() > entity.level.getMinBuildHeight() && !entity.level.getBlockState(mutablePos).getMaterial().blocksMotion();
    }

    default boolean willHaveSightOf(LivingEntity chaser, BlockPos.MutableBlockPos mutablePos, LivingEntity target) {
        Vec3 mutPosVec = new Vec3(mutablePos.getX(), mutablePos.getY() + chaser.getEyeHeight(), mutablePos.getZ());
        Vec3 playerVec = new Vec3(target.getX(), target.getEyeY(), target.getZ());

        if (mutPosVec.distanceTo(playerVec) > 128.0D || target.isSpectator()) {
            return false;
        } else {
            return chaser.level.clip(new ClipContext(mutPosVec, playerVec, ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE, chaser)).getType() == HitResult.Type.MISS;
        }
    }

    default boolean attemptTeleport(LivingEntity entity, double x, double y, double z, LivingEntity target) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(x, y, z);

        while (teleportConditions(entity, mutablePos)) {
            mutablePos.move(Direction.DOWN);
        }

        BlockState blockstate = entity.level.getBlockState(mutablePos);
        boolean isLurking = target == null || this.willHaveSightOf(entity, mutablePos, target);

        if (blockstate.getMaterial().blocksMotion() && !blockstate.getFluidState().is(FluidTags.WATER) && isLurking) {
            Vec3 vec3 = entity.position();
            boolean hasTeleported = entity.randomTeleport(x, y, z, true);

            if (hasTeleported) {
                entity.level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));

                if (!entity.isSilent()) {
                    entity.level.playSound(null, entity.xo, entity.yo, entity.zo, SoundEvents.ENDERMAN_TELEPORT,
                            entity.getSoundSource(), 1.0F, 1.0F);
                    entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }

            return hasTeleported;
        } else {
            return false;
        }
    }

    default boolean attemptTeleport(LivingEntity entity, double x, double y, double z) {
        return this.attemptTeleport(entity, x, y, z, null);
    }

    default boolean teleportInFrontOf(LivingEntity chaser, LivingEntity target) {
        if (!chaser.level.isClientSide && target != null) {
            Vec3 targetPos = target.position();
            Vec3 chaserPos = chaser.position();
            Vec3 dir = targetPos.subtract(chaserPos);
            dir = dir.normalize();
            dir = dir.multiply(new Vec3(-1.5D, -1.5D, -1.5D));
            dir = targetPos.add(dir);

            return this.attemptTeleport(chaser, dir.x, target.getY(), dir.z);
        } else {
            return false;
        }
    }

    default boolean teleportBehindOf(LivingEntity chaser, LivingEntity target){
        if(!chaser.level.isClientSide && target != null){
            Vec3 targetPos = target.position();
            Vec3 chaserPos = chaser.position();
            Vec3 dir = targetPos.subtract(chaserPos);
            dir = dir.normalize();
            dir = dir.multiply(new Vec3(1.5D, 1, 1.5D));
            dir = targetPos.add(dir);

            return this.attemptTeleport(chaser, dir.x, target.getY() + 0.25D, dir.z);
        }else {
            return false;
        }
    }
}
