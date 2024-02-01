package net.tinyallies.fabric.persistent_data;

import net.minecraft.nbt.CompoundTag;

public class BabyfiedData {

	public static boolean updateIsBaby(IEntityDataSaver player, boolean amount) {
		CompoundTag nbt = player.getPersistentData();
		nbt.putBoolean("IsBaby", amount);
		return amount;
	}

	public static boolean updateIsBabyfied(IEntityDataSaver player, boolean amount) {
		CompoundTag nbt = player.getPersistentData();
		nbt.putBoolean("IsBabyfied", amount);
		return amount;
	}
}
