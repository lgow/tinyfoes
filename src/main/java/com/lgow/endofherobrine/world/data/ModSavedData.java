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
	private final Map<String, SpawnerData> data = new HashMap<>();
	private boolean defeatedHerobrine, herobrineIsDead, resurrectedHerobrine;
	private int herobrineRestTimer;

	public ModSavedData() { }

	public static ModSavedData get(MinecraftServer server) {
		ServerLevel level = server.getLevel(Level.OVERWORLD);
		return level.getDataStorage().computeIfAbsent(nbt -> new ModSavedData().read(nbt), ModSavedData::new,
				Main.MOD_ID);
	}

	public SpawnerData getSpawnerData(String key) {
		return this.data.computeIfAbsent(key, s -> new SpawnerData(this));
	}

	public boolean getDefeatedHerobrine() {
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

	public boolean herobrineIsDead() {
		return herobrineIsDead;
	}

	public void setHerobrineIsDead(boolean b) {
		herobrineIsDead = b;
		setDirty();
	}

	public boolean getResurrectedHerobrine() {
		return resurrectedHerobrine;
	}

	public void setResurrectedHerobrine(boolean b) {
		resurrectedHerobrine = b;
		setDirty();
	}

	public boolean getHerobrineIsDeadOrResting() {
		return herobrineIsDead || herobrineRestTimer > 0;
	}

	public ModSavedData read(CompoundTag nbt) {
		if (nbt.contains("HerobrineSpawnDelay", Tag.TAG_INT)) {
			this.getSpawnerData("Herobrine").setSpawnDelay(nbt.getInt("HerobrineSpawnDelay"));
		}
		if (nbt.contains("HerobrineSpawnChance", Tag.TAG_INT)) {
			this.getSpawnerData("Herobrine").setSpawnChance(nbt.getInt("HerobrineSpawnChance"));
		}
		if (nbt.contains("Data", Tag.TAG_LIST)) {
			this.data.clear();
			ListTag list = nbt.getList("Data", Tag.TAG_COMPOUND);
			list.forEach(tag -> {
				CompoundTag nbtTag = (CompoundTag) tag;
				String key = nbtTag.getString("Key");
				SpawnerData data = new SpawnerData(this);
				data.read(nbtTag);
				this.data.put(key, data);
			});
		}
		defeatedHerobrine = nbt.getBoolean("DefeatedHerobrine");
		herobrineRestTimer = nbt.getInt("HerobrineRestTimer");
		herobrineIsDead = nbt.getBoolean("HerobrineIsDead");
		resurrectedHerobrine = nbt.getBoolean("ResurrectedHerobrine");
		return this;
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		ListTag list = new ListTag();
		this.data.forEach((s, data) -> {
			CompoundTag key = new CompoundTag();
			data.write(key);
			key.putString("Key", s);
			list.add(key);
		});
		nbt.put("Data", list);
		nbt.putBoolean("DefeatedHerobrine", defeatedHerobrine);
		nbt.putInt("HerobrineRestTimer", herobrineRestTimer);
		nbt.putBoolean("HerobrineIsDead", herobrineIsDead);
		nbt.putBoolean("ResurrectedHerobrine", resurrectedHerobrine);
		return nbt;
	}
}