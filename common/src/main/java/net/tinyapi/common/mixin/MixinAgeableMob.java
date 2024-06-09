package net.tinyapi.common.mixin;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.tinyapi.common.entity.BabyfiableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AgeableMob.class)
public abstract class MixinAgeableMob extends Mob implements BabyfiableEntity {
	protected MixinAgeableMob(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract int getAge();

	@Override
	public boolean isBaby() {
		return tinyapi$$isBaby() || tinyapi$$isBabyfied();
	}

	@Inject(method = "isBaby", at = @At("RETURN"), cancellable = true)
	public void getMyRidingOffset(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(cir.getReturnValue() || tinyapi$$isBabyfied());
	}

	@Override
	public boolean tinyapi$$isBaby() {
		return this.getAge() < 0;
	}
}



