package net.tinyallies.forge.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.tinyallies.forge.networking.ForgePacketHandler;
import net.tinyallies.forge.networking.SyncIsBabyfied;

@AutoRegisterCapability
public class IsBabyfied {
	private boolean value;

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean b, ServerPlayer serverPlayer) {
		this.value = b;
		this.syncValue(serverPlayer);
	}

	public void syncValue(ServerPlayer serverPlayer) {
		ForgePacketHandler.sendToPlayer(new SyncIsBabyfied(this.value), serverPlayer);
	}

	public void copyFrom(IsBabyfied source, ServerPlayer serverPlayer) {
		this.value = source.getValue();
		this.syncValue(serverPlayer);
	}

	public void saveNBTData(CompoundTag nbt) {
		nbt.putBoolean("is_babyfied", value);
	}

	public void loadNBTData(CompoundTag nbt) {
		value = nbt.getBoolean("is_babyfied");
	}
}
