package net.tinyallies.forge.networking;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tinyallies.common.util.TinyFoesResLoc;

public class ForgePacketHandler {
	private static SimpleChannel INSTANCE;
	private static int packetId = 0;

	private static int id() {
		return packetId++;
	}

	public static void register() {
		SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new TinyFoesResLoc("messages")).networkProtocolVersion(
				() -> "1.0").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();
		INSTANCE = net;
		//SYNC
		net.messageBuilder(SyncIsBaby.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(SyncIsBaby::new).encoder(
				SyncIsBaby::toBytes).consumerMainThread(SyncIsBaby::handle).add();
		net.messageBuilder(SyncIsBabyfied.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(SyncIsBabyfied::new)
				.encoder(SyncIsBabyfied::toBytes).consumerMainThread(SyncIsBabyfied::handle).add();
	}

	public static <MSG> void sendToServer(MSG message) {
		INSTANCE.sendToServer(message);
	}

	public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	}
}
