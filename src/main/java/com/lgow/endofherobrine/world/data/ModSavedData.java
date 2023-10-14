package com.lgow.endofherobrine.world.data;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.world.spawner.SpawnerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class ModSavedData extends SavedData {
	private final Map<String, SpawnerData> spawnerDataMap = new HashMap<>();
	private boolean defeatedHerobrine, herobrineIsDead, resurrectedHerobrine;
	private int herobrineRestTimer, lastLetterIndex;

	public ModSavedData() { }

	public static ModSavedData get(MinecraftServer server) {
		ServerLevel level = server.getLevel(Level.OVERWORLD);
		return level.getDataStorage().computeIfAbsent(nbt -> new ModSavedData().read(nbt), ModSavedData::new,
				Main.MOD_ID);
	}

	public SpawnerData getSpawnerData(String key) {
		return this.spawnerDataMap.computeIfAbsent(key, s -> new SpawnerData(this));
	}

	public boolean hasDefeatedHerobrine() {
		return defeatedHerobrine;
	}

	public void setDefeatedHerobrine(boolean b) {
		defeatedHerobrine = b;
		setDirty();
	}

	public int getHerobrineRestTimer() {
		return herobrineRestTimer;
	}

	public void setHerobrineRestTimer(int b) {
		herobrineRestTimer = b;
		setDirty();
	}

	public void setHerobrineIsDead(boolean b) {
		herobrineIsDead = b;
		setDirty();
	}

	public boolean hasResurrectedHerobrine() {
		return resurrectedHerobrine;
	}

	public void setResurrectedHerobrine(boolean b) {
		resurrectedHerobrine = b;
		setDirty();
	}

	public boolean herobrineIsDead() {
		return herobrineIsDead;
	}

	public boolean isHerobrineDeadOrResting() {
		return herobrineIsDead || herobrineRestTimer > 0;
	}


	public ModSavedData read(CompoundTag nbt) {
		if (nbt.contains("HerobrineSpawnDelay", Tag.TAG_INT)) {
			this.getSpawnerData("Herobrine").setSpawnDelay(nbt.getInt("HerobrineSpawnDelay"));
		}
		if (nbt.contains("HerobrineSpawnChance", Tag.TAG_INT)) {
			this.getSpawnerData("Herobrine").setSpawnChance(nbt.getInt("HerobrineSpawnChance"));
		}
		if (nbt.contains("SpawnData", Tag.TAG_LIST)) {
			this.spawnerDataMap.clear();
			ListTag list = nbt.getList("SpawnData", Tag.TAG_COMPOUND);
			list.forEach(tag -> {
				CompoundTag nbtTag = (CompoundTag) tag;
				String key = nbtTag.getString("Key");
				SpawnerData data = new SpawnerData(this);
				data.read(nbtTag);
				this.spawnerDataMap.put(key, data);
			});
		}
		if (nbt.contains("DefeatedHerobrine", Tag.TAG_BYTE)) {
			defeatedHerobrine = nbt.getBoolean("DefeatedHerobrine");
		}
		if (nbt.contains("HerobrineRestTimer", Tag.TAG_INT)) { herobrineRestTimer = nbt.getInt("HerobrineRestTimer"); }
		if (nbt.contains("HerobrineIsDead", Tag.TAG_BYTE)) { herobrineIsDead = nbt.getBoolean("HerobrineIsDead"); }
		if (nbt.contains("ResurrectedHerobrine", Tag.TAG_BYTE)) {
			resurrectedHerobrine = nbt.getBoolean("ResurrectedHerobrine");
		}
		if (nbt.contains("LastLetterIndex", Tag.TAG_INT)) { lastLetterIndex = nbt.getInt("LastLetterIndex"); }
		return this;
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		// Create "SpawnData" section
		ListTag spawnDataList = new ListTag();
		this.spawnerDataMap.forEach((s, data) -> {
			CompoundTag key = new CompoundTag();
			data.write(key);
			key.putString("Key", s);
			spawnDataList.add(key);
		});
		nbt.put("SpawnData", spawnDataList);
		// Create "Herobrine" section
		nbt.putInt("HerobrineRestTimer", herobrineRestTimer);
		nbt.putBoolean("DefeatedHerobrine", defeatedHerobrine);
		nbt.putBoolean("ResurrectedHerobrine", resurrectedHerobrine);
		nbt.putBoolean("HerobrineIsDead", herobrineIsDead);
		nbt.putInt("LastLetterIndex", lastLetterIndex);
		return nbt;
	}

	public int getLastLetterIndex() {
		return this.lastLetterIndex;
	}

	public void updateLastLetterIndex() {
		if (this.lastLetterIndex >= 8) {
			this.lastLetterIndex = 0;
		}
		else {
			this.lastLetterIndex++;
		}
		this.setDirty();
	}
}