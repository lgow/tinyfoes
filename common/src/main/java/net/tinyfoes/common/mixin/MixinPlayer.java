package net.tinyfoes.common.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tinyfoes.common.entity.BabyfiableEntity;
import net.tinyfoes.common.registry.ModEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements BabyfiableEntity {
	@Unique private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(
			MixinPlayer.class, EntityDataSerializers.BOOLEAN);
	@Unique private static final EntityDataAccessor<Boolean> DATA_BABYFIED_ID = SynchedEntityData.defineId(
			MixinPlayer.class, EntityDataSerializers.BOOLEAN);
	@Shadow @Final public static EntityDimensions STANDING_DIMENSIONS;
	@Shadow @Final private static Map<Pose, EntityDimensions> POSES;

	protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract int getPortalWaitTime();

	@Shadow
	public abstract void playSound(SoundEvent soundEvent, float f, float g);

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return POSES.getOrDefault(pose, STANDING_DIMENSIONS).scale(this.getScale());
	}

	@Override
	public float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
		switch (pose) {
			case SWIMMING, FALL_FLYING, SPIN_ATTACK -> {
				return 0.4f * this.getScale();
			}
			case CROUCHING -> {
				return 1.27f * this.getScale();
			}
		}
		return 1.62f * this.getScale();
	}

	@Inject(method = "serverAiStep", at = @At("HEAD"))
	void serverAiStep(CallbackInfo ci) {
		if (!this.level.isClientSide) {
			this.$setBabyfied(this.hasEffect(ModEffects.BABYFICATION.get()));
		}
	}

	@Unique
	public void $setBaby(boolean bl) {
		this.entityData.set(DATA_BABY_ID, bl);
		if (this.level != null && !this.level.isClientSide) {
			AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			if (!$isBabyfied()) {
				attributeInstance.removeModifier(SPEED_MODIFIER_BABY);
				if (bl) {
					attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
				}
			}
		}
	}

	@Override
	public void $setBabyfied(boolean bl) {
		this.entityData.set(DATA_BABYFIED_ID, bl);
		if (this.level != null && !this.level.isClientSide) {
			AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			if (!$isBaby()) {
				attributeInstance.removeModifier(SPEED_MODIFIER_BABY);
				if (bl) {
					attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
				}
			}
		}
	}

	@Override
	public boolean $isBaby() {
		return this.entityData.get(DATA_BABY_ID);
	}

	@Override
	public boolean $isBabyfied() {
		return this.entityData.get(DATA_BABYFIED_ID);
	}

	@Override
	public boolean isBaby() {
		return $isBaby() || $isBabyfied();
	}

	@Override
	public double getMyRidingOffset() {
		return isBaby() ? 0.0F : -0.35;
	}

	@Inject(method = "defineSynchedData", at = @At("HEAD"))
	private void defineSynchedData(CallbackInfo ci) {
		this.entityData.define(DATA_BABY_ID, false);
		this.entityData.define(DATA_BABYFIED_ID, false);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
	private void readAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
		if (pCompound.contains("IsBaby")) {
			this.entityData.set(DATA_BABY_ID, pCompound.getBoolean("IsBaby"));
		}
		if (pCompound.contains("IsBabyfied")) {
			this.entityData.set(DATA_BABYFIED_ID, pCompound.getBoolean("IsBabyfied"));
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
	private void addAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
		pCompound.putBoolean("IsBaby", this.getEntityData().get(DATA_BABY_ID));
		pCompound.putBoolean("IsBabyfied", this.getEntityData().get(DATA_BABYFIED_ID));
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
		if (DATA_BABY_ID.equals(entityDataAccessor) || DATA_BABYFIED_ID.equals(entityDataAccessor)) {
			this.refreshDimensions();
			if (this.level.isClientSide) {
				if (!isBaby()) {
					this.playSound(SoundEvents.ARMOR_EQUIP_TURTLE, 1, 1);
				}
				else {
					this.playSound(SoundEvents.PUFFER_FISH_BLOW_UP, 1, 1);
				}
			}
		}
		super.onSyncedDataUpdated(entityDataAccessor);
	}
}
