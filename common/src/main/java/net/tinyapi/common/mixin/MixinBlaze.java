package net.tinyapi.common.mixin;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Blaze.class)
public abstract class MixinBlaze extends Monster {
	protected MixinBlaze(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@ModifyArg(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
	private ParticleOptions aiStepAddParticle(ParticleOptions arg) {
		return this.random.nextInt(10) == 0 ? ParticleTypes.LARGE_SMOKE : ParticleTypes.SMOKE;
	}
}
