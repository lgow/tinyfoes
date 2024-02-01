package net.tinyallies.forge.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.tinyallies.forge.networking.ForgePacketHandler;
import net.tinyallies.forge.networking.SyncIsBaby;

@AutoRegisterCapability
public class IsBaby {
	private boolean value;

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean b, ServerPlayer serverPlayer) {
		this.value = b;
		this.syncValue(serverPlayer);
	}

	public void syncValue(ServerPlayer serverPlayer) {
		ForgePacketHandler.sendToPlayer(new SyncIsBaby(this.value), serverPlayer);
	}

	public void copyFrom(IsBaby source, ServerPlayer serverPlayer) {
		this.value = source.getValue();
		this.syncValue(serverPlayer);
	}

	public void saveNBTData(CompoundTag nbt) {
		nbt.putBoolean("is_baby", value);
	}

	public void loadNBTData(CompoundTag nbt) {
		value = nbt.getBoolean("is_baby");
	}
}
