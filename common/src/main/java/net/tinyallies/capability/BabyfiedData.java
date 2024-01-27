package net.tinyallies.capability;

import net.minecraft.nbt.CompoundTag;
import net.tinyallies.util.IEntityDataSaver;

public class BabyfiedData {

	public static boolean updateIsBabyfied(IEntityDataSaver player, boolean amount) {
		CompoundTag nbt = player.getPersistentData();
		nbt.putBoolean("IsBabyfied", amount);
		return amount;
	}
}
