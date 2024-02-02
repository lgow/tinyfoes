package net.tinyallies.forge.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.tinyallies.forge.networking.ForgePacketHandler;
import net.tinyallies.forge.networking.SyncIsBaby;

@AutoRegisterCapability
public class IsBaby {
	private boolean value;

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean b, LivingEntity serverPlayer) {
		this.value = b;
		this.syncValue(serverPlayer);
	}

	public void syncValue(LivingEntity serverPlayer) {
		if(serverPlayer instanceof ServerPlayer) {
			ForgePacketHandler.sendToPlayer(new SyncIsBaby(this.value), (ServerPlayer) serverPlayer);
		}
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
