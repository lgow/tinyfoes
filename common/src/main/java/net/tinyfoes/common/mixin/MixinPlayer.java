package net.tinyfoes.common.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements BabyfiableEntity {
	@Unique private static final EntityDataAccessor<Boolean> DATA_IS_BABY = SynchedEntityData.defineId(
			MixinPlayer.class, EntityDataSerializers.BOOLEAN);
	@Unique private static final EntityDataAccessor<Boolean> DATA_IS_BABYFIED = SynchedEntityData.defineId(
			MixinPlayer.class, EntityDataSerializers.BOOLEAN);
	@Shadow @Final public static EntityDimensions STANDING_DIMENSIONS;
	@Shadow @Final private static Map<Pose, EntityDimensions> POSES;

	protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract int getPortalWaitTime();

//	@Override
//	public EntityDimensions getDimensions(Pose pose) {
//		return POSES.getOrDefault(pose, STANDING_DIMENSIONS).scale(this.getScale());
//	}

//	@Inject(method = "getDimensions", at = @At("RETURN"))
//	void s(Pose pose, CallbackInfoReturnable<EntityDimensions> cir){
//		cir.setReturnValue();
//	}

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
	void aiStep(CallbackInfo ci) {
		this.$setBabyfied(this.hasEffect(ModEffects.BABYFICATION.get()));
	}

	@Unique
	public void $setBaby(boolean bl) {
		this.entityData.set(DATA_IS_BABY, bl);
		if (this.level != null && !this.level.isClientSide) {
			AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			attributeInstance.removeModifier(SPEED_MODIFIER_BABY);
			if (bl && !$isBabyfied()) {
				attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
			}
		}
	}

	@Override
	public boolean $isBaby() {
		return this.entityData.get(DATA_IS_BABY);
	}

	@Override
	public void $setBabyfied(boolean bl) {
		this.entityData.set(DATA_IS_BABYFIED, bl);
		if (this.level != null && !this.level.isClientSide) {
			AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			attributeInstance.removeModifier(SPEED_MODIFIER_BABY);
			if (bl && !$isBaby()) {
				attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
			}
		}
	}

	@Override
	public boolean $isBabyfied() {
		return this.entityData.get(DATA_IS_BABYFIED);
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
	private void s(CallbackInfo ci) {
		this.entityData.define(DATA_IS_BABY, false);
		this.entityData.define(DATA_IS_BABYFIED, false);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
	private void ss(CompoundTag pCompound, CallbackInfo ci) {
		if (pCompound.contains("IsBaby")) {
			this.entityData.set(DATA_IS_BABY, pCompound.getBoolean("IsBaby"));
		}
		if (pCompound.contains("IsBabyfied")) {
			this.entityData.set(DATA_IS_BABYFIED, pCompound.getBoolean("IsBabyfied"));
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
	private void sss(CompoundTag pCompound, CallbackInfo ci) {
		pCompound.putBoolean("IsBaby", this.getEntityData().get(DATA_IS_BABY));
		pCompound.putBoolean("IsBabyfied", this.getEntityData().get(DATA_IS_BABYFIED));
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
		if (DATA_IS_BABY.equals(entityDataAccessor) || DATA_IS_BABYFIED.equals(entityDataAccessor)) {
			this.refreshDimensions();
		}
		super.onSyncedDataUpdated(entityDataAccessor);
	}
}
