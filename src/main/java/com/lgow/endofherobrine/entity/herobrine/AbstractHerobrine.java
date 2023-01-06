package com.lgow.endofherobrine.entity.herobrine;

import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.Teleporter;
import com.lgow.endofherobrine.entity.ai.StarePlayerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHerobrine extends Monster implements Teleporter {
    public int lastSeenPlayerTimer;
    public int teleportTimer;

    protected AbstractHerobrine(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.setInvulnerable(true);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 256D)
                .add(Attributes.ATTACK_DAMAGE, 0.1D);
    }

    public Player getNearestPlayer() {
        return this.level.getNearestPlayer(TargetingConditions.forNonCombat().ignoreLineOfSight(), this);
    }

    @Override
    public MobType getMobType() { return ModMobTypes.HEROBRINE;}

    @Override
    public boolean isPushable() { return false;}

    @Override
    public boolean canBeCollidedWith() { return false;}

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dim) { return 1.725F;}

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return null;}

    @Override
    protected SoundEvent getDeathSound() { return null;}

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new StarePlayerGoal(this, Float.MAX_VALUE, true));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.spawnPositoning(blockPosition());
        this.setCustomName(Component.literal("Herobrine"));
//        this.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Spawned " + this.getTypeName().getString() + " at " + blockPosition()), false);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public boolean hasLineOfSight(Entity pEntity) {
        if (pEntity.level != this.level) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
            Vec3 vec31 = new Vec3(pEntity.getX(), pEntity.getEyeY(), pEntity.getZ());
            return this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
        }
    }

    public boolean isLookingAtAnyPlayer() {
        boolean isLookinAtAnyPlayers = false;
        if (!this.level.isClientSide){
            for (Player player : this.level.getServer().getPlayerList().getPlayers()) {
                isLookinAtAnyPlayers = player.hasLineOfSight(this);
            }
        }
        return isLookinAtAnyPlayers;
    }


    @Override
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            double d0 = this.getMoveControl().getSpeedModifier();
            this.setSprinting(d0 > 1);
        }
    }

    public void destroyBlocksInAABB(AABB area) {
        int minX = Mth.floor(area.minX);
        int minY = Mth.floor(area.minY);
        int minZ = Mth.floor(area.minZ);
        int maxX = Mth.floor(area.maxX);
        int maxY = Mth.floor(area.maxY);
        int maxZ = Mth.floor(area.maxZ);
        boolean destroyBlock = false;
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockPos blockpos = new BlockPos(x, y, z);
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    if (!blockstate.isAir() && blockstate.getMaterial() != Material.FIRE) {
                        destroyBlock = this.level.destroyBlock(blockpos, true);
                    }
                }
            }
        }
        if (destroyBlock) {
            this.swing(InteractionHand.MAIN_HAND, true);
        }
    }

    public void spawnPositoning(BlockPos pos) {
        BlockState blockState = level.getBlockState(pos.above());
        boolean blocksMotion = blockState.getMaterial().blocksMotion();
        if (blocksMotion && level.canSeeSky(pos.above())) {
            this.moveTo(pos.above(), getXRot(), getYRot());
        } else if (isInWall()) {
            this.destroyBlocksInAABB(this.getBoundingBox());
        }
    }

    @Override
    public void tick() {
        if(this.teleportTimer<0) { this.teleportTimer--;}
        super.tick();
    }
}
