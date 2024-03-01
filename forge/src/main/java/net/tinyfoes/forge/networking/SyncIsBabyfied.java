package net.tinyfoes.forge.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.tinyfoes.forge.capabilities.CapabilityProvider;

import java.util.function.Supplier;

public class SyncIsBabyfied {
	private final boolean value;

	public SyncIsBabyfied(boolean value) {
		this.value = value;
	}

	public SyncIsBabyfied(FriendlyByteBuf buf) {
		this.value = buf.readBoolean();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBoolean(value);
	}

	public void handle(Supplier<NetworkEvent.Context> supplier) {

		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			Minecraft.getInstance().player.getCapability(CapabilityProvider.IS_BABYFIED_CAPABILITY).ifPresent((s) -> {
				s.setValue(value, Minecraft.getInstance().player);
			});
			PlayerData.setBabyfied(value);
		});
	}
}
