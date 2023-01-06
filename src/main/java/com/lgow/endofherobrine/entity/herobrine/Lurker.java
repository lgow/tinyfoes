package com.lgow.endofherobrine.entity.herobrine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Lurker extends AbstractHerobrine {

    public Lurker(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1, true));
        this.targetSelector.addGoal(0, new LurkerTargetGoal(this));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if(!this.isLookingAtAnyPlayer()){ this.teleportToLurk(this.getNearestPlayer());}
        this.teleportTimer = 5;
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && entityIn instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 150, 1,false , false));
            player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 150,1,false , false));
            if(level.isClientSide) {
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.AMBIENT_CAVE.get(), SoundSource.MASTER,
                        0.5F, (float) (0.8F + (Math.random() * 0.2D)), false);
            }
            this.discard();
        }
        return flag;
    }

    public boolean isBeingLookedAtBy(Player pPlayer) {
        Vec3 vec3 = pPlayer.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(this.getX() - pPlayer.getX(), this.getEyeY() - pPlayer.getEyeY(), this.getZ() - pPlayer.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0D - 0.025D / d0 && pPlayer.hasLineOfSight(this);

    }

    private boolean teleportToLurk(LivingEntity player) {
        if (!this.level.isClientSide && this.isAlive() && player!=null) {
            double randX = this.random.nextIntBetweenInclusive(15,40);
            double randZ = this.random.nextIntBetweenInclusive(15,40);
            double x = player.getX() + (this.random.nextBoolean() ? randX : -randX);
            double y = player.getY() + this.random.nextInt(16);
            double z = player.getZ() + (this.random.nextBoolean() ? randZ : -randZ);
            return this.attemptTeleport(this, x, y, z, player);
        }else {
            return false;
        }
    }

    private boolean wasApproachedByPlayer() {
        boolean b = false;
        if(!this.level.isClientSide && this.getNearestPlayer() != null && this.distanceTo(getNearestPlayer()) <= 10 && this.teleportTimer==0){
            this.discard();
            b = true;
        }
        return b;
    }

    @Override
    public void customServerAiStep() {
        this.wasApproachedByPlayer();
        super.customServerAiStep();
    }

    @Override
    public void tick() {
        this.lastSeenPlayerTimer++;

        if(this.isLookingAtAnyPlayer()){
            this.lastSeenPlayerTimer = 0;
        }
        else if(this.lastSeenPlayerTimer >= 60 ){
            this.teleportToLurk(this.getNearestPlayer());}

        if(!(this.getSpeed() > 0)){
            this.setYBodyRot(getYHeadRot());
        }
        super.tick();
    }

    public static class LurkerTargetGoal extends NearestAttackableTargetGoal<Player> {

        private final Lurker lurker;
        @javax.annotation.Nullable
        private Player pendingTarget;
        private int teleportTime, aggroTime;
        private final TargetingConditions startAggroTargetConditions, continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();

        public LurkerTargetGoal(Lurker lurker) {
            super(lurker, Player.class, false);
            this.lurker = lurker;
            this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(
                    (livingEntity) -> lurker.isBeingLookedAtBy((Player)livingEntity));
        }

        public boolean canUse() {
            this.pendingTarget = this.lurker.level.getNearestPlayer(this.startAggroTargetConditions, this.lurker);
            return this.pendingTarget != null;
        }

        public void start() {
            super.start();
        }

        public void stop() {
            this.pendingTarget = null;
            super.stop();
        }

        public boolean canContinueToUse() {
            if (this.pendingTarget != null) {
                if (!this.lurker.isBeingLookedAtBy(this.pendingTarget)) {
                    return false;
                } else {
                    this.lurker.lookAt(this.pendingTarget, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return this.target != null && this.continueAggroTargetConditions.test(this.lurker, this.target) || super.canContinueToUse();
            }
        }

        public void tick() {
            if (this.lurker.getTarget() == null) {
                super.setTarget(null);
            }

            if (this.pendingTarget != null) {
                if (--this.aggroTime <= 0) {
                    this.target = this.pendingTarget;
                    this.pendingTarget = null;
                    super.start();
                }
            } else {
                if (this.target != null && this.target.distanceToSqr(this.lurker) > 2.0D
                        && this.teleportTime++ >= this.adjustedTickDelay(5)
                        && this.lurker.teleportInFrontOf(this.lurker, this.target)) {
                    this.teleportTime = 0;
                    this.lurker.teleportTimer = 60;
                }

                super.tick();
            }
        }
    }
}

