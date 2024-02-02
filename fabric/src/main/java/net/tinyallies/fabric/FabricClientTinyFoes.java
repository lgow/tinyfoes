package net.tinyallies.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.tinyallies.common.client.TinyFoesClientCommon;
import net.tinyallies.fabric.persistent_data.ModMessages;

public class FabricClientTinyFoes implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TinyFoesClientCommon.preClientInit();
        ModMessages.registerS2CPackets();
    }
}