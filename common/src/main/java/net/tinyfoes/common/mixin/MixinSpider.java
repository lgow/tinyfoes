package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Spider.class)
public abstract class MixinSpider extends Monster {
	protected MixinSpider(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public double getEyeY() {
		return isBaby() ? super.getEyeY() * 0.9 : super.getEyeY();
	}
}
