package net.tinyallies.fabric.persistent_data;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.UUID;

public class IsBabyfiedSyncPacket {
	public static void clientToServer(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
		UUID uuid = buf.readUUID();
		boolean readBoolean = buf.readBoolean();
		server.execute(()->{
			((IEntityDataSaver)player).getPersistentData().putBoolean(
					"IsBabyfied", readBoolean);
		});
	}

	public static void serverToClient(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
		UUID uuid = buf.readUUID();
		boolean readBoolean = buf.readBoolean();
		client.execute(()->{
			((IEntityDataSaver) handler.getLevel().getPlayerByUUID(uuid)).getPersistentData().putBoolean(
					"IsBabyfied", readBoolean);
		});
	}
}
