package net.tinyallies.forge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncIsBaby {
	private final boolean value;

	public SyncIsBaby(boolean value) {
		this.value = value;
	}

	public SyncIsBaby(FriendlyByteBuf buf) {
		this.value = buf.readBoolean();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBoolean(value);
	}

	public void handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> PlayerData.setBaby(value));
	}
}
