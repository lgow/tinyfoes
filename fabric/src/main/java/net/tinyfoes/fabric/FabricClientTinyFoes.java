package net.tinyfoes.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.tinyfoes.common.client.TinyFoesClientCommon;

public class FabricClientTinyFoes implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TinyFoesClientCommon.preClientInit();
    }
}