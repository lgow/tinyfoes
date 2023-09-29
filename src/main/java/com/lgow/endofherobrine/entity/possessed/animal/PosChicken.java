package com.lgow.endofherobrine.entity.possessed.animal;

import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.ModMobTypes;
import com.lgow.endofherobrine.entity.PossessedAnimal;
import com.lgow.endofherobrine.entity.Teleporter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PosChicken extends Chicken implements NeutralMob, PossessedAnimal, Teleporter {
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 80);
	private @Nullable UUID persistentAngerTarget;
	private int remainingPersistentAngerTime, possessionTimer;

	public PosChicken(EntityType<? extends PosChicken> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.5)
				.add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ATTACK_DAMAGE, 1.0);
	}

	@Override
	public MobType getMobType() {
		return ModMobTypes.POSSESSED;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() { return new ResourceLocation("minecraft", "entities/chicken"); }

	@Override
	protected void registerGoals() {
		this.registerPosAnimalGoals(this, 3.0D, 1.4D);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide) {
			this.updatePersistentAnger((ServerLevel) this.level(), true);
		}
	}

	protected SoundEvent getAmbientSound() {
		return null;
	}

	public Chicken getBreedOffspring(ServerLevel server, AgeableMob mob) {
		return EntityInit.P_CHICKEN.get().create(server);
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


	protected void teleportToDodge() {
		if (!this.level().isClientSide && this.isAlive()) {
			double x = this.getX() + (this.random.nextDouble() - 0.5) * 10.0;
			double y = this.getY() + (double) this.random.nextInt(12);
			double z = this.getZ() + (this.random.nextDouble() - 0.5) * 10.0;
			if (this.blockPosition().distToCenterSqr(x, y, z) > 3.0) {
				this.attemptTeleport(this, x, y, z);
			}
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!this.level().isClientSide && source.getEntity() instanceof Player && amount < this.getHealth()) {
			this.teleportToDodge();
		}
		return super.hurt(source, amount);
	}

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
}