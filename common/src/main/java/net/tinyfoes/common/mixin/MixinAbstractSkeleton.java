package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractSkeleton.class)
public abstract class MixinAbstractSkeleton extends Monster {
	protected MixinAbstractSkeleton(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
		return this.isBaby() ? 0.93F : super.getStandingEyeHeight(pose, entityDimensions);
	}

	@Override
	public double getMyRidingOffset() {
		return isBaby() ? -0.2 : -0.6;
	}
}
