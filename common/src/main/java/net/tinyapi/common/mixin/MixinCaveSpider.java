package net.tinyapi.common.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CaveSpider.class)
public abstract class MixinCaveSpider extends Monster {
	protected MixinCaveSpider(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "getStandingEyeHeight", at = @At("HEAD"), cancellable = true)
	public void getMyRidingOffset(Pose pose, EntityDimensions entityDimensions, CallbackInfoReturnable<Float> cir) {
		if (isBaby()) {
			cir.setReturnValue(entityDimensions.height * 0.9F);
		}
	}
}
