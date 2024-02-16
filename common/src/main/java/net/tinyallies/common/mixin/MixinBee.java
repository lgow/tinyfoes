package net.tinyallies.common.mixin;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.tinyallies.common.entity.BabyfiableEntity;
import net.tinyallies.common.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class MixinBee extends AgeableMob implements BabyfiableEntity {
	protected MixinBee(EntityType<? extends AgeableMob> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "customServerAiStep", at = @At("HEAD"))
	protected void customServerAiStep(CallbackInfo ci) {
		this.$setBabyfied(this.hasEffect(ModEffects.BABYFICATION.get()));
	}
}
