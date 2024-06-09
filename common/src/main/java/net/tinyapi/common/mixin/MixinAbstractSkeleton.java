package net.tinyapi.common.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeleton.class)
public abstract class MixinAbstractSkeleton extends Monster {
	protected MixinAbstractSkeleton(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "getStandingEyeHeight", at = @At("HEAD"), cancellable = true)
	public void getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions, CallbackInfoReturnable<Float> cir) {
		if(this.isBaby()){
			cir.setReturnValue(0.93F);
		}
	}

	@Inject(method = "getMyRidingOffset", at = @At("HEAD"), cancellable = true)
	public void getMyRidingOffset(CallbackInfoReturnable<Double> cir) {
		if (isBaby()) {
			cir.setReturnValue(-0.2);
		}
	}
}
