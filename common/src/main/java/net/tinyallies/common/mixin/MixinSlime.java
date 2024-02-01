package net.tinyallies.common.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.tinyallies.common.entity.BabyfiableEntity;
import net.tinyallies.common.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public abstract class MixinSlime extends Mob implements BabyfiableEntity {
	@Unique private static EntityDataAccessor<Boolean> DATA_BABYFIED_ID = SynchedEntityData.defineId(MixinSlime.class,
			EntityDataSerializers.BOOLEAN);

	public MixinSlime(EntityType<? extends Horse> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "getSize", at = @At("HEAD"), cancellable = true)
	void isTiny(CallbackInfoReturnable<Integer> cir) {
		if($isBabyfied()) {
			cir.setReturnValue(1);
		}
	}

	public boolean $isBabyfied() {
		return this.getEntityData().get(DATA_BABYFIED_ID);
	}

	@Override
	public void $setBabyfied(boolean b) {
		this.getEntityData().set(DATA_BABYFIED_ID, b);
		if (this.level != null && !this.level.isClientSide) {
			AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			attributeInstance.removeModifier(SPEED_MODIFIER_BABY);
			if (b) {
				attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
			}
		}
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	protected void defineSynchedData(CallbackInfo ci) {
		this.getEntityData().define(DATA_BABYFIED_ID, false);
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
		compoundTag.putBoolean("IsBabyfied", this.getEntityData().get(DATA_BABYFIED_ID));
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
		this.$setBabyfied(compoundTag.getBoolean("IsBabyfied"));
	}

	@Inject(method = "onSyncedDataUpdated", at = @At("TAIL"))
	public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor, CallbackInfo ci) {
		if (DATA_BABYFIED_ID.equals(entityDataAccessor)) {
			this.refreshDimensions();
		}
	}

	@Override
	protected void customServerAiStep() {
		this.$setBabyfied(this.hasEffect(ModEffects.BABYFICATION.get()));
		super.customServerAiStep();
	}
}
