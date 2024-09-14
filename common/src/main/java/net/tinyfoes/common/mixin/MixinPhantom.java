package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Phantom.class)
public abstract class MixinPhantom extends FlyingMob {
	protected MixinPhantom(EntityType<? extends FlyingMob> entityType, Level level) {
		super(entityType, level);
	}



}
