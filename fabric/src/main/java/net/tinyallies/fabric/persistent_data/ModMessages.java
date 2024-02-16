package net.tinyallies.fabric.persistent_data;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.tinyallies.common.util.TinyFoesResLoc;

public class ModMessages {
	public static final ResourceLocation C_2_S = new TinyFoesResLoc("is_babyfied_c2s");

	public static final ResourceLocation S_2_C = new TinyFoesResLoc("is_babyfied_s2c");

	public static void registerC2SPackets() {
		ServerPlayNetworking.registerGlobalReceiver(C_2_S, IsBabyfiedSyncPacket::clientToServer);
	}

	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(S_2_C, IsBabyfiedSyncPacket::serverToClient);
	}
}
