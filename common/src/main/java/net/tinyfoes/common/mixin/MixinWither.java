package net.tinyfoes.common.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.tinyfoes.common.entity.BabyfiableEntity;
import net.tinyfoes.common.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitherBoss.class)
public abstract class MixinWither extends Monster implements BabyfiableEntity {

	protected MixinWither(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "getHeadX", at = @At("HEAD"), cancellable = true)
	private void getHeadX(int i, CallbackInfoReturnable<Double> cir) {
		if(this.isBaby()) {
			if (i <= 0) {
				cir.setReturnValue(this.getX() - 0.05);
			}
			else {
				float f = (this.yBodyRot + (float) (180 * (i - 1))) * 0.08726646F;
				float g = Mth.cos(f);
				cir.setReturnValue(this.getX() + (double) g * 1.3);
			}
		}
	}

	@Inject(method = "getHeadY", at = @At("HEAD"), cancellable = true)
	private void getHeadY(int i, CallbackInfoReturnable<Double> cir) {
		if(this.isBaby()) {
			cir.setReturnValue(i <= 0 ? this.getY() + 1.6 : this.getY() + 1.35);
		}
	}
}
