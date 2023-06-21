package net.tinyallies.quilt;

import net.tinyallies.CommonTinyAllies;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class QuiltTinyAllies implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        CommonTinyAllies.init();
    }
}
