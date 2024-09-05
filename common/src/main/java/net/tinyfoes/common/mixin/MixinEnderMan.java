package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnderMan.class)
public abstract class MixinEnderMan extends Monster {
	protected MixinEnderMan(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
		return this.isBaby() ? 1.33F : super.getStandingEyeHeight(pose, entityDimensions);
	}

}
