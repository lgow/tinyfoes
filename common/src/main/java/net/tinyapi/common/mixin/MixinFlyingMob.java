package net.tinyapi.common.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.tinyapi.common.entity.BabyfiableEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlyingMob.class)
public abstract class MixinFlyingMob extends Mob implements BabyfiableEntity {
	protected MixinFlyingMob(EntityType<? extends Mob> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public boolean isBaby() {
		return tinyapi$$isBaby() || tinyapi$$isBabyfied();
	}

	@Override
	public void setBaby(boolean b) {
		this.tinyapi$$setBaby(b);
	}

	@Override
	public int getExperienceReward() {
		if (isBaby()) {
			this.xpReward = (int) ((double) this.xpReward * 2.5);
		}
		return super.getExperienceReward();
	}
}
