package net.tinyallies.common.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.tinyallies.common.entity.BabyfiableEntity;
import net.tinyallies.common.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MixinMob extends LivingEntity implements BabyfiableEntity {
	@Unique private static EntityDataAccessor<Boolean> DATA_BABYFIED_ID = SynchedEntityData.defineId(MixinMob.class,
			EntityDataSerializers.BOOLEAN);
	@Unique private static EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(MixinMob.class,
			EntityDataSerializers.BOOLEAN);

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
		this.$setBabyfied(compoundTag.getBoolean("IsBabyfied"));
		this.setBaby(compoundTag.getBoolean("IsBaby"));
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
		if (DATA_BABYFIED_ID.equals(entityDataAccessor)) {
			this.refreshDimensions();
		}
		if (DATA_BABY_ID.equals(entityDataAccessor)) {
			this.refreshDimensions();
		}
		super.onSyncedDataUpdated(entityDataAccessor);
	}

	@Inject(method = "aiStep", at = @At("HEAD"))
	public void aiStep(CallbackInfo ci) {
		if (!this.level.isClientSide) {
			this.$setBabyfied(this.hasEffect(ModEffects.BABYFICATION.get()));
		}
	}

	public boolean $isBabyfied() {
		return this.getEntityData().get(DATA_BABYFIED_ID);
	}

	@Override
	public boolean $isBaby() {
		return this.getEntityData().get(DATA_BABY_ID);
	}

	@Override
	public void $setBaby(boolean b) {
		this.getEntityData().set(DATA_BABY_ID, b);
		if (this.level != null && !this.level.isClientSide) {
			AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
			AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
			AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
			movementSpeed.removeModifier(SPEED_MODIFIER_BABY);
			attackDamage.removeModifier(ATTACK_MODIFIER_BABY);
			maxHealth.removeModifier(HEALTH_MODIFIER_BABY);
			if (b && !$isBabyfied()) {
				movementSpeed.addTransientModifier(SPEED_MODIFIER_BABY);
				attackDamage.addTransientModifier(ATTACK_MODIFIER_BABY);
				maxHealth.addTransientModifier(HEALTH_MODIFIER_BABY);
			}
		}
	}

	@Override
	public void $setBabyfied(boolean b) {
		this.getEntityData().set(DATA_BABYFIED_ID, b);
		if (this.level != null && !this.level.isClientSide) {
			AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			attributeInstance.removeModifier(SPEED_MODIFIER_BABY);
			if (b && !$isBaby()) {
				attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
			}
		}
	}
}



