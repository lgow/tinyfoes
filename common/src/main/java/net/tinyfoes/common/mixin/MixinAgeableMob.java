package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.tinyfoes.common.entity.BabyfiableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AgeableMob.class)
public abstract class MixinAgeableMob extends Mob implements BabyfiableEntity {
	protected MixinAgeableMob(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract int getAge();

	@Override
	public boolean isBaby() {
		return this.getAge() < 0 || $isBabyfied();
	}

}



