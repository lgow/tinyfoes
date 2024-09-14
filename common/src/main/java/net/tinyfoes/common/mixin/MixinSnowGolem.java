package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SnowGolem.class)
public class MixinSnowGolem extends AbstractGolem {
	protected MixinSnowGolem(EntityType<? extends AbstractGolem> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public double getEyeY() {
		return isBaby() ? 0.760526F : 1.7;
	}
}
