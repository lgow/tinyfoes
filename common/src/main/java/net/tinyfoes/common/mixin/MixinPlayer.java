package net.tinyfoes.common.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tinyfoes.common.config.TinyFoesConfigs;
import net.tinyfoes.common.entity.BabyfiableEntity;
import net.tinyfoes.common.entity.ModEntityTypeTags;
import net.tinyfoes.common.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements BabyfiableEntity {
	@Unique private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(
			MixinPlayer.class, EntityDataSerializers.BOOLEAN);
	@Unique private static final EntityDataAccessor<Boolean> DATA_BABYFIED_ID = SynchedEntityData.defineId(
			MixinPlayer.class, EntityDataSerializers.BOOLEAN);
	@Unique AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(
			UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836"), "Baby speed boost",
			TinyFoesConfigs.BABY_SPEED_MODIFIER.get(), AttributeModifier.Operation.MULTIPLY_BASE);
	@Unique AttributeModifier HEALTH_MODIFIER_BABY = new AttributeModifier(
			UUID.fromString("B9766B57-9566-4402-BC1F-2EE2A276D836"), "Baby health boost",
			TinyFoesConfigs.BABY_MAX_HEALTH_MODIFIER.get(), AttributeModifier.Operation.MULTIPLY_BASE);

	protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract int getPortalWaitTime();

	@Shadow
	public abstract void playSound(SoundEvent soundEvent, float f, float g);

	@Inject(method = "getDimensions", at = @At("RETURN"), cancellable = true)
	public void getDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
		cir.setReturnValue(cir.getReturnValue().scale(this.getScale()));
	}

	@Inject(method = "getStandingEyeHeight", at = @At("RETURN"), cancellable = true)
	public void getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(cir.getReturnValue() * this.getScale());
	}

	@Inject(method = "serverAiStep", at = @At("HEAD"))
	void serverAiStep(CallbackInfo ci) {
		if (!this.level().isClientSide) {
			this.tinyfoes$$setBabyfied(this.hasEffect(ModEffects.BABYFICATION.get()));
		}
	}

	@Unique
	public void tinyfoes$$setBaby(boolean bl) {
		if (!this.getType().is(ModEntityTypeTags.BABYFICATION_BLACKLIST) && !this.tinyfoes$$isBabyfied()) {
			this.entityData.set(DATA_BABY_ID, bl);
			if (this.level() != null && !this.level().isClientSide) {
				AttributeInstance speedAttribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
				AttributeInstance maxHealthAttribute = this.getAttribute(Attributes.MAX_HEALTH);
				maxHealthAttribute.removeModifier(HEALTH_MODIFIER_BABY);
				speedAttribute.removeModifier(SPEED_MODIFIER_BABY);
				if (bl) {
					speedAttribute.addTransientModifier(SPEED_MODIFIER_BABY);
					maxHealthAttribute.addTransientModifier(HEALTH_MODIFIER_BABY);
				}
			}
		}
	}

	@Override
	public void tinyfoes$$setBabyfied(boolean bl) {
		if (!this.getType().is(ModEntityTypeTags.BABYFICATION_BLACKLIST) && !tinyfoes$$isBaby()) {
			this.entityData.set(DATA_BABYFIED_ID, bl);
			if (this.level() != null && !this.level().isClientSide) {
				AttributeInstance speedAttribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
				AttributeInstance maxHealthAttribute = this.getAttribute(Attributes.MAX_HEALTH);
				maxHealthAttribute.removeModifier(HEALTH_MODIFIER_BABY);
				speedAttribute.removeModifier(SPEED_MODIFIER_BABY);
				if (bl) {
					speedAttribute.addTransientModifier(SPEED_MODIFIER_BABY);
					maxHealthAttribute.addTransientModifier(HEALTH_MODIFIER_BABY);
				}
			}
		}
	}

	@Override
	public boolean tinyfoes$$isBaby() {
		return this.entityData.get(DATA_BABY_ID);
	}

	@Override
	public boolean tinyfoes$$isBabyfied() {
		return this.entityData.get(DATA_BABYFIED_ID);
	}

	@Override
	public boolean isBaby() {
		return tinyfoes$$isBaby() || tinyfoes$$isBabyfied();
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
			this.tinyfoes$$setBaby(pCompound.getBoolean("IsBaby"));
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
		if (DATA_BABY_ID.equals(entityDataAccessor)) {
			this.refreshDimensions();
			if (this.level().isClientSide && this.tickCount > 20) {
				if (!tinyfoes$$isBaby()) {
					if (!tinyfoes$$isBabyfied()) {
						this.level().playSound((Player) (Object) this, this.blockPosition(),
								SoundEvents.ARMOR_EQUIP_TURTLE, SoundSource.PLAYERS, 1.0F, 1.0F);
					}
				}
				else if (!tinyfoes$$isBabyfied()) {
					this.level().playSound((Player) (Object) this, this.blockPosition(),
							SoundEvents.PUFFER_FISH_BLOW_UP, SoundSource.PLAYERS, 1.0F, 1.0F);
				}
			}
		}
		if (DATA_BABYFIED_ID.equals(entityDataAccessor)) {
			this.refreshDimensions();
			if (this.level().isClientSide && this.tickCount > 20) {
				if (!tinyfoes$$isBabyfied()) {
					if (!tinyfoes$$isBaby()) {
						this.level().playSound((Player) (Object) this, this.blockPosition(),
								SoundEvents.ARMOR_EQUIP_TURTLE, SoundSource.PLAYERS, 1.0F, 1.0F);
					}
				}
				else if (!tinyfoes$$isBaby()) {
					this.level().playSound((Player) (Object) this, this.blockPosition(),
							SoundEvents.PUFFER_FISH_BLOW_UP, SoundSource.PLAYERS, 1.0F, 1.0F);
				}
			}
		}
		super.onSyncedDataUpdated(entityDataAccessor);
	}
}
