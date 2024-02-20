package net.tinyallies.fabric.persistent_data;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class BabyfiedData {
	boolean isbabyfied = false;

	public static boolean updateIsBaby(Player player, boolean bl) {
		CompoundTag nbt = ((IEntityDataSaver)player).getPersistentData();
		nbt.putBoolean("IsBaby", bl);
		syncIsBabyfied(bl, player);
		return bl;
	}

//	public static boolean updateIsBabyfied(Player player, boolean bl) {
//		CompoundTag nbt = ((IEntityDataSaver)player).getPersistentData();
//		nbt.putBoolean("IsBabyfied", bl);
//		syncIsBabyfied(bl, player);
//		return bl;
//	}

//	public static void syncIsBabyfied(boolean isBabyfied, Player serverPlayer) {
//		FriendlyByteBuf buffer = PacketByteBufs.create();
//		buffer.writeBoolean(isBabyfied);
//		buffer.writeUUID(serverPlayer.getUUID());
//		ClientPlayNetworking.send(ModMessages.C_2_S, buffer);
//	}

	public static void syncIsBabyfied(boolean isBabyfied, Player serverPlayer) {
		FriendlyByteBuf buffer = PacketByteBufs.create();
		buffer.writeBoolean(isBabyfied);
		buffer.writeUUID(serverPlayer.getUUID());
		ClientPlayNetworking.send(ModMessages.C_2_S, buffer);
	}
}
