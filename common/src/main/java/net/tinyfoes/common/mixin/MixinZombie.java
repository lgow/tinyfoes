package net.tinyfoes.common.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.tinyfoes.common.config.TinyFoesConfigs;
import net.tinyfoes.common.entity.BabyfiableEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public abstract class MixinZombie extends Monster implements BabyfiableEntity {
	@Shadow @Final private static EntityDataAccessor<Boolean> DATA_BABY_ID;

	protected MixinZombie(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "getSpawnAsBabyOdds", at = @At("HEAD"), cancellable = true)
	private static void getSpawnAsBabyOdds(RandomSource randomSource, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(randomSource.nextFloat() < TinyFoesConfigs.SPAWN_AS_BABY_ODDS.get());
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
