package net.tinyallies.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
	@Shadow @Final private static Map<Pose, EntityDimensions> POSES;
	@Shadow @Final public static EntityDimensions STANDING_DIMENSIONS;
	protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return POSES.getOrDefault(pose, STANDING_DIMENSIONS).scale(this.getScale());
	}

	@Override
	public float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
		switch (pose) {
			case SWIMMING, FALL_FLYING, SPIN_ATTACK -> {
				return 0.4f * this.getScale();
			}
			case CROUCHING -> {
				return 1.27f * this.getScale();
			}
		}
		return 1.62f * this.getScale();
	}

	@Override
	public boolean isBaby() {
		return true;
//		return getActiveEffects() !=null && this.hasEffect(ModEffects.BABYFICATION);
	}
}
