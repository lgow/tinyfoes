package com.lgow.endofherobrine.entity.herobrine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Lurker extends AbstractHerobrine {
	private static final EntityDataAccessor<Boolean> IS_ANGRY = SynchedEntityData.defineId(Lurker.class,
			EntityDataSerializers.BOOLEAN);

	private int lastLurkedTimer, watchedPlayerTimer, playSoundCooldown;

	public Lurker(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1, true));
		this.targetSelector.addGoal(0, new LurkerTargetGoal(this));
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		if (!this.canSeeAnyPlayers()) { this.tpToWatchPlayer(this.getNearestPlayer()); }
		this.resetTpCooldown();
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	private void resetTpCooldown() {
		this.teleportCooldown = 60;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(IS_ANGRY, false);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("IsAngry", this.getAngry());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.setAngry(pCompound.getBoolean("IsAngry"));
	}

	private void warnPlayer() {
		if (this.canSeeAnyPlayers() && this.getTargetPlayer() != null) {
			if(random.nextInt(3) ==0 && this.playSoundCooldown <= 0){
				level().playSound(null, this.getTargetPlayer().blockPosition(), SoundEvents.AMBIENT_CAVE.get(),
						SoundSource.HOSTILE, 0.5F, (float) (0.8F + (Math.random() * 0.2D)));
				this.playSoundCooldown = 4000;
			}
			else {
				this.playSoundCooldown = 600;
			}
		}
	}

	private void tooCloseToPlayer() {
		if (!this.level().isClientSide && this.getNearestPlayer() != null && this.distanceTo(getNearestPlayer()) <= 5
				&& !this.getAngry()) {
			this.teleportAway();
		}
	}



	private void lurkPlayer() {
		if (this.teleportCooldown <= 0 && this.lastLurkedTimer > 60 && this.tpToWatchPlayer(this.getNearestPlayer())) {
			this.resetTpCooldown();
		}
	}

	private void leave() {
		if (this.canSeeAnyPlayers()) {
			this.lastLurkedTimer = 0;

			if(watchedPlayerTimer > 4000){
				this.teleportAway();
			}
			else {
				this.watchedPlayerTimer++;
			}
		}
		else if (this.lastLurkedTimer > 600) {
			this.teleportAway();
		}
		else{
			this.lastLurkedTimer++;
		}
	}

	@Override
	public void customServerAiStep() {
		this.playSoundCooldown--;
		this.teleportCooldown--;
		this.leave();
		this.tooCloseToPlayer();
		this.warnPlayer();
		this.lurkPlayer();
		super.customServerAiStep();
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		boolean hurtTarget = super.doHurtTarget(entityIn);
		if (hurtTarget && entityIn instanceof Player player) {
			player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 150, 1, false, false));
			player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 150, 1, false, false));
			level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.AMBIENT_CAVE.get(),
					SoundSource.HOSTILE, 0.5F, (float) (0.8F + (Math.random() * 0.2D)), false);

			this.discard();
		}
		return hurtTarget;
	}



	public boolean isBeingLookedAtBy(Player pPlayer) {
		Vec3 vec3 = pPlayer.getViewVector(1.0F).normalize();
		Vec3 vec31 = new Vec3(this.getX() - pPlayer.getX(), this.getEyeY() - pPlayer.getEyeY(),
				this.getZ() - pPlayer.getZ());
		double d0 = vec31.length();
		vec31 = vec31.normalize();
		double d1 = vec3.dot(vec31);
		return d1 > 1.0D - 0.025D / d0 && pPlayer.hasLineOfSight(this);
	}



	private boolean getAngry() {
		return this.entityData.get(IS_ANGRY);
	}

	private void setAngry(boolean isAngry) {
		this.entityData.set(IS_ANGRY, isAngry);
	}

	private static class LurkerTargetGoal extends NearestAttackableTargetGoal<Player> {
		private final Lurker lurker;

		private final TargetingConditions startAggroTargetConditions, continueAggroTargetConditions = TargetingConditions.forCombat()
				.ignoreLineOfSight();

		@Nullable private Player pendingTarget;

		private int aggroTime;

		public LurkerTargetGoal(Lurker lurker) {
			super(lurker, Player.class, false);
			this.lurker = lurker;
			this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(
					(livingEntity) -> lurker.isBeingLookedAtBy((Player) livingEntity));
		}

		public boolean canUse() {
			this.pendingTarget = this.lurker.level().getNearestPlayer(this.startAggroTargetConditions, this.lurker);
			return this.pendingTarget != null;
		}

		public void start() {
			super.start();
		}

		public boolean canContinueToUse() {
			if (this.pendingTarget != null) {
				if (!this.lurker.isBeingLookedAtBy(this.pendingTarget)) {
					return false;
				}
				else {
					this.lurker.lookAt(this.pendingTarget, 10.0F, 10.0F);
					return true;
				}
			}
			else {
				return this.target != null && this.continueAggroTargetConditions.test(this.lurker, this.target)
						|| super.canContinueToUse();
			}
		}

		public void stop() {
			this.pendingTarget = null;
			super.stop();
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
			}
			else {
				if (this.target != null && this.target.distanceToSqr(this.lurker) > 6.0D
						&& this.lurker.teleportCooldown <= 0 && (this.lurker.teleportInFrontOf(
						this.lurker, this.target) || this.lurker.teleportBehindOf(this.lurker, this.target))) {
					this.lurker.resetTpCooldown();
				}
				this.lurker.setAngry(true);
				super.tick();
			}
		}
	}
}

