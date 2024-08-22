package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Phantom.class)
public abstract class MixinPhantom extends FlyingMob {
	protected MixinPhantom(EntityType<? extends FlyingMob> entityType, Level level) {
		super(entityType, level);
	}

	@Redirect(method = "tick",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Phantom;getPhantomSize()I"))
	private int injected(Phantom instance) {
		return instance.getPhantomSize() + (isBaby() ? -3 : 0);
	}
}
