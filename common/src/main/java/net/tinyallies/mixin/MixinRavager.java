package net.tinyallies.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Ravager.class)
public abstract class MixinRavager extends Raider {
	protected MixinRavager(EntityType<? extends Raider> entityType, Level level) {
		super(entityType, level);
	}

//	@Override
//	public double getPassengersRidingOffset() {
//		return isBaby() ? this.getBbHeight() * 1.5f : 2.1;
//	}
}
