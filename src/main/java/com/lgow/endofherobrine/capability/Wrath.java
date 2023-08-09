package com.lgow.endofherobrine.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class Wrath {

	private int value;

	public int getValue() {
		return value;
	}

	public void addValue(int add, ServerPlayer serverPlayer) {
		this.value += add;
		this.syncValue(serverPlayer);
	}

	public void subValue(int sub, ServerPlayer serverPlayer) {
		this.value = Math.max(value - sub, 0);
		this.syncValue(serverPlayer);
	}

	public void syncValue(ServerPlayer serverPlayer) {
//		ModPacketHandler.sendToPlayer(new SyncWrathLevel(this.value), serverPlayer);
	}

	public void copyFrom(Wrath source, ServerPlayer serverPlayer) {
		this.value = source.value;
		this.syncValue(serverPlayer);
	}

	public void saveNBTData(CompoundTag nbt) {
		nbt.putInt("wrath", value);
	}

	public void loadNBTData(CompoundTag nbt) {
		value = nbt.getInt("wrath");
	}
}
