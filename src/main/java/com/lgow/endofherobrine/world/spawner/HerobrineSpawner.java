package com.lgow.endofherobrine.world.spawner;

import com.lgow.endofherobrine.ModUtil;
import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.entity.EntityInit;
import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class HerobrineSpawner {

    private final SpawnerData data;
    private final RandomSource random = RandomSource.create();
    private int tickDelay, spawnDelay, spawnChance;


    public HerobrineSpawner(MinecraftServer server, String key) {
        this.data = SpawnerSavedData.get(server).getSpawnerData(key);
        this.tickDelay = ModConfigs.TICK_DELAY.get();
        this.spawnDelay = data.getSpawnDelay();
        this.spawnChance = data.getSpawnChance();
        if (this.spawnDelay == 0 && this.spawnChance == 0) {
            this.spawnDelay = ModConfigs.SPAWN_DELAY.get();
            this.data.setSpawnDelay(this.spawnDelay);
            this.spawnChance = ModConfigs.SPAWN_CHANCE.get();
            this.data.setSpawnChance(this.spawnChance);
        }

    }

    public boolean noHerobrineExists(Level level){
        List<AbstractHerobrine> list = new ArrayList<>();
        for(ServerPlayer serverplayer : level.getServer().getPlayerList().getPlayers()) {
            list.addAll(level.getEntitiesOfClass(AbstractHerobrine.class, serverplayer.getBoundingBox().inflate(256)));
        }
        return list.isEmpty();
    }

    public int tick(ServerLevel pLevel) {
        if (!(ModUtil.destructionScore(pLevel, 1) && noHerobrineExists(pLevel))) {
            return 0;
        } else if (--this.tickDelay > 0) {
            return 0;
        } else {
            this.tickDelay =  ModConfigs.TICK_DELAY.get();
            this.spawnDelay -= ModConfigs.SPAWN_DELAY.get();
            this.data.setSpawnDelay(this.spawnDelay);
            if (this.spawnDelay > 0) {
                return 0;
            } else {
                this.spawnDelay = ModConfigs.SPAWN_DELAY.get();
                if (!pLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    return 0;
                } else {
                    if (this.random.nextInt(100) > this.spawnChance) {
                        return 0;
                    } else if (this.spawn(pLevel)) {
                        this.spawnChance = ModConfigs.SPAWN_CHANCE.get();
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }

    private boolean spawn(ServerLevel pServerLevel) {
        Player player = pServerLevel.getRandomPlayer();
        if (player == null) {
            return true;
        } else {
            BlockPos blockPos = this.findSpawnPositionNear(pServerLevel, player.blockPosition(), 48);
            if (blockPos != null && this.hasEnoughSpace(pServerLevel, blockPos)) {
                AbstractHerobrine herobrine;
                    if(this.random.nextInt(6) < 3) {
                        herobrine = EntityInit.LURKER.get().spawn(pServerLevel, blockPos, MobSpawnType.EVENT);
                    } else {
                        herobrine = EntityInit.BUILDER.get().spawn(pServerLevel, blockPos, MobSpawnType.EVENT);
                }
                return herobrine != null;
            }
            return false;
        }
    }

    @Nullable
    private BlockPos findSpawnPositionNear(LevelReader pLevel, BlockPos pPos, int pMaxDistance) {
        BlockPos blockpos = null;

        for(int i = 0; i < 10; ++i) {
            int j = pPos.getX() + this.random.nextInt(pMaxDistance * 2) - pMaxDistance;
            int k = pPos.getZ() + this.random.nextInt(pMaxDistance * 2) - pMaxDistance;
            int l = pLevel.getHeight(Heightmap.Types.WORLD_SURFACE, j, k);
            BlockPos blockpos1 = new BlockPos(j, l, k);
            if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, pLevel, blockpos1, EntityType.WANDERING_TRADER)) {
                blockpos = blockpos1;
                break;
            }
        }
        return blockpos;
    }

    private boolean hasEnoughSpace(BlockGetter pLevel, BlockPos pPos) {
        for(BlockPos blockpos : BlockPos.betweenClosed(pPos, pPos.offset(1, 2, 1))) {
            if (!pLevel.getBlockState(blockpos).getCollisionShape(pLevel, blockpos).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}