package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SkeletonHorse.class)
public abstract class MixinSkeletonHorse extends AbstractHorse {
	public MixinSkeletonHorse(EntityType<? extends Horse> entityType, Level level) {
		super(entityType, level);
	}


}
