package net.tinyallies.forge.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tinyallies.common.entity.BabyfiableEntity;
import net.tinyallies.common.registry.ModEffects;
import net.tinyallies.forge.capabilities.CapabilityProvider;
import net.tinyallies.forge.capabilities.IsBaby;
import net.tinyallies.forge.capabilities.IsBabyfied;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements BabyfiableEntity {
	@Shadow @Final public static EntityDimensions STANDING_DIMENSIONS;
	@Shadow @Final private static Map<Pose, EntityDimensions> POSES;

	protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

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

	@Inject(method = "aiStep", at = @At("HEAD"))
	void serverAiStep(CallbackInfo ci) {
		this.$setBabyfied(this.hasEffect(ModEffects.BABYFICATION.get()));
	}

	@Override
	public boolean isBaby() {
		return $isBaby() || $isBabyfied();
	}

	@Unique
	public void $setBaby(boolean bl) {
		this.getCapability(CapabilityProvider.IS_BABY_CAPABILITY).ifPresent((isBaby) -> isBaby.setValue(bl, this));
		if (this.level != null && !this.level.isClientSide) {
			AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			attributeInstance.removeModifier(SPEED_MODIFIER_BABY);
			if (bl) {
				attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
			}
		}
		this.refreshDimensions();
	}

	@Override
	public boolean $isBaby() {
		Optional<IsBaby> resolve = this.getCapability(CapabilityProvider.IS_BABY_CAPABILITY).resolve();
		return resolve.isPresent() && resolve.get().getValue();
	}

	@Override
	public boolean $isBabyfied() {
		Optional<IsBabyfied> resolve = this.getCapability(CapabilityProvider.IS_BABYFIED_CAPABILITY).resolve();
		return resolve.isPresent() && resolve.get().getValue();
	}

	@Override
	public void $setBabyfied(boolean bl) {
		this.getCapability(CapabilityProvider.IS_BABYFIED_CAPABILITY).ifPresent(
				(isBabyfied) -> isBabyfied.setValue(bl, this));
		if (this.level != null && !this.level.isClientSide) {
			AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
			attributeInstance.removeModifier(SPEED_MODIFIER_BABY);
			if (bl) {
				attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
			}
		}
		this.refreshDimensions();
	}

	@Override
	public double getMyRidingOffset() {
		return isBaby() ? 0.0F : -0.35;
	}
	//	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	//	public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
	//		compoundTag.putBoolean("IsBaby", this.$isBaby());
	//		compoundTag.putBoolean("IsBabyfied", this.$isBabyfied());
	//	}
	//
	//	@Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
	//	public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
	//		this.$setBaby(compoundTag.getBoolean("IsBaby"));
	//		this.$setBabyfied(compoundTag.getBoolean("IsBabyfied"));
	//	}
}
