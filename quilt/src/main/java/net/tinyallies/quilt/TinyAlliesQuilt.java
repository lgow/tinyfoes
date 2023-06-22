package net.tinyallies.quilt;

import net.tinyallies.TinyAlliesCommon;
import net.tinyallies.client.TinyAlliesCommonClient;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class TinyAlliesQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        TinyAlliesCommonClient.preClientInit();
        TinyAlliesCommon.init();
    }
}
