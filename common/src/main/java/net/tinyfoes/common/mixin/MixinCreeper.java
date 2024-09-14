package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Creeper.class)
public abstract class MixinCreeper extends Monster {
	protected MixinCreeper(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public double getEyeY() {
		return isBaby() ? 0.8F : super.getEyeY();
	}
}
