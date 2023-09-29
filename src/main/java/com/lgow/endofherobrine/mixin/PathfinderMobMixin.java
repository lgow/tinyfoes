package com.lgow.endofherobrine.mixin;

import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.event.WrathHandler;
import com.lgow.endofherobrine.util.ModUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import static com.lgow.endofherobrine.event.WrathHandler.probability;
import static com.lgow.endofherobrine.util.ModUtil.possessMob;

@Mixin({ PathfinderMob.class })
public class PathfinderMobMixin extends Mob {
	protected PathfinderMobMixin(EntityType<? extends Mob> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		if (probability(pLevel.getLevel(), 0.6F)) {
			possessMob(this, pLevel.getLevel(), false, true);
		}
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}
}
