package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Ravager.class)
public abstract class MixinRavager extends Raider {
	protected MixinRavager(EntityType<? extends Raider> entityType, Level level) {
		super(entityType, level);
	}
	//todo passanger offset
	@Override
	protected Vector3f getPassengerAttachmentPoint(Entity entity, EntityDimensions entityDimensions, float f) {
		float f1 = isBaby() ? 0.0625F : 2.0F;
		return new Vector3f(0.0F, entityDimensions.height + f1 * f, -f1 * f);
	}
}
