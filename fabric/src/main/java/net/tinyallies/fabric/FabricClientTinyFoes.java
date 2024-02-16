package net.tinyallies.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.chat.Component;
import net.tinyallies.common.client.TinyFoesClientCommon;
import net.tinyallies.fabric.persistent_data.IEntityDataSaver;
import net.tinyallies.fabric.persistent_data.ModMessages;

public class FabricClientTinyFoes implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TinyFoesClientCommon.preClientInit();
        ModMessages.registerS2CPackets();
        ModMessages.registerC2SPackets();
    }
}