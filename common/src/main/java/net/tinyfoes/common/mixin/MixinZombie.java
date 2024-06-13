package net.tinyfoes.common.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.tinyfoes.common.entity.BabyfiableEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Zombie.class)
public abstract class MixinZombie extends Monster implements BabyfiableEntity {
	@Shadow @Final private static EntityDataAccessor<Boolean> DATA_BABY_ID;

	protected MixinZombie(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public boolean isBaby() {
		return this.getEntityData().get(DATA_BABY_ID) || tinyfoes$$isBabyfied();
	}
	@Override
	public boolean tinyfoes$$isBaby() {
		return this.getEntityData().get(DATA_BABY_ID);
	}
}
