package net.tinyallies.fabric.persistent_data;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.tinyallies.common.util.TinyFoesResLoc;

public class ModMessages {
	public static final ResourceLocation BABYFIED_SYNC_ID = new TinyFoesResLoc("is_babyfied");

	public static void registerC2SPackets() {
	}

	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(BABYFIED_SYNC_ID, IsBabyfiedSyncS2CPacket::receive);
	}
}
