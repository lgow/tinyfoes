package net.tinyallies.capability;

import net.minecraft.nbt.CompoundTag;
import net.tinyallies.util.IEntityDataSaver;

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
