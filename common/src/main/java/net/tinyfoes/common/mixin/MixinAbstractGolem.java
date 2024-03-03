package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.level.Level;
import net.tinyfoes.common.entity.BabyfiableEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractGolem.class)
public abstract class MixinAbstractGolem extends PathfinderMob implements BabyfiableEntity {

	protected MixinAbstractGolem(EntityType<? extends PathfinderMob> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public boolean isBaby() {
		return $isBaby() || $isBabyfied();
	}

}