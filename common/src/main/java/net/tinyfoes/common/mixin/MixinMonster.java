package net.tinyfoes.common.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tinyfoes.common.entity.BabyfiableEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Monster.class)
public abstract class MixinMonster extends PathfinderMob implements BabyfiableEntity {
	protected MixinMonster(EntityType<? extends PathfinderMob> entityType, Level level) {
		super(entityType, level);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		this.setBaby(random.nextFloat() < 0.05F);
		return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
	}

	@Override
	public boolean isBaby() {
		return $isBaby() || $isBabyfied();
	}

	@Override
	public void setBaby(boolean b) {
		$setBaby(b);
	}

	public int getExperienceReward() {
		if (isBaby()) {
			this.xpReward = (int) ((double) this.xpReward * 2.5);
		}
		return super.getExperienceReward();
	}

	@Override
	public double getMyRidingOffset() {
		return this.isBaby() ? 0.0 : -0.45;
	}
}

