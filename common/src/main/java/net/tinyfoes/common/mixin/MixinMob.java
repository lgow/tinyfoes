package net.tinyfoes.common.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
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

import java.util.UUID;

@Mixin(Mob.class)
public abstract class MixinMob extends LivingEntity implements BabyfiableEntity {
	@Unique private static final EntityDataAccessor<Boolean> DATA_BABYFIED_ID = SynchedEntityData.defineId(
			MixinMob.class, EntityDataSerializers.BOOLEAN);
	@Unique private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(MixinMob.class,
			EntityDataSerializers.BOOLEAN);
	@Unique AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(
			UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836"), "Baby speed boost",
			TinyFoesConfigs.BABY_MAX_HEALTH_MODIFIER.get(), AttributeModifier.Operation.MULTIPLY_BASE);
	@Unique AttributeModifier HEALTH_MODIFIER_BABY = new AttributeModifier(
			UUID.fromString("B9766B57-9566-4402-BC1F-2EE2A276D836"), "Baby health boost",
			TinyFoesConfigs.BABY_MAX_HEALTH_MODIFIER.get(), AttributeModifier.Operation.MULTIPLY_BASE);

	protected MixinMob(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract void setBaby(boolean bl);

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	public void defineSynchedData(CallbackInfo ci) {
		this.getEntityData().define(DATA_BABYFIED_ID, false);
		this.getEntityData().define(DATA_BABY_ID, false);
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
		compoundTag.putBoolean("IsBabyfied", this.getEntityData().get(DATA_BABYFIED_ID));
		compoundTag.putBoolean("IsBaby", this.getEntityData().get(DATA_BABY_ID));
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
		this.tinyfoes$$setBabyfied(compoundTag.getBoolean("IsBabyfied"));
		this.setBaby(compoundTag.getBoolean("IsBaby"));
	}

	public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
		if (DATA_BABY_ID.equals(entityDataAccessor)) {
			this.refreshDimensions();
			if (this.level().isClientSide && this.tickCount > 20) {
				if (!tinyfoes$$isBaby()) {
					if (!tinyfoes$$isBabyfied()) {
						this.playSound(SoundEvents.ARMOR_EQUIP_TURTLE);
					}
				}
				else if (!tinyfoes$$isBabyfied()) {
					this.playSound(SoundEvents.PUFFER_FISH_BLOW_UP);
				}
			}
		}
		if (DATA_BABYFIED_ID.equals(entityDataAccessor)) {
			this.refreshDimensions();
			if (this.tickCount > 20) {
				if (!tinyfoes$$isBabyfied()) {
					if (!tinyfoes$$isBaby()) {
						this.playSound(SoundEvents.ARMOR_EQUIP_TURTLE);
					}
				}
				else if (!tinyfoes$$isBaby()) {
					this.playSound(SoundEvents.PUFFER_FISH_BLOW_UP);
				}
			}
		}
		super.onSyncedDataUpdated(entityDataAccessor);
	}

	@Inject(method = "aiStep", at = @At("HEAD"))
	public void aiStep(CallbackInfo ci) {
		if (!this.level().isClientSide) {
			this.tinyfoes$$setBabyfied(this.hasEffect(ModEffects.BABYFICATION.get()));
		}
	}

	public boolean tinyfoes$$isBabyfied() {
		return this.getEntityData().get(DATA_BABYFIED_ID);
	}

	@Override
	public boolean tinyfoes$$isBaby() {
		return this.getEntityData().get(DATA_BABY_ID);
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
}



