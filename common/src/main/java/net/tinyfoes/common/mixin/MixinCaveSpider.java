package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CaveSpider.class)
public abstract class MixinCaveSpider extends Monster {
	protected MixinCaveSpider(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public double getEyeY() {
		return isBaby() ? 0.93F : super.getEyeY() * 0.9;
	}
}
