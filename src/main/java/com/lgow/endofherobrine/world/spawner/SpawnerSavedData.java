package com.lgow.endofherobrine.world.spawner;

import com.lgow.endofherobrine.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class SpawnerSavedData extends SavedData
{
    private static final String DATA_NAME = Main.MOD_ID + "herobrine_variant";

    private final Map<String, SpawnerData> data = new HashMap<>();

    public SpawnerSavedData() {}

    public SpawnerData getSpawnerData(String key) {
        return this.data.computeIfAbsent(key, s -> new SpawnerData(this));
    }

    public SpawnerSavedData read(CompoundTag tag) {
        if(tag.contains("HerobrineSpawnDelay", Tag.TAG_INT)) {
            this.getSpawnerData("Herobrine").setSpawnDelay(tag.getInt("HerobrineSpawnDelay"));
        }
        if(tag.contains("HerobrineSpawnChance", Tag.TAG_INT)) {
            this.getSpawnerData("Herobrine").setSpawnChance(tag.getInt("HerobrineSpawnChance"));
        }
        if(tag.contains("Data", Tag.TAG_LIST)) {
            this.data.clear();
            ListTag list = tag.getList("Data", Tag.TAG_COMPOUND);
            list.forEach(nbt -> {
                CompoundTag nbtTag = (CompoundTag) nbt;
                String key = nbtTag.getString("Key");
                SpawnerData data = new SpawnerData(this);
                data.read(nbtTag);
                this.data.put(key, data);
            });
        }
        return this;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        this.data.forEach((s, goblinData) -> {
            CompoundTag goblinTag = new CompoundTag();
            goblinData.write(goblinTag);
            goblinTag.putString("Key", s);
            list.add(goblinTag);
        });
        tag.put("Data", list);
        return tag;
    }

    public static SpawnerSavedData get(MinecraftServer server) {
        ServerLevel level = server.getLevel(Level.OVERWORLD);
        return level.getDataStorage().computeIfAbsent(tag -> new SpawnerSavedData().read(tag), SpawnerSavedData::new, DATA_NAME);
    }
}