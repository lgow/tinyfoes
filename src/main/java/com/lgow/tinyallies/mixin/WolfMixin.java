package com.lgow.tinyallies.mixin;

import com.lgow.tinyallies.entity.BabyMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public abstract class WolfMixin {

	@Inject(method = "wantsToAttack", at = @At("HEAD"), cancellable = true)
	private void wantsToAttack(LivingEntity pTarget, LivingEntity pOwner, CallbackInfoReturnable<Boolean> cir) {
		if (pTarget instanceof BabyMonster ownable) {
			cir.setReturnValue(ownable.getOwner() != pOwner);
		}
	}
}
