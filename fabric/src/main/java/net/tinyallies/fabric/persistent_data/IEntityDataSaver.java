package net.tinyallies.fabric.persistent_data;

import net.minecraft.nbt.CompoundTag;

public interface IEntityDataSaver {
	CompoundTag getPersistentData();
}