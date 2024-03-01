package net.tinyfoes.common.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Ghast.class)
public abstract class MixinGhast extends FlyingMob {
	protected MixinGhast(EntityType<? extends FlyingMob> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
	return isBaby() ? 1.3f : 2.6f;
	}
}
