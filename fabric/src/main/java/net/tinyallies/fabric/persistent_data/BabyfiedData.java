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

	public static boolean updateIsBaby(Player player, boolean amount) {
		CompoundTag nbt = ((IEntityDataSaver)player).getPersistentData();
		nbt.putBoolean("IsBaby", amount);
		return amount;
	}

	public static boolean updateIsBabyfied(Player player, boolean amount) {
		CompoundTag nbt = ((IEntityDataSaver)player).getPersistentData();
		nbt.putBoolean("IsBabyfied", amount);
		syncIsBabyfied(amount, player);
		return amount;
	}

	public static void syncIsBabyfied(boolean isBabyfied, Player serverPlayer) {
		FriendlyByteBuf buffer = PacketByteBufs.create();
		buffer.writeBoolean(isBabyfied);
		buffer.writeUUID(serverPlayer.getUUID());
		ClientPlayNetworking.send(ModMessages.C_2_S, buffer);
	}
}
