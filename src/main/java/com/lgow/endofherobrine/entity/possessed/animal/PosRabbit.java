package com.lgow.endofherobrine.entity.possessed.animal;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedAnimal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class PosRabbit extends Rabbit implements NeutralMob, PossessedAnimal {
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);
	@Nullable private UUID persistentAngerTarget;
	private int remainingPersistentAngerTime, possessionTimer;

	public PosRabbit(EntityType<? extends PosRabbit> type, Level level) { super(type, level); }

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 3D).add(Attributes.MOVEMENT_SPEED, 0.3).add(
				Attributes.FOLLOW_RANGE, 40).add(Attributes.ATTACK_DAMAGE, 0.5D);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("entities/rabbit"); }

	@Override
	protected void registerGoals() {
		this.registerPosAnimalGoals(this, 2.2D);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide) {
			this.updatePersistentAnger((ServerLevel) this.level(), true);
this.possessionTimer++;
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		this.addPersistentAngerSaveData(pCompound);
		this.addPossessionSavedData(pCompound, possessionTimer);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.readPersistentAngerSaveData(this.level(), pCompound);
		this.readPossessionSaveData(pCompound);
	}

	@Override
	protected SoundEvent getAmbientSound() { return null; }

	public Rabbit getBreedOffspring(ServerLevel server, AgeableMob mob) {
		return EntityInit.P_RABBIT.get().create(server);
	}

	private void spawnLingeringCloud() {
		Collection<MobEffectInstance> collection = this.getActiveEffects();
		if (!collection.isEmpty()) {
			AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
			areaeffectcloud.setRadius(2.5F);
			areaeffectcloud.setRadiusOnUse(-0.5F);
			areaeffectcloud.setWaitTime(10);
			areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
			areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
			for (MobEffectInstance mobeffectinstance : collection) {
				areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
			}
			this.level().addFreshEntity(areaeffectcloud);
		}
	}

	private void explode() {
		if (!this.level().isClientSide) {
			this.dead = true;
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3f, Level.ExplosionInteraction.MOB);
			this.discard();
			this.spawnLingeringCloud();
		}
	}

	@Override
	public void dropAllDeathLoot(DamageSource source) {
		super.dropAllDeathLoot(source);
		if (source.getEntity() instanceof Player || this.lastHurtByPlayer != null) { this.explode(); }
	}

	@Override
	public MobType getMobType() { return ModMobTypes.POSSESSED; }

	@Override
	public int getRemainingPersistentAngerTime() { return this.remainingPersistentAngerTime; }

	@Override
	public void setRemainingPersistentAngerTime(int pTime) { this.remainingPersistentAngerTime = pTime; }

	@Nullable
	@Override
	public UUID getPersistentAngerTarget() { return this.persistentAngerTarget; }

	@Override
	public void setPersistentAngerTarget(@Nullable UUID pTarget) { this.persistentAngerTarget = pTarget; }
	@Override
	public void startPersistentAngerTimer() {
		this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
	}
	@Override
	public void setPossessionTimer(int possessionTimer) {
		this.possessionTimer = possessionTimer;
	}

	@Override
	public int getPossessionTimer() {
		return possessionTimer;
	}
}
