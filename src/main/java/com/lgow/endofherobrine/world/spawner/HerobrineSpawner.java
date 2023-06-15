package com.lgow.endofherobrine.world.spawner;

import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import com.lgow.endofherobrine.event.WrathIncreaserEvents;
import com.lgow.endofherobrine.util.ModUtil;
import com.lgow.endofherobrine.world.data.ModSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.List;

public class HerobrineSpawner {
	private final SpawnerData data;

	private final RandomSource random = RandomSource.create();

	private int tickDelay, currentSpawnDelay, currentSpawnChance;

	private final int spawnDelay, spawnChance;

	public HerobrineSpawner(MinecraftServer server, String key) {
		this.data = ModSavedData.get(server).getSpawnerData(key);
		this.tickDelay = 600;
		this.currentSpawnDelay = this.data.getSpawnDelay();
		this.currentSpawnChance = this.data.getSpawnChance();
		this.spawnDelay = ModConfigs.SPAWN_DELAY.get();
		this.spawnChance = ModConfigs.SPAWN_CHANCE.get();
		if (this.currentSpawnDelay == 0 && this.currentSpawnChance == 0) {
			this.currentSpawnDelay = this.spawnDelay;
			this.currentSpawnChance = this.spawnChance;
			this.data.setSpawnDelay(this.currentSpawnDelay);
			this.data.setSpawnChance(this.currentSpawnChance);
		}
	}

	public void tick(ServerLevel level) {
		if (this.spawnChance != 0 && WrathIncreaserEvents.getHerobrineHostility(level) > 0 && ModUtil.noHerobrineExists(
				level)) {
			if (--this.tickDelay <= 0) {
				int delay = Math.max(this.spawnDelay / 20, 1);
				this.tickDelay = delay;
				this.currentSpawnDelay -= delay;
				this.data.setSpawnDelay(this.currentSpawnDelay);
				if (this.currentSpawnDelay <= 0) {
					this.currentSpawnDelay = this.spawnDelay;
					if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
						int spawnChance = this.currentSpawnChance;
						this.currentSpawnChance = Mth.clamp(this.currentSpawnChance + this.spawnChance,
								this.spawnChance, 100);
						this.data.setSpawnChance(this.currentSpawnChance);
						if (level.getRandom().nextInt(100) <= spawnChance) {
							if (this.spawnVariant(level)) {
								this.currentSpawnChance = this.spawnChance;
							}
						}
					}
				}
			}
		}
	}

	private boolean spawnVariant(ServerLevel level) {
		List<? extends Player> players = level.players();
		if (players.isEmpty()) {
			return false;
		}
		Player randomPlayer = players.get(level.getRandom().nextInt(players.size()));
		if (randomPlayer == null) {
			return true;
		}
		else {
			BlockPos blockPos = this.findPositionAroundPlayer(randomPlayer.level, randomPlayer.getOnPos(), 48);
			AbstractHerobrine herobrine = null;
			if (blockPos != null && this.hasEnoughSpace(level, blockPos)) {
				if (this.random.nextInt(7) != 0) {
					herobrine = EntityInit.LURKER.get().spawn(level, blockPos, MobSpawnType.EVENT);
				}
				else if (!ModConfigs.SPAWN_BUILDER.get()) {
					herobrine = EntityInit.BUILDER.get().spawn(level, blockPos, MobSpawnType.EVENT);
				}
				return herobrine != null;
			}
			return false;
		}
	}

	@Nullable
	private BlockPos findPositionAroundPlayer(LevelReader pLevel, BlockPos pPos, int pMaxDistance) {
		BlockPos blockpos = null;
		for (int i = 0; i < 10; ++i) {
			int j = pPos.getX() + this.random.nextInt(pMaxDistance * 2) - pMaxDistance;
			int k = pPos.getZ() + this.random.nextInt(pMaxDistance * 2) - pMaxDistance;
			int l = pLevel.getHeight(Heightmap.Types.WORLD_SURFACE, j, k);
			BlockPos blockpos1 = new BlockPos(j, l, k);
			if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, pLevel, blockpos1,
					EntityType.WANDERING_TRADER)) {
				blockpos = blockpos1;
				break;
			}
		}
		return blockpos;
	}

	private boolean hasEnoughSpace(BlockGetter pLevel, BlockPos pPos) {
		for (BlockPos blockpos : BlockPos.betweenClosed(pPos, pPos.offset(1, 2, 1))) {
			if (!pLevel.getBlockState(blockpos).getCollisionShape(pLevel, blockpos).isEmpty()) {
				return false;
			}
		}
		return true;
	}
}