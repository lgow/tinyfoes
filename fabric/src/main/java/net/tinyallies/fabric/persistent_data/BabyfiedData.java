package net.tinyallies.fabric.persistent_data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class BabyfiedData {

	public static boolean updateIsBaby(IEntityDataSaver player, boolean amount) {
		CompoundTag nbt = player.getPersistentData();
		nbt.putBoolean("IsBaby", amount);
		return amount;
	}

	public static boolean updateIsBabyfied(IEntityDataSaver player, boolean amount) {
		CompoundTag nbt = player.getPersistentData();
		nbt.putBoolean("IsBabyfied", amount);
		if(player instanceof ServerPlayer){
			syncIsBabyfied(amount, (ServerPlayer) player);
		}
		return amount;
	}

	public static void syncIsBabyfied(boolean isBabyfied, ServerPlayer player) {
		FriendlyByteBuf buffer = PacketByteBufs.create();
		buffer.writeBoolean(isBabyfied);
		ServerPlayNetworking.send(player, ModMessages.BABYFIED_SYNC_ID, buffer);
	}
}
