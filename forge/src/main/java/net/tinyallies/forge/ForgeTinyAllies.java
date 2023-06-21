package net.tinyallies.forge;

import dev.architectury.platform.forge.EventBuses;
import net.tinyallies.CommonTinyAllies;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CommonTinyAllies.MOD_ID)
public class ForgeTinyAllies {
    public ForgeTinyAllies() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(CommonTinyAllies.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        CommonTinyAllies.init();
    }
}
