package net.tinyallies.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.tinyallies.entity.BabyfiableEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Zombie.class)
public abstract class MixinMob extends Monster{
	protected MixinMob(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public boolean isBaby() {
		return super.isBaby();
	}
}



