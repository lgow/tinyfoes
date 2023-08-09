package com.lgow.endofherobrine.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class PlayerEyesSkin {

	private int skin;

	public int getSkin() {
		return skin;
	}

	public void setSkin(int add, ServerPlayer serverPlayer) {
		this.skin = add;
		this.syncSkin(serverPlayer);
	}

	public void syncSkin(ServerPlayer serverPlayer) {
		//		ModPacketHandler.sendToPlayer(new SyncPlayerEyesSkin(this.skin), serverPlayer);
	}

	public void copyFrom(PlayerEyesSkin source, ServerPlayer serverPlayer) {
		this.skin = source.skin;
		this.syncSkin(serverPlayer);
	}

	public void saveNBTData(CompoundTag nbt) {
		nbt.putInt("eye_skin", skin);
	}

	public void loadNBTData(CompoundTag nbt) {
		skin = nbt.getInt("eye_skin");
	}
}

