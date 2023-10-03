package net.tinyallies.quilt;

import net.tinyallies.TinyAlliesCommon;
import net.tinyallies.client.TinyAlliesClientCommon;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class TinyAlliesQuilt implements ModInitializer {
	@Override
	public void onInitialize(ModContainer mod) {
		TinyAlliesClientCommon.preClientInit();
		TinyAlliesCommon.init();
	}
}
