package net.tinyallies.fabric;

import net.tinyallies.CommonTinyAllies;
import net.fabricmc.api.ModInitializer;

public class FabricTinyAllies implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonTinyAllies.init();
    }
}
