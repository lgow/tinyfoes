package net.tinyfoes.common.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tinyfoes.common.config.TinyFoesConfigs;
import net.tinyfoes.common.entity.BabyfiableEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlyingMob.class)
public abstract class MixinFlyingMob extends Mob implements BabyfiableEntity {
	protected MixinFlyingMob(EntityType<? extends Mob> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public boolean isBaby() {
		return tinyfoes$$isBaby() || tinyfoes$$isBabyfied();
	}

	@Override
	public void setBaby(boolean b) {
		this.tinyfoes$$setBaby(b);
	}

	@Override
	public int getExperienceReward() {
		if (isBaby()) {
			this.xpReward = (int) ((double) this.xpReward * 2.5);
		}
		return super.getExperienceReward();
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		this.setBaby(random.nextFloat() < TinyFoesConfigs.SPAWN_AS_BABY_ODDS.get());
		return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
	}
}
