package net.tinyfoes.common.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Blaze.class)
public abstract class MixinBlaze extends Monster {
	protected MixinBlaze(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "aiStep", at = @At("HEAD"))
	private void aiStep(CallbackInfo ci) {
		if (!this.onGround && this.getDeltaMovement().y < 0.0) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
		}
		if (this.level.isClientSide) {
			if (this.random.nextInt(24) == 0 && !this.isSilent()) {
				this.level.playLocalSound(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5,
						SoundEvents.BLAZE_BURN, this.getSoundSource(), 1.0f + this.random.nextFloat(),
						this.random.nextFloat() * 0.7f + 0.3f, false);
			}
			if (this.isBaby()) {
				this.level.addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5), this.getRandomY(),
						this.getRandomZ(0.5), 0.0, 0.0, 0.0);
			}
			else {
				for (int i = 0; i < 2; ++i) {
					this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0,
							0.0, 0.0);
				}
			}
		} super.aiStep();
	}
}
