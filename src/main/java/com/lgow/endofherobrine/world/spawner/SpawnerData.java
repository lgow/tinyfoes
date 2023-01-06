package com.lgow.endofherobrine.world.spawner;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class SpawnerData {
    private final SpawnerSavedData data;
    private int herobrineSpawnDelay;
    private int herobrineSpawnChance;

    public SpawnerData(SpawnerSavedData data)
    {
        this.data = data;
    }

    public void setSpawnDelay(int herobrineSpawnDelay) {
        this.herobrineSpawnDelay = herobrineSpawnDelay;
        this.data.setDirty(true);
    }

    public void setSpawnChance(int i) {
        this.herobrineSpawnChance = i;
        this.data.setDirty(true);
    }

    public int getSpawnDelay()
    {
        return herobrineSpawnDelay;
    }

    public int getSpawnChance()
    {
        return herobrineSpawnChance;
    }

    public void read(CompoundTag compound) {
        if(compound.contains("HerobrineSpawnDelay", Tag.TAG_INT)){
            this.herobrineSpawnDelay = compound.getInt("HerobrineSpawnDelay");
        }
        if(compound.contains("HerobrineSpawnChance", Tag.TAG_INT)) {
            this.herobrineSpawnChance = compound.getInt("HerobrineSpawnChance");
        }
    }

    public CompoundTag write(CompoundTag compound) {
        compound.putInt("HerobrineSpawnDelay", this.herobrineSpawnDelay);
        compound.putInt("HerobrineSpawnChance", this.herobrineSpawnChance);
        return compound;
    }
}
