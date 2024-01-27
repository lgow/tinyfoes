package net.tinyallies.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.tinyallies.client.TinyFoesClientCommon;

public class FabricClientTinyFoes implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TinyFoesClientCommon.preClientInit();
    }
}