package net.tinyfoes.forge.networking;

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


	//	private CompoundNBT data;
	//
	//	XiuXianStateSyncMessage(PacketBuffer buf) {
	//		this.data = buf.readCompoundTag();
	//	}
	//
	//	public XiuXianStateSyncMessage(CompoundNBT nbt) {
	//		this.data = nbt;
	//	}
	//
	//	void encode(PacketBuffer buf) {
	//		buf.writeCompoundTag(data);
	//	}
	//
	//	void handle(Supplier<NetworkEvent.Context> context) {
	//		NetworkEvent.Context ctx = context.get();
	//		ctx.enqueueWork(() -> {
	//			
	//		});
	//		ctx.setPacketHandled(true);
	//	}
	//}
}
