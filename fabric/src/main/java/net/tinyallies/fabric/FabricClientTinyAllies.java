package net.tinyallies.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.tinyallies.client.TinyAlliesClientCommon;

public class FabricClientTinyAllies implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TinyAlliesClientCommon.preClientInit();
    }
}