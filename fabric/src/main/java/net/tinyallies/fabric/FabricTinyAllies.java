package net.tinyallies.fabric;

import net.tinyallies.TinyAlliesCommon;
import net.fabricmc.api.ModInitializer;
import net.tinyallies.client.TinyAlliesCommonClient;

public class FabricTinyAllies implements ModInitializer {
    @Override
    public void onInitialize() {
        TinyAlliesCommonClient.preClientInit();
        TinyAlliesCommon.init();
    }
}
